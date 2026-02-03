package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.databases;

import com.zaxxer.hikari.HikariDataSource;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.thesorimed.ThesorimedRoutineExecutor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class ThesorimedDataSourceConfiguration {


    public static class ThesorimedHibernateProperties extends JpaUnitProperties {}

    @Bean
    @ConfigurationProperties(prefix = "thesorimed.jpa")
    public ThesorimedHibernateProperties thesorimedHibernateProperties() {
        return new ThesorimedHibernateProperties();
    }

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
    public ThesorimedRoutineExecutor thesorimedRoutineExecutor(@Qualifier("thesorimedJdbcTemplate") JdbcTemplate jdbcTemplate) {
        return new ThesorimedRoutineExecutor(jdbcTemplate);
    }

    @Bean("thesorimedTransactionManager")
    public PlatformTransactionManager thesorimedJdbcTransactionManager(@Qualifier("thesorimedDataSource") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }

}
