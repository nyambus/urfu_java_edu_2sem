package ru.education.lab2.exception;

import ru.education.lab2.model.Request;

public class UnsupportedCodeException extends RuntimeException {
    private final Request request;

    public UnsupportedCodeException(Request request) {
        super("Идентификатор uid=123 не поддерживается");
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }
}
