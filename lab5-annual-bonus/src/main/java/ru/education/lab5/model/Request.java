package ru.education.lab5.model;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class Request {
    /** Уникальный идентификатор сообщения, до 32 символов. */
    @NotBlank @Size(max = 32)
    private String uid;
    /** Уникальный идентификатор операции, до 32 символов. */
    @NotBlank @Size(max = 32)
    private String operationUid;
    /** Имя системы отправителя. */
    private Systems systemName;
    /** Время создания сообщения в формате ISO-8601 (с датой). */
    @NotBlank
    private String systemTime;
    /** Наименование ресурса; перед пересылкой дополняется именем сервиса. */
    private String source;
    /** Уникальный идентификатор коммуникации (1–100000). */
    @NotNull @Min(1) @Max(100000)
    private Integer communicationId;
    /** Уникальный идентификатор шаблона. */
    private Integer templateId;
    /** Код продукта. */
    private Integer productCode;
    /** СМС-код. */
    private Integer smsCode;
    /** Должность сотрудника, определяющая коэффициент премии. */
    @NotNull
    private Positions position;
    /** Оклад сотрудника, неотрицательная денежная величина. */
    @NotNull @DecimalMin("0.0")
    private BigDecimal salary;
    /** Безразмерный неотрицательный множитель премии. */
    @NotNull @DecimalMin("0.0")
    private BigDecimal bonus;
    /** Положительное число рабочих дней для расчета. */
    @NotNull @Min(1)
    private Integer workDays;

    public enum Systems { ERP, CRM, WMS }
}
