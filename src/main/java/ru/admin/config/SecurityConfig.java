package ru.admin.config;

import org.passay.*;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import ru.admin.config.properties.MinCountProperties;
import ru.admin.config.properties.PasswordValidationProperties;
import ru.admin.enitity.UserRole;
import ru.admin.service.UserDetailsService;
import ru.admin.utils.PasswordValidatorBuilder;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    SecurityWebFilterChain configure(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/users/confirm", "/users/signup", "/users/login", "/actuator/health").permitAll()
                .pathMatchers("/doc/**", "/webjars/swagger-ui/**", "/v3/api-docs/**", "/actuator/**").hasRole(UserRole.ADMIN.name())
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
    PasswordValidationProperties passwordValidationProperties () {
        return new PasswordValidationProperties();
    }

    @Bean
    PasswordValidator passwordValidator() {
        PasswordValidationProperties properties = passwordValidationProperties();
        MinCountProperties minCount = properties.getMinCount();
        int simpleSequenceLimit = properties.getSimpleSequenceLimit() + 1;
        return new PasswordValidatorBuilder()
                .length(properties.getMinLength(), properties.getMaxLength())
                .characterRule(EnglishCharacterData.UpperCase, minCount.getUpperCase())
                .characterRule(EnglishCharacterData.LowerCase, minCount.getLowerCase())
                .characterRule(EnglishCharacterData.Digit, minCount.getDigit())
                .characterRule(EnglishCharacterData.Special, minCount.getSpecial())
                .sequenceRule(EnglishSequenceData.Alphabetical, simpleSequenceLimit)
                .sequenceRule(EnglishSequenceData.Numerical, simpleSequenceLimit)
                .sequenceRule(EnglishSequenceData.USQwerty, simpleSequenceLimit)
                .repeatCharacterRule(simpleSequenceLimit)
                .whitespaceRule()
                .build();
    }
}
