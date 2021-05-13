package ru.admin.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = "account-activation")
public class AccountActivationProperties {
    private String emailTemplate;
    private String queueName;
    private Duration confirmationTime;
}
