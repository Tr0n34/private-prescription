package fr.cnamts.cpam33.ordonnance.infrastructure;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Définition des tests spécifiques pour utilister TestContainer et le datasource Trace
 */
@TestConfiguration
public class TraceJpaTestConfig {

    @Bean(name = "tracesDataSource")
    DataSource tracesDataSource(
            @Qualifier("tracesJdbcUrl") String jdbcUrl,
            @Qualifier("tracesUsername") String username,
            @Qualifier("tracesPassword") String password) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(jdbcUrl);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setMaximumPoolSize(4);
        ds.setPoolName("traces-ti-pool");
        return ds;
    }

    @Bean(name = "tracesJdbcUrl")
    String tracesJdbcUrl(Environment env) {
        return env.getRequiredProperty("spring.datasource.traces.hikari.jdbc-url");
    }

    @Bean(name = "tracesUsername")
    String tracesUsername(Environment env) {
        return env.getRequiredProperty("spring.datasource.traces.hikari.username");
    }

    @Bean(name = "tracesPassword")
    String tracesPassword(Environment env) {
        return env.getRequiredProperty("spring.datasource.traces.hikari.password");
    }

    @Bean(name = "traceEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean traceEntityManagerFactory(@Qualifier("tracesDataSource") DataSource ds) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(ds);
        emf.setPackagesToScan("fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces");
        emf.setPersistenceUnitName("tracePU");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "create");
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        emf.setJpaPropertyMap(props);
        return emf;
    }

    @Bean(name = "traceTransactionManager")
    PlatformTransactionManager traceTransactionManager(@Qualifier("traceEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

}

