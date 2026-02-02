package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.flyway;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties({
        OrdonnanceFlywayProperties.class,
        TraceFlywayProperties.class,
})
public class FlywayConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "flyway.ordonnance", name = "enabled", havingValue = "true", matchIfMissing = true)
    public Flyway ordonnanceFlyway(@Qualifier("ordonnanceDataSource") DataSource ordonnanceDataSource,
                                   OrdonnanceFlywayProperties properties) {
        return Flyway.configure()
                .dataSource(ordonnanceDataSource)
                .locations(properties.getLocations().toArray(String[]::new))
                .load();
    }

    @Bean
    @ConditionalOnProperty(prefix = "flyway.trace", name = "enabled", havingValue = "true", matchIfMissing = true)
    public Flyway traceFlyway(@Qualifier("traceDataSource") DataSource traceDataSource,
                              TraceFlywayProperties properties) {
        return Flyway.configure()
                .dataSource(traceDataSource)
                .locations(properties.getLocations().toArray(String[]::new))
                .load();
    }

}
