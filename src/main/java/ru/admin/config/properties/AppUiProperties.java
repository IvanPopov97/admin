package ru.admin.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ConfigurationProperties(prefix = "app-ui")
public class AppUiProperties {
    @NotBlank
    private String url;
    @NotBlank
    private String language;
}
