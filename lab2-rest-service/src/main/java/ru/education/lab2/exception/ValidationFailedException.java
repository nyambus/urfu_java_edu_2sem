package ru.education.lab2.exception;

import ru.education.lab2.model.Request;

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
