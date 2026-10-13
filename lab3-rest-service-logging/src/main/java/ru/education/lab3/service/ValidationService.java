package ru.education.lab3.service;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.education.lab3.exception.ValidationFailedException;
import ru.education.lab3.model.Request;

@Service
@Slf4j
public class ValidationService {
    public void check(Request request, BindingResult result) {
        if (result.hasErrors()) {
            String details = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .distinct().sorted().collect(Collectors.joining("; "));
            log.error("BindingResult validation failure: {}", details);
            log.error("Throwing ValidationFailedException for request {}", request);
            throw new ValidationFailedException(request, details);
        }
    }
}
