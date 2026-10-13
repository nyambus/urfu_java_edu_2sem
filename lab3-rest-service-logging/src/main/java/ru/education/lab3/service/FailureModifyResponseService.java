package ru.education.lab3.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.education.lab3.model.Codes;
import ru.education.lab3.model.Response;

@Service("failureResponseService")
@Slf4j
public class FailureModifyResponseService implements ModifyResponseService {
    @Override
    public Response modify(Response response) {
        response.setCode(Codes.FAILED);
        log.info("Response code set to failed: {}", response);
        return response;
    }
}
