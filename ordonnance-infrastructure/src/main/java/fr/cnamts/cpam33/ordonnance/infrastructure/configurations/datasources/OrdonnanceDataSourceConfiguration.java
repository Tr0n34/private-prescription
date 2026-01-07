package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.datasources;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
        basePackages = "fr.cnamts.cpam33.ordonnance.infrastructure.out.repositories.ordonnances",
        entityManagerFactoryRef = "ordonnanceEntityManagerFactory",
        transactionManagerRef = "ordonnanceTransactionManager"
)
public class OrdonnanceDataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.ordonnances.hikari")
    public HikariDataSource ordonnanceDataSource() {
        return new HikariDataSource();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean ordonnanceEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(ordonnanceDataSource())
                .packages("fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances")
                .persistenceUnit("ordonnancePU")
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager ordonnanceTransactionManager(@Qualifier("ordonnanceEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

}
