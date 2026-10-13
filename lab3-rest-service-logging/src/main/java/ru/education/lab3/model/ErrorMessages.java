package ru.education.lab3.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorMessages {
    NONE(""), UNSUPPORTED_CODE("Идентификатор uid=123 не поддерживается"),
    VALIDATION("Ошибка валидации запроса"), UNKNOWN("Произошла непредвиденная ошибка");

    private final String value;

    ErrorMessages(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
