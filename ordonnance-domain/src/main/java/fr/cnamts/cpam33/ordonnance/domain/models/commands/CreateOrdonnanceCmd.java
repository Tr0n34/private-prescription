package fr.cnamts.cpam33.ordonnance.domain.models.commands;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.Command;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Prescription;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins.MedecinId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.patients.PatientId;

import java.util.List;

public record CreateOrdonnanceCmd(
        PatientId patientId,
        MedecinId medecinId,
        List<Prescription> prescriptions
) implements Command {

    public CreateOrdonnanceCmd {
        if ( patientId == null || medecinId == null || prescriptions == null ) {
            throw  new IllegalArgumentException( "Patient or Medecin id or prescriptions cannot be null" );
        }
    }

}
