package ru.admin.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Controller
@Hidden
public class TemplateController {
    @GetMapping("login")
    public Mono<String> getLoginView(ServerWebExchange exchange, Model model) {
        Mono<CsrfToken> csrfToken = exchange.getAttributeOrDefault(CsrfToken.class.getName(), Mono.empty());
        return csrfToken.doOnNext(token -> model.addAttribute(token.getParameterName(), token)).thenReturn("login");
    }
}
