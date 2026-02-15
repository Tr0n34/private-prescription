package fr.cnamts.cpam33.ordonnance.domain.models.queries;

import fr.cnamts.cpam33.ordonnance.domain.kernel.cqrs.Query;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.PatientId;

import java.time.LocalDate;

public record ListerOrdonnancesPatientQuery(
        PatientId patientId,
        LocalDate dateDebut,
        LocalDate dateFin,
        boolean isSigned
) implements Query {

}
