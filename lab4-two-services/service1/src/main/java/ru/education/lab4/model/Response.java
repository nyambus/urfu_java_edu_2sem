package ru.education.lab4.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private String uid;
    private String operationUid;
    private String systemTime;
    private Code code;
    private ErrorCodes errorCode;
    private ErrorMessages errorMessage;

    public enum Code {
        SUCCESS("success"), FAILED("failed");
        private final String value;
        Code(String value) { this.value = value; }
        @JsonValue public String getValue() { return value; }
    }

    public enum ErrorCodes {
        NONE(""), UNSUPPORTED("UnsupportedCodeException"), VALIDATION("ValidationException"),
        UNKNOWN("UnknownException"), DOWNSTREAM("DownstreamUnavailable");
        private final String value;
        ErrorCodes(String value) { this.value = value; }
        @JsonValue public String getValue() { return value; }
    }

    public enum ErrorMessages {
        NONE(""), UNSUPPORTED("Идентификатор uid=123 не поддерживается"),
        VALIDATION("Ошибка валидации запроса"), UNKNOWN("Произошла непредвиденная ошибка"),
        DOWNSTREAM("Сервис 2 недоступен или отклонил запрос");
        private final String value;
        ErrorMessages(String value) { this.value = value; }
        @JsonValue public String getValue() { return value; }
    }
}
