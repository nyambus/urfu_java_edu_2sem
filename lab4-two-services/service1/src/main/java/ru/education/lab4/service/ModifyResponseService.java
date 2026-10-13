package ru.education.lab4.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.education.lab4.model.Response;

public interface ModifyResponseService {
    Response modify(Response response);
}

@Slf4j
@Component("successResponse")
class SuccessResponseService implements ModifyResponseService {
    @Override
    public Response modify(Response response) {
        response.setCode(Response.Code.SUCCESS);
        response.setErrorCode(Response.ErrorCodes.NONE);
        response.setErrorMessage(Response.ErrorMessages.NONE);
        log.info("Успешный ответ: {}", response);
        return response;
    }
}

@Slf4j
@Component("failureResponse")
class FailureResponseService implements ModifyResponseService {
    @Override
    public Response modify(Response response) {
        response.setCode(Response.Code.FAILED);
        log.info("Ответ с ошибкой: {}", response);
        return response;
    }
}
