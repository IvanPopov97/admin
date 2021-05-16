package ru.admin.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.admin.config.properties.AccountActivationProperties;
import ru.admin.config.properties.PasswordProperties;

@Configuration
public class MessagingConfig {

    private final AccountActivationProperties accountActivationProperties;
    private final PasswordProperties passwordProperties;

    public MessagingConfig(AccountActivationProperties accountActivationProperties,
            PasswordProperties passwordProperties) {
        this.accountActivationProperties = accountActivationProperties;
        this.passwordProperties = passwordProperties;
    }

    // очередь пользователей, которые ждут письмо для активации аккаунта
    @Bean
    public Queue accountActivationQueue() {
        return new Queue(accountActivationProperties.getQueueName());
    }

    // очередь пользователей, которые ждут письмо со сгенерированным паролем
    @Bean
    public Queue passwordGenerationQueue() {
        return new Queue(passwordProperties.getGeneration().getQueueName());
    }
}
