package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.datasources;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
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

import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "fr.cnamts.cpam33.thesorimed.infrastructure.out.adapters.repositories.thesorimed",
        entityManagerFactoryRef = "thesorimedEntityManagerFactory",
        transactionManagerRef = "thesorimedTransactionManager"
)
public class ThesorimedDataSourceConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.thesorimed.hikari")
    public HikariDataSource thesorimedDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean("thesorimedEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean thesorimedEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(thesorimedDataSource())
                .packages("fr.cnamts.cpam33.thesorimed.infrastructure.out.entities.thesorimed")
                .persistenceUnit("thesorimedPU")
                .properties(Map.of(
                        "hibernate.hbm2ddl.auto", "none",
                        "hibernate.connection.provider_disables_autocommit", "true"
                ))
                .build();
    }

    @Bean("thesorimedTransactionManager")
    public PlatformTransactionManager thesorimedTransactionManager(@Qualifier("thesorimedEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

}
