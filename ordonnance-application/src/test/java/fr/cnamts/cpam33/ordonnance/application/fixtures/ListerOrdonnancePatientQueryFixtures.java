package fr.cnamts.cpam33.ordonnance.application.fixtures;

import fr.cnamts.cpam33.ordonnance.domain.fixtures.PatientFixtures;
import fr.cnamts.cpam33.ordonnance.domain.models.queries.ListerOrdonnancesPatientQuery;

import java.time.LocalDate;

public class ListerOrdonnancePatientQueryFixtures {

    public static ListerOrdonnancesPatientQuery valideNow() {
        return new ListerOrdonnancesPatientQuery(
                PatientFixtures.patientValide().patientId(),
                LocalDate.now(),
                LocalDate.now(),
                false
        );
    }

}
