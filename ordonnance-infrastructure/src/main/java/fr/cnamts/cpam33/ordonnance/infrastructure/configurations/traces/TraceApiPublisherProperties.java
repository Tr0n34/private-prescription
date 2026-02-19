package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ordonnance.microservices.traces.publisher")
public record TraceApiPublisherProperties(
        String url
) {
}
