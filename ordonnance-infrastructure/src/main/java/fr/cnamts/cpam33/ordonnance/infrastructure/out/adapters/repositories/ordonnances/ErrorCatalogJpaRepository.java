package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.repositories.ordonnances;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.ErrorCatalogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ErrorCatalogJpaRepository extends JpaRepository<ErrorCatalogEntity, String> {

    Optional<ErrorCatalogEntity> findByCodeAndActiveTrue(String code);

}
