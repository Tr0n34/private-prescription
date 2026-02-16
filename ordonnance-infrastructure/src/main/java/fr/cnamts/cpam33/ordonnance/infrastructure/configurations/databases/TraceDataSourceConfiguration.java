package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.databases;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.traces",
        entityManagerFactoryRef = "traceEntityManagerFactory",
        transactionManagerRef = "traceTransactionManager"
)
@EnableConfigurationProperties(TraceDataSourceConfiguration.TraceJpaProperties.class)
public class TraceDataSourceConfiguration {

    public static class TraceJpaProperties extends JpaUnitProperties {}

    @Bean
    @ConfigurationProperties(prefix = "trace.jpa")
    public TraceJpaProperties traceJpaProperties() {
        return new TraceJpaProperties();
    }

    @Bean("traceDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.traces.hikari")
    public HikariDataSource traceDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean("traceEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean traceEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("traceDataSource") HikariDataSource traceDataSource,
            TraceJpaProperties traceJpaProperties) {
        return builder
                .dataSource(traceDataSource)
                .packages(traceJpaProperties.getPackages())
                .persistenceUnit(traceJpaProperties.getPersistenceUnit())
                .properties(new HashMap<>(traceJpaProperties.getProperties()))
                .build();
    }

    @Bean("traceTransactionManager")
    public PlatformTransactionManager traceTransactionManager(@Qualifier("traceEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

}
