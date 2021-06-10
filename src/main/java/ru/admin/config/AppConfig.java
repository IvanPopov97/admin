package ru.admin.config;

import org.springframework.context.annotation.Configuration;
import ru.admin.config.properties.AppUiProperties;

import java.util.Locale;

@Configuration
public class AppConfig {

    public AppConfig(AppUiProperties appUiProperties) {
        String language = appUiProperties.getLanguage();
        Locale.setDefault(new Locale(language, language.toUpperCase()));
    }
}
