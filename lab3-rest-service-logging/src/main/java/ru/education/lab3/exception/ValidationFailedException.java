package ru.education.lab3.exception;

import ru.education.lab3.model.Request;

public class ValidationFailedException extends RuntimeException {
    private final Request request;

    public ValidationFailedException(Request request, String details) {
        super(details);
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }
}
