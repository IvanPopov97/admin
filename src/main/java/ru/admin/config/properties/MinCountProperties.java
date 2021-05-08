package ru.admin.config.properties;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Getter
@Setter
@Valid
public class MinCountProperties {
    @Min(value = 0)
    private int upperCase;
    @Min(value = 0)
    private int lowerCase;
    @Min(value = 0)
    private int digit;
    @Min(value = 0)
    private int special;
}
