package ru.education.lab4.controller;

import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.education.lab4.model.Response;
import ru.education.lab4.service.ModifyResponseService;
import ru.education.lab4.util.DateTimeUtil;

@Slf4j
@RestControllerAdvice
public class MalformedRequestHandler {
    private final ModifyResponseService failureResponse;

    public MalformedRequestHandler(@Qualifier("failureResponse") ModifyResponseService failureResponse) {
        this.failureResponse = failureResponse;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response> malformed(HttpMessageNotReadableException ex) {
        log.error("Невозможно прочитать тело запроса", ex);
        Response response = new Response(null, null, DateTimeUtil.getCustomFormat().format(new Date()),
                null, Response.ErrorCodes.VALIDATION, Response.ErrorMessages.VALIDATION);
        return ResponseEntity.badRequest().body(failureResponse.modify(response));
    }
}
