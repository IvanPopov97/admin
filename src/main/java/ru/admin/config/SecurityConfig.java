package ru.admin.config;

import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.PasswordValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterExchange;
import reactor.core.publisher.Mono;
import ru.admin.config.properties.PasswordProperties;
import ru.admin.enitity.UserRole;
import ru.admin.service.UserDetailsService;
import ru.admin.utils.PasswordToolBuilder;

import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers;

@EnableWebFluxSecurity
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
                .pathMatchers("/user/confirm", "/login", "/logout", "/user", "/actuator/health").permitAll()
                .pathMatchers("/doc/**", "/webjars/swagger-ui/**", "/v3/api-docs/**", "/actuator/**", "/admin/**").hasRole(UserRole.ADMIN.name())
                .anyExchange().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .requiresAuthenticationMatcher(pathMatchers(HttpMethod.POST, "/login"))
                .authenticationSuccessHandler(this::onAuthenticationSuccess)
                .authenticationFailureHandler(this::onAuthenticationError)
                .and()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .build();
        // @formatter:on
    }

    @Bean
    ReactiveAuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
    }

    @Bean
    PasswordProperties passwordProperties() {
        return new PasswordProperties();
    }

    @Bean
    PasswordToolBuilder passwordToolBuilder() {
        var properties = passwordProperties();
        var minCount = properties.getMinCount();
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
        var generation = passwordProperties().getGeneration();
        return passwordToolBuilder().buildGenerator(generation.getMinLength(), generation.getMaxLength());
    }

    private Mono<Void> onAuthenticationSuccess(WebFilterExchange exchange, Authentication authentication) {
        exchange.getExchange().getResponse().setStatusCode(HttpStatus.OK);
        return Mono.empty();
    }

    private Mono<Void> onAuthenticationError(WebFilterExchange exchange, AuthenticationException e) {
        exchange.getExchange().getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return Mono.empty();
    }
}
