package ru.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class TemplateController {
    @GetMapping("login")
    public Mono<String> getLoginView() {
        return Mono.just("login");
    }
}
