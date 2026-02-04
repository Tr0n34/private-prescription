package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.ordonnances;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientJpaRepository extends JpaRepository<PatientEntity, Long> {

    Optional<PatientEntity> findByExternalId(String externalPatientId);

    Optional<PatientEntity> findByPatientId(String patientId);



}
