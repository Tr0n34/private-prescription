package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.databases;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
        basePackages = "fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.repositories.ordonnances",
        entityManagerFactoryRef = "ordonnanceEntityManagerFactory",
        transactionManagerRef = "ordonnanceTransactionManager"
)
public class OrdonnanceDataSourceConfiguration {

    @ConfigurationProperties(prefix = "ordonnance.jpa")
    public static class OrdonnanceJpaProperties extends JpaUnitProperties {}

    @Bean("ordonnanceDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.ordonnances.hikari")
    public HikariDataSource ordonnanceDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean("ordonnanceEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean ordonnanceEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            OrdonnanceJpaProperties ordonnanceJpaProperties) {
        return builder
                .dataSource(ordonnanceDataSource())
                .packages(ordonnanceJpaProperties.getPackages())
                .persistenceUnit(ordonnanceJpaProperties.getPersistenceUnit())
                .build();
    }

    @Bean("ordonnanceTransactionManager")
    @Primary
    public PlatformTransactionManager ordonnanceTransactionManager(@Qualifier("ordonnanceEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

}
