package fr.cnamts.cpam33.ordonnance.domain.fixtures;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;

import java.time.LocalDate;
import java.time.Month;

public class PatientFixtures {

    public static Patient patientValide() {
        return Patient.of(
                new PatientId("1234567891234"),
                new ExternalPatientId("123"),
                new Nom("Dupont"),
                new Prenom("Jean"),
                LocalDate.of(1980, Month.SEPTEMBER, 5)
                );
    }

    public static Patient patientValideWithIdAndCes(String numero, String externalId) {
        return Patient.of(
                new PatientId(numero),
                new ExternalPatientId(externalId),
                new Nom("Dupont"),
                new Prenom("Jean"),
                LocalDate.of(1980, Month.SEPTEMBER, 5));
    }

    public static Patient patientValideWitExternalId(ExternalPatientId externalId) {
        return Patient.of(
                new PatientId("123456798"),
                externalId,
                new Nom("Dupont"),
                new Prenom("Jean"),
                LocalDate.of(1980, Month.SEPTEMBER, 5));
    }

}
