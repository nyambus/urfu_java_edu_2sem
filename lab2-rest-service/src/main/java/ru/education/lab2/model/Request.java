package ru.education.lab2.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class Request {
    @NotBlank @Size(max = 32)
    private String uid;
    @NotBlank @Size(max = 32)
    private String operationUid;
    private String systemName;
    @NotBlank
    private String systemTime;
    private String source;
    @NotNull @Min(1) @Max(100000)
    private Integer communicationId;
    private Integer templateId;
    private Integer productCode;
    private Integer smsCode;
}
