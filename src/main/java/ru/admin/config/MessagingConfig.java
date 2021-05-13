package ru.admin.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.admin.config.properties.AccountActivationProperties;

@Configuration
public class MessagingConfig {

    private final AccountActivationProperties accountActivationProperties;

    public MessagingConfig(AccountActivationProperties accountActivationProperties) {
        this.accountActivationProperties = accountActivationProperties;
    }

    // очередь пользователей, которые ждут письмо для активации аккаунта
    @Bean
    public Queue accountActivationQueue() {
        return new Queue(accountActivationProperties.getQueueName());
    }
}
