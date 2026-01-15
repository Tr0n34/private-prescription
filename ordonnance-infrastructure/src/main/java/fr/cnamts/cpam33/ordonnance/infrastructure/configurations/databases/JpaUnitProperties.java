package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.databases;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties
public class JpaUnitProperties {

    private String packages;
    private String persistenceUnit;
    private Map<String, String> properties = new HashMap<>();

    public String getPackages() {
        return packages;
    }

    public JpaUnitProperties setPackages(String packages) {
        this.packages = packages;
        return this;
    }

    public String getPersistenceUnit() {
        return persistenceUnit;
    }

    public JpaUnitProperties setPersistenceUnit(String persistenceUnit) {
        this.persistenceUnit = persistenceUnit;
        return this;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public JpaUnitProperties setProperties(Map<String, String> properties) {
        this.properties = properties;
        return this;
    }

}
