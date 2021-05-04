package ru.admin.config;

import org.passay.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import ru.admin.service.UserDetailsService;

import java.util.List;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .anyExchange().authenticated()
                .and()
                .httpBasic()
                .and()
                .build();
    }

    @Bean
    ReactiveAuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
    }

    @Bean
    PasswordValidator passwordValidator(@Value("${password.validation.length.min}") int minLength,
            @Value("${password.validation.length.max}") int maxLength,
            @Value("${password.validation.min-count.uppercase}") int upperCaseLimit,
            @Value("${password.validation.min-count.lowercase}") int lowerCaseLimit,
            @Value("${password.validation.min-count.digit}") int digitLimit,
            @Value("${password.validation.min-count.special}") int specialLimit,
            @Value("${password.validation.simple-sequence-limit}") int simpleSequenceLimit) {

        return new PasswordValidator (
                List.of (
                        new LengthRule(minLength, maxLength),
                        new CharacterRule(EnglishCharacterData.UpperCase, upperCaseLimit),
                        new CharacterRule(EnglishCharacterData.LowerCase, lowerCaseLimit),
                        new CharacterRule(EnglishCharacterData.Digit, digitLimit),
                        new CharacterRule(EnglishCharacterData.Special, specialLimit),
                        new IllegalSequenceRule(EnglishSequenceData.Alphabetical, simpleSequenceLimit, false),
                        new IllegalSequenceRule(EnglishSequenceData.Numerical, simpleSequenceLimit, false),
                        new IllegalSequenceRule(EnglishSequenceData.USQwerty, simpleSequenceLimit, false),
                        new RepeatCharactersRule(simpleSequenceLimit),
                        new WhitespaceRule()
                )
        );
    }
}
