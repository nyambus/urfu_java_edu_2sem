package ru.education.lab3.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.education.lab3.model.Codes;
import ru.education.lab3.model.ErrorCodes;
import ru.education.lab3.model.ErrorMessages;
import ru.education.lab3.model.Response;

@Service("successResponseService")
@Slf4j
public class SuccessModifyResponseService implements ModifyResponseService {
    @Override
    public Response modify(Response response) {
        response.setCode(Codes.SUCCESS);
        log.info("Response code set to success: {}", response);
        response.setErrorCode(ErrorCodes.NONE);
        log.info("Response error code cleared: {}", response);
        response.setErrorMessage(ErrorMessages.NONE);
        log.info("Response error message cleared: {}", response);
        return response;
    }
}
