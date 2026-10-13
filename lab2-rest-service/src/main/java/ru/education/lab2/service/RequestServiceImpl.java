package ru.education.lab2.service;

import java.time.Instant;
import org.springframework.stereotype.Service;
import ru.education.lab2.exception.UnsupportedCodeException;
import ru.education.lab2.model.Request;
import ru.education.lab2.model.Response;

@Service
public class RequestServiceImpl implements RequestService {
    @Override
    public Response process(Request request) {
        if ("123".equals(request.getUid())) {
            throw new UnsupportedCodeException(request);
        }
        return new Response(request.getUid(), request.getOperationUid(), Instant.now().toString(),
                "success", "", "");
    }
}
