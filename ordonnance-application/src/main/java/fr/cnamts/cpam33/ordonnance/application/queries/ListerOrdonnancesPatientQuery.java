package fr.cnamts.cpam33.ordonnance.application.queries;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.cqrs.Query;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.PatientId;

import java.time.LocalDate;

public record ListerOrdonnancesPatientQuery(
        PatientId patientId,
        LocalDate dateDebut,
        LocalDate dateFin,
        boolean isSigned
) implements Query {

}
