package fr.cnamts.cpam33.ordonnance.infrastructure.out.repositories;

import fr.cnamts.cpam33.ordonnance.infrastructure.contexts.TraceJpaTestConfig;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.traces.TraceOutboxJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.TraceEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.TraceOutboxEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
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
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

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

    private void insertOutboxRow(long id, int attempts, LocalDateTime createdAt) {
        jdbc.update("""
        INSERT INTO trace_outbox (id, attempts, created_at, payload_json)
        VALUES (?, ?, ?, CAST(? AS jsonb))
        """,
                id, attempts, createdAt, "{}"
        );
    }

    private TransactionTemplate txTemplate() {
        return new TransactionTemplate(txManager);
    }

    @Test
    public void should_reserve_batch_ordered_and_filtered_and_count_and_oldest_age() {
        jdbc.execute("DELETE FROM trace_outbox");
        LocalDateTime now = LocalDateTime.now();
        insertOutboxRow(1L, 0, now.minusSeconds(120));
        insertOutboxRow(2L, 2, now.minusSeconds(60));
        insertOutboxRow(3L, 3, now.minusSeconds(300));
        long count = repo.outboxCount();
        Double oldest = repo.oldestAgeSeconds();
        List<TraceOutboxEntity> batch = repo.reserveBatch(10, 3);

        assertThat(count).isEqualTo(3);
        assertThat(oldest).isNotNull();
        assertThat(oldest).isGreaterThan(50.0);
        assertThat(oldest).isLessThan(600.0);
        assertThat(batch).extracting(TraceOutboxEntity::id).containsExactly(1L, 2L);
    }

    @Test
    public void should_apply_limit_in_reserve_batch() {
        jdbc.execute("DELETE FROM trace_outbox");
        LocalDateTime now = LocalDateTime.now();
        insertOutboxRow(10L, 0, now.minusSeconds(300));
        insertOutboxRow(11L, 0, now.minusSeconds(200));
        insertOutboxRow(12L, 0, now.minusSeconds(100));
        List<TraceOutboxEntity> batch = repo.reserveBatch(2, 99);
        assertThat(batch).extracting(TraceOutboxEntity::id)
                .containsExactly(10L, 11L);
    }

    @Test
    public void should_skip_locked_rows_between_two_concurrent_transactions() throws Exception {
        setupOutboxCommitted(); // commit visible par les autres threads

        TransactionTemplate tx = txTemplate();

        CountDownLatch tx1HasLocked = new CountDownLatch(1);
        CountDownLatch allowTx1ToCommit = new CountDownLatch(1);

        AtomicReference<List<Long>> tx1Ids = new AtomicReference<>();
        AtomicReference<List<Long>> tx2Ids = new AtomicReference<>();

        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<?> f1 = pool.submit(() -> {
            tx.execute(status -> {
                List<Long> ids = repo.reserveBatch(2, 99).stream()
                        .map(TraceOutboxEntity::id)
                        .toList();
                tx1Ids.set(ids);
                tx1HasLocked.countDown();
                try {
                    allowTx1ToCommit.await(3, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return null;
            });
        });

        Future<?> f2 = pool.submit(() -> {
            try {
                tx1HasLocked.await(3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            tx.execute(status -> {
                List<Long> ids = repo.reserveBatch(2, 99).stream()
                        .map(TraceOutboxEntity::id)
                        .toList();
                tx2Ids.set(ids);
                return null;
            });
        });

        f2.get(5, TimeUnit.SECONDS);
        allowTx1ToCommit.countDown();
        f1.get(5, TimeUnit.SECONDS);

        pool.shutdownNow();

        assertThat(tx1Ids.get()).containsExactly(100L, 101L);
        assertThat(tx2Ids.get()).isNotNull();
        assertThat(tx2Ids.get()).isEmpty();
    }


    private void setupOutboxCommitted() {
        TransactionTemplate t = txTemplate();
        t.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        t.execute(status -> {
            jdbc.execute("DELETE FROM trace_outbox");
            LocalDateTime now = LocalDateTime.now();
            insertOutboxRow(100L, 0, now.minusSeconds(30));
            insertOutboxRow(101L, 0, now.minusSeconds(20));
            return null;
        });
    }

}
