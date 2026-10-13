package ru.education.lab3.controller;

import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.education.lab3.exception.UnsupportedCodeException;
import ru.education.lab3.exception.ValidationFailedException;
import ru.education.lab3.model.ErrorCodes;
import ru.education.lab3.model.ErrorMessages;
import ru.education.lab3.model.Request;
import ru.education.lab3.model.Response;
import ru.education.lab3.service.ModifyResponseService;
import ru.education.lab3.util.DateTimeUtil;

@RestControllerAdvice
@Slf4j
public class RequestExceptionHandler {
    private final ModifyResponseService failureResponseService;

    public RequestExceptionHandler(@Qualifier("failureResponseService") ModifyResponseService failureResponseService) {
        this.failureResponseService = failureResponseService;
    }

    @ExceptionHandler(ValidationFailedException.class)
    public ResponseEntity<Response> validation(ValidationFailedException ex) {
        return error(HttpStatus.BAD_REQUEST, ex.getRequest(), ErrorCodes.VALIDATION, ErrorMessages.VALIDATION, ex.getMessage());
    }

    @ExceptionHandler(UnsupportedCodeException.class)
    public ResponseEntity<Response> unsupported(UnsupportedCodeException ex) {
        return error(HttpStatus.UNPROCESSABLE_ENTITY, ex.getRequest(), ErrorCodes.UNSUPPORTED_CODE, ErrorMessages.UNSUPPORTED_CODE, null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response> unreadable(HttpMessageNotReadableException ex) {
        log.error("Cannot parse JSON request: {}", ex.getMostSpecificCause().getMessage());
        return error(HttpStatus.BAD_REQUEST, null, ErrorCodes.VALIDATION, ErrorMessages.VALIDATION,
                "Ожидается корректный JSON; systemName должен быть ERP, CRM или WMS");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> unknown(Exception ex) {
        log.error("Unexpected request processing exception", ex);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, null, ErrorCodes.UNKNOWN, ErrorMessages.UNKNOWN, null);
    }

    private ResponseEntity<Response> error(HttpStatus status, Request request, ErrorCodes code,
                                           ErrorMessages message, String details) {
        Response response = new Response(request == null ? "" : request.getUid(),
                request == null ? "" : request.getOperationUid(),
                DateTimeUtil.getCustomFormat().format(new Date()), null, code, message, details);
        log.info("Created error response: {}", response);
        return ResponseEntity.status(status).body(failureResponseService.modify(response));
    }
}
