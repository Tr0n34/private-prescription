package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.ordonnances;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.MedecinEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedecinJpaRepository extends JpaRepository<MedecinEntity, Long> {

    Optional<MedecinEntity> findByExternalId(String externalId);

}

