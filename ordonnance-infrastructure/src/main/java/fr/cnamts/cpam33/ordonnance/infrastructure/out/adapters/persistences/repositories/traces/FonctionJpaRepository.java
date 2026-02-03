package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.traces;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.FonctionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FonctionJpaRepository extends JpaRepository<FonctionEntity, String> {

    Optional<FonctionEntity> findByCode(String code);

}
