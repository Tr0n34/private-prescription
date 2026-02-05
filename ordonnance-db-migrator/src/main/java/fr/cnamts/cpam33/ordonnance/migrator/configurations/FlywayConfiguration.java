package fr.cnamts.cpam33.ordonnance.migrator.configurations;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(FlywayMigratorProperties.class)
public class FlywayConfiguration {

    @Bean(name = "ordonnanceDataSource")
    public DataSource ordonnanceDataSource(FlywayMigratorProperties props) {
        return DataSourceBuilder.create()
                .url(props.getOrdonnance().getUrl())
                .username(props.getOrdonnance().getUsername())
                .password(props.getOrdonnance().getPassword())
                .build();
    }

    @Bean(name = "traceDataSource")
    public DataSource traceDataSource(FlywayMigratorProperties props) {
        return DataSourceBuilder.create()
                .url(props.getTrace().getUrl())
                .username(props.getTrace().getUsername())
                .password(props.getTrace().getPassword())
                .build();
    }

    @Bean(name = "ordonnanceFlyway")
    public Flyway ordonnanceFlyway(@Qualifier("ordonnanceDataSource") DataSource ds, FlywayMigratorProperties props) {
        return Flyway.configure()
                .dataSource(ds)
                .locations(props.getOrdonnance().getLocations().toArray(String[]::new))
                .load();
    }

    @Bean(name = "traceFlyway")
    public Flyway traceFlyway(@Qualifier("traceDataSource") DataSource ds, FlywayMigratorProperties props) {
        return Flyway.configure()
                .dataSource(ds)
                .locations(props.getTrace().getLocations().toArray(String[]::new))
                .load();
    }

}
