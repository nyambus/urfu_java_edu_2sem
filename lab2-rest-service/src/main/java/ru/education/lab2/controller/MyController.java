package ru.education.lab2.controller;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.education.lab2.model.Request;
import ru.education.lab2.model.Response;
import ru.education.lab2.service.RequestService;
import ru.education.lab2.service.ValidationService;

@RestController
@RequestMapping("/api")
public class MyController {
    private final RequestService requestService;
    private final ValidationService validationService;

    public MyController(RequestService requestService, ValidationService validationService) {
        this.requestService = requestService;
        this.validationService = validationService;
    }

    @PostMapping("/request")
    public ResponseEntity<Response> process(@Valid @RequestBody Request request, BindingResult result) {
        validationService.check(request, result);
        return ResponseEntity.ok(requestService.process(request));
    }
}
