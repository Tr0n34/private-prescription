package fr.cnamts.cpam33.ordonnance.infrastructure.fixtures.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private final ThrowingService service;

    TestController(ThrowingService service) {
        this.service = service;
    }

    @GetMapping("/domain")
    void domain() {
        service.domain();
    }

    @GetMapping("/infra")
    void infra() {
        service.infra();
    }

    @GetMapping("/boom")
    void boom() {
        service.boom();
    }

}
