package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers;

import fr.cnamts.cpam33.ordonnance.domain.fixtures.PatientFixtures;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.PatientId;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.PatientEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances.NomPrenomMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances.PatientEntityMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances.PatientIdMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PatientEntityMapperTest {

    private PatientEntityMapper mapper;
    private NomPrenomMapper nomPrenomMapper;
    private PatientIdMapper patientIdMapper;

    @BeforeEach
    void setUp() {
        mapper = new PatientEntityMapper() {
            @Override
            public PatientEntity toEntity(Patient patient) {
                PatientEntity entity = new PatientEntity();
                entity.setExternalId(patient.patientId().externalId());
                entity.setNom(patient.nom().value());
                entity.setPrenom(patient.prenom().value());
                return entity;
            }

            @Override
            public Patient toDomain(PatientEntity entity) {
                return new Patient(
                        new PatientId(entity.getExternalId()),
                        new Nom(entity.getNom()),
                        new Prenom(entity.getPrenom()),
                        entity.getDateNaissance()
                );
            }
        };
    }

    @Test
    void testToEntity() {
        Patient patient = PatientFixtures.patientValide();
        PatientEntity entity = mapper.toEntity(patient);
        assertThat(entity).isNotNull();
        assertThat(entity.getExternalId()).isEqualTo(patient.patientId().externalId());
        assertThat(entity.getNom()).isEqualTo(patient.nom().value());
        assertThat(entity.getPrenom()).isEqualTo(patient.prenom().value());
    }

    @Test
    void testToDomain() {
        LocalDate dateNaissance = LocalDate.of(1990, 1, 1);
        PatientEntity entity = new PatientEntity();
        entity.setExternalId("1234567890123");
        entity.setNom("Dupont");
        entity.setPrenom("Jean");
        entity.setDateNaissance(dateNaissance);
        Patient patient = mapper.toDomain(entity);
        assertThat(patient).isNotNull();
        assertThat(patient.patientId().externalId()).isEqualTo("1234567890123");
        assertThat(patient.nom().value()).isEqualTo("Dupont");
        assertThat(patient.prenom().value()).isEqualTo("Jean");
        assertThat(dateNaissance).isEqualTo(patient.dateNaissance());
    }

}