package ru.education.lab3.service;

import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.education.lab3.exception.UnsupportedCodeException;
import ru.education.lab3.model.Request;
import ru.education.lab3.model.Response;
import ru.education.lab3.util.DateTimeUtil;

@Service
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final ModifyResponseService responseService;

    public RequestServiceImpl(@Qualifier("successResponseService") ModifyResponseService responseService) {
        this.responseService = responseService;
    }

    @Override
    public Response process(Request request) {
        if ("123".equals(request.getUid())) {
            log.error("Throwing UnsupportedCodeException: uid=123, operationUid={}", request.getOperationUid());
            throw new UnsupportedCodeException(request);
        }
        Response response = new Response(request.getUid(), request.getOperationUid(),
                DateTimeUtil.getCustomFormat().format(new Date()), null, null, null, null);
        log.info("Created response from request: {}", response);
        return responseService.modify(response);
    }
}
