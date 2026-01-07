package fr.cnamts.cpam33.ordonnance.infrastructure.out.repositories.traces;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.TraceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraceJpaRepository extends JpaRepository<TraceEntity, Long> {

}
