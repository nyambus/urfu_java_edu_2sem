package ru.education.lab4.controller;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.education.lab4.exception.UnsupportedCodeException;
import ru.education.lab4.exception.ValidationFailedException;
import ru.education.lab4.model.Request;
import ru.education.lab4.model.Response;
import ru.education.lab4.service.ModifyResponseService;
import ru.education.lab4.util.DateTimeUtil;

@Slf4j
@RestController
public class MyController {
    private final ModifyResponseService successResponse;
    private final ModifyResponseService failureResponse;

    public MyController(@Qualifier("successResponse") ModifyResponseService successResponse,
                        @Qualifier("failureResponse") ModifyResponseService failureResponse) {
        this.successResponse = successResponse;
        this.failureResponse = failureResponse;
    }

    @PostMapping("/api/request")
    public ResponseEntity<Response> receive(@Valid @RequestBody Request request,
                                            BindingResult bindingResult,
                                            @RequestHeader(value = "X-Service1-Received-At", required = false) Long receivedAt) {
        log.info("Сервис 2 получил модифицированный request: {}", request);
        if (receivedAt != null) {
            long elapsedMs = Instant.now().toEpochMilli() - receivedAt;
            log.info("Время от получения Сервисом 1 до получения Сервисом 2: {} мс", elapsedMs);
        }
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
            Response response = new Response(request.getUid(), request.getOperationUid(),
                    DateTimeUtil.getCustomFormat().format(new Date()), null, null, null);
            return ResponseEntity.ok(successResponse.modify(response));
        } catch (ValidationFailedException ex) {
            log.error("Ошибка валидации: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(failure(request,
                    Response.ErrorCodes.VALIDATION, Response.ErrorMessages.VALIDATION));
        } catch (UnsupportedCodeException ex) {
            log.error("Неподдерживаемый код: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(failure(request,
                    Response.ErrorCodes.UNSUPPORTED, Response.ErrorMessages.UNSUPPORTED));
        }
    }

    private Response failure(Request request, Response.ErrorCodes code, Response.ErrorMessages message) {
        Response response = new Response(request.getUid(), request.getOperationUid(),
                DateTimeUtil.getCustomFormat().format(new Date()), null, code, message);
        return failureResponse.modify(response);
    }
}
