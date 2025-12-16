package fr.cnamts.cpam33.ordonnance.domain.models.aggregates;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Posologie;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Prescription;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.IdentiteMedecin;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.IdentitePatient;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.OrdonnanceId;

import java.util.List;

public class Ordonnance {

    private OrdonnanceId ordonnanceId;
    private final IdentitePatient patient;
    private final IdentiteMedecin medecin;
    private final List<Prescription> prescriptions;

    private Ordonnance(
            OrdonnanceId ordonnanceId,
            IdentitePatient patient,
            IdentiteMedecin medecin,
            List<Prescription> prescriptions
    ) {
        this.ordonnanceId = ordonnanceId;
        this.patient = patient;
        this.medecin = medecin;
        this.prescriptions = List.copyOf(prescriptions);
    }

    public static Ordonnance of(
            OrdonnanceId ordonanceId,
            IdentitePatient patient,
            IdentiteMedecin medecin,
            List<Prescription> prescriptions) {
        return new Ordonnance(new OrdonnanceId("numero", medecin.rpps(), ordonanceId.), patient, medecin, prescriptions);
    }


}
