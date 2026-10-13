package ru.education.lab2.service;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.education.lab2.exception.ValidationFailedException;
import ru.education.lab2.model.Request;

@Service
public class ValidationService {
    public void check(Request request, BindingResult result) {
        if (result.hasErrors()) {
            String details = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .distinct().sorted().collect(Collectors.joining("; "));
            throw new ValidationFailedException(request, details);
        }
    }
}
