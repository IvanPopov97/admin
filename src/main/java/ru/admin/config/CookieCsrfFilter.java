package ru.admin.config;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class CookieCsrfFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain next) {
        Mono<CsrfToken> csrfToken = exchange.getAttribute(CsrfToken.class.getName());
        if (csrfToken == null)
            return Mono.empty();
        return csrfToken.doOnSuccess(token -> {}).then(next.filter(exchange));
    }
}
