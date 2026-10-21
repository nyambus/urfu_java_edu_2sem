package ru.education.lab5.controller;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.education.lab5.model.Request;
import ru.education.lab5.model.Response;
import ru.education.lab5.service.RequestProcessingService;

@RestController
public class MyController {
    private final RequestProcessingService processingService;

    public MyController(RequestProcessingService processingService) {
        this.processingService = processingService;
    }

    @PostMapping("/api/request")
    public ResponseEntity<Response> receive(@Valid @RequestBody Request request, BindingResult bindingResult) {
        return processingService.process(request, bindingResult);
    }
}
