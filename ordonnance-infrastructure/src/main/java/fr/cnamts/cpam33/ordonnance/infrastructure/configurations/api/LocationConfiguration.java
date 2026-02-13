package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.api;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LocationProperties.class)
public class LocationConfiguration {
}
