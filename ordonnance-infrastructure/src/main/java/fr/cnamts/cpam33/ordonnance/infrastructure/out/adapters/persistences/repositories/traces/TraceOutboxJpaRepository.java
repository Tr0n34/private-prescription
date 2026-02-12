package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.traces;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.TraceOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TraceOutboxJpaRepository extends JpaRepository<TraceOutboxEntity, Long> {

    @Query(value = """
        SELECT * from trace_outbox
        WHERE attempts < :maxAttempts
        ORDER BY created_at ASC
        FOR UPDATE skip locked
        LIMIT :limit
        """, nativeQuery = true)
    List<TraceOutboxEntity> reserveBatch(@Param("limit") int limit, @Param("maxAttempts") int maxAttempts);

    @Query(value = """
        SELECT count(*) 
        FROM trace_outbox
        """, nativeQuery = true)
    long outboxCount();

    @Query(value = """
        SELECT extract(epoch from (now() - min(created_at)))
        FROM trace_outbox
    """, nativeQuery = true)
    Double oldestAgeSeconds();

}
