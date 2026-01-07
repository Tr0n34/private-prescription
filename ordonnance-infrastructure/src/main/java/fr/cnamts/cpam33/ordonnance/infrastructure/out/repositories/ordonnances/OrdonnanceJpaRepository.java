package fr.cnamts.cpam33.ordonnance.infrastructure.out.repositories.ordonnances;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.OrdonnanceEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.OrdonnanceIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdonnanceJpaRepository extends JpaRepository<OrdonnanceEntity, OrdonnanceIdEntity> {

}
