package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.api;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;
import java.time.Duration;

@ConfigurationProperties(prefix = "patient.api")
public record PatientApiProperties(
        URI url,
        Duration connectTimeout,
        Duration readTimeout
) {

}
