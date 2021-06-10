package ru.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.util.Locale;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "ru.admin.config.properties")
public class AdminApplication {

    public static void main(String[] args) {
        Locale.setDefault(new Locale("ru", "RU"));
        SpringApplication.run(AdminApplication.class, args);
    }

}
