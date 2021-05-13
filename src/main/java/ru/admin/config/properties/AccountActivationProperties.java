package ru.admin.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = "account-activation")
public class AccountActivationProperties {
    @NotBlank
    private String emailTemplate;
    @NotBlank
    private String queueName;
    @NotNull
    private Duration confirmationTime;
    @NotNull
    private String link;
}
