package ru.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.admin.config.properties.AccountActivationProperties;
import ru.admin.config.properties.AppUiProperties;
import ru.admin.config.properties.BlockingCallDetectionProperties;
import ru.admin.config.properties.PasswordProperties;

@SpringBootApplication
@EnableConfigurationProperties({PasswordProperties.class, BlockingCallDetectionProperties.class,
        AccountActivationProperties.class, AppUiProperties.class})
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
