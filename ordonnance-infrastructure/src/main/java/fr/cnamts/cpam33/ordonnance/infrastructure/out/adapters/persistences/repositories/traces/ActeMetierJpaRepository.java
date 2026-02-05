package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.traces;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.ActeMetierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActeMetierJpaRepository extends JpaRepository<ActeMetierEntity, Long> {

    Optional<ActeMetierEntity> findByCode(String code);

}
