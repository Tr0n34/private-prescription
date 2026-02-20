package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.traces;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.TraceOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraceOutboxJpaRepository extends JpaRepository<TraceOutboxEntity, Long> {

}
