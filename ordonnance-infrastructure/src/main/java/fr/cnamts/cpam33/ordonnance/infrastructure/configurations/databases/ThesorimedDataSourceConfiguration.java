package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.databases;

import com.zaxxer.hikari.HikariDataSource;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.repositories.thesorimed.ThesorimedRoutineExecutor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ThesorimedDataSourceConfiguration {

    @ConfigurationProperties(prefix = "thesorimed.jpa")
    public static class ThesorimedHibernateProperties extends JpaUnitProperties {}

    @Bean("thesorimedDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.thesorimed.hikari")
    public HikariDataSource thesorimedDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean("thesorimedJdbcTemplate")
    public JdbcTemplate thesorimedJdbcTemplate(@Qualifier("thesorimedDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean("thesorimedRoutineExecutor")
    public ThesorimedRoutineExecutor thesorimedRoutineExecutor(
            @Qualifier("thesorimedJdbcTemplate") JdbcTemplate jdbcTemplate) {
        return new ThesorimedRoutineExecutor(jdbcTemplate);
    }

    @Bean("thesorimedTransactionManager")
    public PlatformTransactionManager thesorimedJdbcTransactionManager(
            @Qualifier("thesorimedDataSource") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }

}
