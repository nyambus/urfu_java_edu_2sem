package ru.education.lab2.controller;

import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.education.lab2.exception.UnsupportedCodeException;
import ru.education.lab2.exception.ValidationFailedException;
import ru.education.lab2.model.Request;
import ru.education.lab2.model.Response;

@RestControllerAdvice
public class RequestExceptionHandler {
    @ExceptionHandler(ValidationFailedException.class)
    public ResponseEntity<Response> validation(ValidationFailedException ex) {
        return error(HttpStatus.BAD_REQUEST, ex.getRequest(), "ValidationException", ex.getMessage());
    }

    @ExceptionHandler(UnsupportedCodeException.class)
    public ResponseEntity<Response> unsupported(UnsupportedCodeException ex) {
        return error(HttpStatus.UNPROCESSABLE_ENTITY, ex.getRequest(), "UnsupportedCodeException", ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response> unreadable(HttpMessageNotReadableException ex) {
        return error(HttpStatus.BAD_REQUEST, null, "ValidationException", "Ожидается корректный JSON с допустимыми типами полей");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> unknown(Exception ex) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, null, "UnknownException", "Произошла непредвиденная ошибка");
    }

    private ResponseEntity<Response> error(HttpStatus status, Request request, String code, String message) {
        return ResponseEntity.status(status).body(new Response(
                request == null ? "" : request.getUid(),
                request == null ? "" : request.getOperationUid(),
                Instant.now().toString(), "failed", code, message));
    }
}
