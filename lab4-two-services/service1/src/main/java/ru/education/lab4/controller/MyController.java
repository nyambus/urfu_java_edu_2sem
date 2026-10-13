package ru.education.lab4.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.education.lab4.exception.UnsupportedCodeException;
import ru.education.lab4.exception.ValidationFailedException;
import ru.education.lab4.model.Request;
import ru.education.lab4.model.Response;
import ru.education.lab4.service.ModifyRequestService;
import ru.education.lab4.service.ModifyResponseService;
import ru.education.lab4.util.DateTimeUtil;

@Slf4j
@RestController
public class MyController {
    private final List<ModifyRequestService> modifiers;
    private final ModifyResponseService failureResponse;
    private final RestTemplate restTemplate;
    private final String service2Url;

    public MyController(List<ModifyRequestService> modifiers,
                        @Qualifier("failureResponse") ModifyResponseService failureResponse,
                        RestTemplateBuilder builder,
                        @Value("${service2.url}") String service2Url) {
        this.modifiers = modifiers;
        this.failureResponse = failureResponse;
        this.restTemplate = builder.setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(3)).build();
        this.service2Url = service2Url;
    }

    @PostMapping("/api/request")
    public ResponseEntity<Response> receive(@Valid @RequestBody Request request, BindingResult bindingResult) {
        long receivedAt = Instant.now().toEpochMilli();
        log.info("Сервис 1 получил исходный request: {}", request);
        try {
            if (bindingResult.hasErrors()) {
                String errors = bindingResult.getFieldErrors().stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .collect(Collectors.joining("; "));
                log.error("Ошибки BindingResult: {}", errors);
                throw new ValidationFailedException(errors);
            }
            if ("123".equals(request.getUid())) {
                log.error("Неподдерживаемый uid: {}", request.getUid());
                throw new UnsupportedCodeException();
            }
            for (ModifyRequestService modifier : modifiers) {
                modifier.modify(request);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Service1-Received-At", Long.toString(receivedAt));
            log.info("Пересылаем модифицированный request: {}", request);
            ResponseEntity<Response> result = restTemplate.postForEntity(service2Url,
                    new HttpEntity<>(request, headers), Response.class);
            if (result.getBody() == null) {
                log.error("Сервис 2 вернул пустое тело ответа");
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(failure(request,
                        Response.ErrorCodes.DOWNSTREAM, Response.ErrorMessages.DOWNSTREAM));
            }
            return ResponseEntity.status(result.getStatusCode()).body(result.getBody());
        } catch (ValidationFailedException ex) {
            log.error("Ошибка валидации: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(failure(request,
                    Response.ErrorCodes.VALIDATION, Response.ErrorMessages.VALIDATION));
        } catch (UnsupportedCodeException ex) {
            log.error("Неподдерживаемый код: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(failure(request,
                    Response.ErrorCodes.UNSUPPORTED, Response.ErrorMessages.UNSUPPORTED));
        } catch (RestClientException ex) {
            log.error("Не удалось передать request в Сервис 2", ex);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(failure(request,
                    Response.ErrorCodes.DOWNSTREAM, Response.ErrorMessages.DOWNSTREAM));
        }
    }

    private Response failure(Request request, Response.ErrorCodes code, Response.ErrorMessages message) {
        Response response = new Response(request.getUid(), request.getOperationUid(),
                DateTimeUtil.getCustomFormat().format(new Date()), null, code, message);
        return failureResponse.modify(response);
    }
}
