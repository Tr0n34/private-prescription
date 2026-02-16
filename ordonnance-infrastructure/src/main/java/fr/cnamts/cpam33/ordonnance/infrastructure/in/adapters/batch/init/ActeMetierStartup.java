package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.batch.init;

import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.batch.ActeMetierLoader;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
public class ActeMetierStartup {

    private final ActeMetierLoader acteMetierLoader;

    public ActeMetierStartup(ActeMetierLoader acteMetierLoader) {
        this.acteMetierLoader = acteMetierLoader;
    }

    @PostConstruct
    public void init() {
        acteMetierLoader.reload();
    }

}
