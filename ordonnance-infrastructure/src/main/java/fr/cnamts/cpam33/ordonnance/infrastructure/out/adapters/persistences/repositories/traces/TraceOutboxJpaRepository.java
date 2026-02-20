package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.traces;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces.TraceOutboxStatus;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.TraceOutboxEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface TraceOutboxJpaRepository extends JpaRepository<TraceOutboxEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select t
        from TraceOutboxEntity t
        where t.status = :status
          and (t.nextRetryAt is null or t.nextRetryAt <= :now)
        order by t.createdAt asc
        """)
    List<TraceOutboxEntity> findRetryableLocked(
            @Param("status") TraceOutboxStatus status,
            @Param("now") OffsetDateTime now,
            org.springframework.data.domain.Pageable pageable
    );

    default List<TraceOutboxEntity> findRetryable(OffsetDateTime now, int limit) {
        return findRetryableLocked(TraceOutboxStatus.RETRYING, now, org.springframework.data.domain.PageRequest.of(0, limit));
    }

}
