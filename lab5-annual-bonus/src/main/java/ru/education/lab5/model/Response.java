package ru.education.lab5.model;

import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    /** Уникальный идентификатор сообщения из запроса. */
    private String uid;
    /** Уникальный идентификатор операции из запроса. */
    private String operationUid;
    /** Время формирования ответа сервисом. */
    private String systemTime;
    /** Результат обработки: success или failed. */
    private Code code;
    /** Код ошибки; пустой при успешной обработке. */
    private ErrorCodes errorCode;
    /** Описание ошибки; пустое при успешной обработке. */
    private ErrorMessages errorMessage;
    /** Рассчитанная годовая премия; отсутствует при ошибке. */
    private BigDecimal annualBonus;
    /** Рассчитанная квартальная премия; null для неуправленческих должностей и при ошибке. */
    private BigDecimal quarterlyBonus;

    public Response(String uid, String operationUid, String systemTime, Code code,
                    ErrorCodes errorCode, ErrorMessages errorMessage) {
        this(uid, operationUid, systemTime, code, errorCode, errorMessage, null, null);
    }

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
