package ru.education.lab3.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCodes {
    NONE(""), UNSUPPORTED_CODE("UnsupportedCodeException"),
    VALIDATION("ValidationException"), UNKNOWN("UnknownException");

    private final String value;

    ErrorCodes(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
