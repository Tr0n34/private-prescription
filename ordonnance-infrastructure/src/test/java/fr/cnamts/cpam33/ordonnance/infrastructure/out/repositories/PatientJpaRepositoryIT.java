package fr.cnamts.cpam33.ordonnance.infrastructure.out.repositories;

import fr.cnamts.cpam33.ordonnance.infrastructure.TestJpaConfiguration;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.repositories.ordonnances.PatientJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.PatientEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("integration")
@DataJpaTest
@Import(TestJpaConfiguration.class)
@EntityScan(basePackageClasses = PatientEntity.class)
@EnableJpaRepositories(basePackageClasses = PatientJpaRepository.class)
class PatientJpaRepositoryIT {

    @Autowired
    private PatientJpaRepository patientRepository;

    @Test
    void shouldSaveAndFindPatientByExternalId() {
        PatientEntity patient = new PatientEntity()
                .setExternalId("EXT-123")
                .setNom("Dupont")
                .setPrenom("Jean");
        patientRepository.save(patient);
        Optional<PatientEntity> result = patientRepository.findByExternalId("EXT-123");
        assertTrue(result.isPresent(), "Le patient doit exister");
        PatientEntity saved = result.get();
        assertNotNull(saved.getId(), "L'id doit être généré");
        assertEquals("Dupont", saved.getNom());
        assertEquals("Jean", saved.getPrenom());
        assertEquals("EXT-123", saved.getExternalId());
    }

}
