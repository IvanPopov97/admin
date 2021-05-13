package ru.admin.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ConfigurationProperties(prefix = "password-validation")
@Valid
public class PasswordValidationProperties {
    @Min(value = 1)
    private int minLength;
    @Min(value = 2)
    private int maxLength;
    @NotNull
    private MinCountProperties minCount;
    @Min(value = 0)
    private int simpleSequenceLimit;
}
