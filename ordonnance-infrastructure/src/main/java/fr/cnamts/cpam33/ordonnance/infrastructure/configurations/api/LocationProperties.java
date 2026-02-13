package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.api;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("location.created")
public record LocationProperties(
        String pathId
) {

}
