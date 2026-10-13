package ru.education.lab4.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.education.lab4.model.Request;

public interface ModifyRequestService {
    void modify(Request request);
}

@Slf4j
@Component
@Order(1)
class NormalizeSourceService implements ModifyRequestService {
    @Override
    public void modify(Request request) {
        if (request.getSource() != null) {
            request.setSource(request.getSource().trim());
            log.info("Нормализован источник: {}", request);
        }
    }
}

@Slf4j
@Component
@Order(2)
class MarkSourceService implements ModifyRequestService {
    @Override
    public void modify(Request request) {
        String original = request.getSource();
        request.setSource("service1" + (original == null || original.isEmpty() ? "" : ":" + original));
        log.info("Источник изменён Сервисом 1: {}", request);
    }
}
