package ru.admin.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ConfigurationProperties(prefix = "password")
@Valid
public class PasswordProperties {
    @Min(value = 1)
    private int minLength;
    @Min(value = 2)
    private int maxLength;
    @NotNull
    private PasswordCharacterMinCountProperties minCount;
    @NotNull
    private PasswordGenerationProperties generation;
    @Min(value = 0)
    private int simpleSequenceLimit;
}
