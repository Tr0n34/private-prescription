package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identifiants.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;

import java.time.LocalDate;

public record ImportPatientCandidate(
        ExternalPatientId externalPatientId,
        Nom nom,
        Prenom prenom,
        LocalDate dateNaissance
) {

}
