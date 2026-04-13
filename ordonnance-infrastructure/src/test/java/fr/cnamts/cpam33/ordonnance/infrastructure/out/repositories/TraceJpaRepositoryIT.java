package fr.cnamts.cpam33.ordonnance.infrastructure.out.repositories;

import fr.cnamts.cpam33.ordonnance.infrastructure.contexts.TraceJpaTestConfig;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.traces.TraceOutboxJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.TraceEntity;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

@ActiveProfiles("integration")
@Testcontainers
@DataJpaTest
@Import({TraceJpaTestConfig.class})
@EntityScan(basePackageClasses = TraceEntity.class)
@EnableJpaRepositories(
        basePackages = "fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.traces",
        entityManagerFactoryRef = "traceEntityManagerFactory",
        transactionManagerRef = "traceTransactionManager"
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TraceJpaRepositoryIT {

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:17.6-alpine3.22")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void datasourceProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.traces.hikari.jdbc-url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.traces.hikari.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.datasource.traces.hikari.username", POSTGRES::getUsername);
        registry.add("spring.datasource.traces.hikari.password", POSTGRES::getPassword);
        registry.add("trace.jpa.properties.hibernate.hbm2ddl.auto", () -> "create");
        registry.add("spring.sql.init.mode", () -> "never");
    }

    @Autowired TraceOutboxJpaRepository repo;
    @Autowired JdbcTemplate jdbc;
    @Autowired @Qualifier("traceTransactionManager") PlatformTransactionManager txManager;

    @AfterEach
    void cleanup() {
        jdbc.execute("TRUNCATE TABLE trace_outbox RESTART IDENTITY CASCADE");
    }

}
