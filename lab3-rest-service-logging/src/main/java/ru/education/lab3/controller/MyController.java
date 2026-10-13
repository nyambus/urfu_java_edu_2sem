package ru.education.lab3.controller;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.education.lab3.model.Request;
import ru.education.lab3.model.Response;
import ru.education.lab3.service.RequestService;
import ru.education.lab3.service.ValidationService;

@RestController
@RequestMapping("/api")
@Slf4j
public class MyController {
    private final RequestService requestService;
    private final ValidationService validationService;

    public MyController(RequestService requestService, ValidationService validationService) {
        this.requestService = requestService;
        this.validationService = validationService;
    }

    @PostMapping("/request")
    public ResponseEntity<Response> process(@Valid @RequestBody Request request, BindingResult result) {
        log.info("Received request: {}", request);
        validationService.check(request, result);
        Response response = requestService.process(request);
        log.info("Returning response: {}", response);
        return ResponseEntity.ok(response);
    }
}
