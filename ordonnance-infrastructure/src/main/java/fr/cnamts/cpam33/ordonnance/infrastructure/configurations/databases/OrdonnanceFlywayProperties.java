package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.databases;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "flyway.ordonnance")
public class OrdonnanceFlywayProperties {

    private List<String> locations = List.of();

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

}
