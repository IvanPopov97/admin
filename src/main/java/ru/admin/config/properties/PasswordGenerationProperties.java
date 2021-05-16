package ru.admin.config.properties;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Valid
public class PasswordGenerationProperties {
    @Min(value = 0)
    private int minLength;
    @Min(value = 0)
    private int maxLength;
    @NotBlank
    private String queueName;
    @NotBlank
    private String emailTemplate;
}
