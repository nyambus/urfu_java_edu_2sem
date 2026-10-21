package ru.education.lab5.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.education.lab5.exception.UnsupportedCodeException;
import ru.education.lab5.exception.ValidationFailedException;
import ru.education.lab5.model.Request;
import ru.education.lab5.model.Response;
import ru.education.lab5.util.DateTimeUtil;

@Slf4j
@Service
public class RequestProcessingService {
    private final List<ModifyRequestService> modifiers;
    private final ModifyResponseService failureResponse;
    private final AnnualBonusService bonusService;
    private final RestTemplate restTemplate;
    private final String service2Url;

    public RequestProcessingService(List<ModifyRequestService> modifiers,
                                    @Qualifier("failureResponse") ModifyResponseService failureResponse,
                                    AnnualBonusService bonusService, RestTemplateBuilder builder,
                                    @Value("${service2.url}") String service2Url) {
        this.modifiers = modifiers;
        this.failureResponse = failureResponse;
        this.bonusService = bonusService;
        this.restTemplate = builder.setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(3)).build();
        this.service2Url = service2Url;
    }

    public ResponseEntity<Response> process(Request request, BindingResult bindingResult) {
        long receivedAt = Instant.now().toEpochMilli();
        log.info("Сервис 1 получил исходный request: {}", request);
        try {
            if (bindingResult.hasErrors()) {
                String errors = bindingResult.getFieldErrors().stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .collect(Collectors.joining("; "));
                throw new ValidationFailedException(errors);
            }
            if ("123".equals(request.getUid())) {
                throw new UnsupportedCodeException();
            }
            LocalDate date;
            try {
                date = LocalDate.parse(request.getSystemTime(), DateTimeFormatter.ISO_DATE_TIME);
            } catch (DateTimeParseException ex) {
                throw new ValidationFailedException("systemTime must be an ISO-8601 date-time");
            }
            for (ModifyRequestService modifier : modifiers) {
                modifier.modify(request);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Service1-Received-At", Long.toString(receivedAt));
            log.info("Пересылаем модифицированный request: {}", request);
            ResponseEntity<Response> result = restTemplate.postForEntity(service2Url,
                    new HttpEntity<>(request, headers), Response.class);
            Response response = result.getBody();
            if (response == null) {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(failure(request,
                        Response.ErrorCodes.DOWNSTREAM, Response.ErrorMessages.DOWNSTREAM));
            }
            if (result.getStatusCode().is2xxSuccessful() && response.getCode() == Response.Code.SUCCESS) {
                response.setAnnualBonus(bonusService.calculate(request.getSalary(), request.getBonus(),
                        request.getWorkDays(), request.getPosition(), date));
                if (request.getPosition().isManager()) {
                    response.setQuarterlyBonus(bonusService.calculateQuarterly(request.getSalary(),
                            request.getBonus(), request.getWorkDays(), request.getPosition(), date));
                }
            }
            return ResponseEntity.status(result.getStatusCode()).body(response);
        } catch (ValidationFailedException | IllegalArgumentException ex) {
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
