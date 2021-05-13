package ru.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.admin.config.properties.AccountActivationProperties;
import ru.admin.config.properties.BlockingCallDetectionProperties;
import ru.admin.config.properties.PasswordValidationProperties;

@SpringBootApplication
@EnableConfigurationProperties({PasswordValidationProperties.class, BlockingCallDetectionProperties.class,
		AccountActivationProperties.class})
public class AdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}

}
