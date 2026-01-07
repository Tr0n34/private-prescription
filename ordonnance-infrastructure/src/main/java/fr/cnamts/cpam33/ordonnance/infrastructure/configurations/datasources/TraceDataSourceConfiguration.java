package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.datasources;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "fr.cnamts.cpam33.ordonnance.infrastructure.out.repositories.traces",
        entityManagerFactoryRef = "traceEntityManagerFactory",
        transactionManagerRef = "traceTransactionManager"
)
public class TraceDataSourceConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.traces.hikari")
    public HikariDataSource traceDataSource() {
        return new HikariDataSource();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean traceEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("traceDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces")
                .persistenceUnit("tracePU")
                .build();
    }

    @Bean
    public PlatformTransactionManager traceTransactionManager(@Qualifier("traceEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

}
