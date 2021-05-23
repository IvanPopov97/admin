package ru.admin.config;

import org.passay.*;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import ru.admin.config.properties.MinCountProperties;
import ru.admin.config.properties.PasswordGenerationProperties;
import ru.admin.config.properties.PasswordProperties;
import ru.admin.enitity.UserRole;
import ru.admin.service.UserDetailsService;
import ru.admin.utils.PasswordToolBuilder;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    SecurityWebFilterChain configure(ServerHttpSecurity http) {
        // @formatter:off
        return http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/users/confirm", "/login", "/logout", "/users/signup", "/actuator/health").permitAll()
                .pathMatchers("/doc/**", "/webjars/swagger-ui/**", "/v3/api-docs/**", "/actuator/**", "/users/**").hasRole(UserRole.ADMIN.name())
                .anyExchange().authenticated()
                .and()
                .httpBasic()
                .and()
                .formLogin().loginPage("/login")
                .and()
                .build();
        // @formatter:on
    }

    @Bean
    ReactiveAuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
    }

    @Bean
    PasswordProperties passwordProperties () {
        return new PasswordProperties();
    }

    @Bean
    PasswordToolBuilder passwordToolBuilder() {
        PasswordProperties properties = passwordProperties();
        MinCountProperties minCount = properties.getMinCount();
        int simpleSequenceLimit = properties.getSimpleSequenceLimit() + 1;
        // @formatter:off
        return new PasswordToolBuilder()
                .length(properties.getMinLength(), properties.getMaxLength())
                .characterRule(EnglishCharacterData.UpperCase, minCount.getUpperCase())
                .characterRule(EnglishCharacterData.LowerCase, minCount.getLowerCase())
                .characterRule(EnglishCharacterData.Digit, minCount.getDigit())
                .characterRule(EnglishCharacterData.Special, minCount.getSpecial())
                .sequenceRule(EnglishSequenceData.Alphabetical, simpleSequenceLimit)
                .sequenceRule(EnglishSequenceData.Numerical, simpleSequenceLimit)
                .sequenceRule(EnglishSequenceData.USQwerty, simpleSequenceLimit)
                .repeatCharacterRule(simpleSequenceLimit)
                .whitespaceRule();
        // @formatter:on
    }

    @Bean
    PasswordValidator passwordValidator() {
        return passwordToolBuilder().buildValidator();
    }

    @Bean
    PasswordGeneratorTemplate passwordGeneratorTemplate() {
        PasswordGenerationProperties generation = passwordProperties().getGeneration();
        return passwordToolBuilder().buildGenerator(generation.getMinLength(), generation.getMaxLength());
    }
}
