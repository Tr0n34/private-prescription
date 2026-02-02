package fr.cnamts.cpam33.ordonnance.migrator.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "migrator")
public class FlywayMigratorProperties {

    private DataSourceProperties ordonnance = new DataSourceProperties();
    private DataSourceProperties trace = new DataSourceProperties();

    public DataSourceProperties getOrdonnance() {
        return ordonnance;
    }

    public FlywayMigratorProperties setOrdonnance(DataSourceProperties ordonnance) {
        this.ordonnance = ordonnance;
        return this;
    }

    public DataSourceProperties getTrace() {
        return trace;
    }

    public FlywayMigratorProperties setTrace(DataSourceProperties trace) {
        this.trace = trace;
        return this;
    }

    public static class DataSourceProperties {

        private String url;
        private String username;
        private String password;
        private List<String> locations = new ArrayList<>();

        public String getUrl() {
            return url;
        }

        public DataSourceProperties setUrl(String url) {
            this.url = url;
            return this;
        }

        public String getUsername() {
            return username;
        }

        public DataSourceProperties setUsername(String username) {
            this.username = username;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public DataSourceProperties setPassword(String password) {
            this.password = password;
            return this;
        }

        public List<String> getLocations() {
            return locations;
        }

        public DataSourceProperties setLocations(List<String> locations) {
            this.locations = locations;
            return this;
        }

    }

}
