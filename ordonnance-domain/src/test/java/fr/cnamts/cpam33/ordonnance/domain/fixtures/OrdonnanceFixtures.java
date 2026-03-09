package fr.cnamts.cpam33.ordonnance.domain.fixtures;

import fr.cnamts.cpam33.ordonnance.domain.fixtures.stubs.OrdonnanceNumGeneratorStub;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.Ordonnance;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.OrdonnanceId;

import java.util.List;

public class OrdonnanceFixtures {

    public static final String NUMERO = "123456";

    public static Ordonnance ordonnanceValide(){
        return ordonnanceValide(NUMERO);
    }

    public static Ordonnance ordonnanceValide(String numero) {
        Ordonnance ordonnance = ordonnanceNonValidee(numero);
        ordonnance.validate();
        return ordonnance;
    }

    public static Ordonnance ordonnanceNonValidee(String numero) {
        return Ordonnance.of(
                new OrdonnanceId(numero),
                PatientFixtures.patientValide(),
                MedecinFixtures.medecinValide(),
                List.of(PrescriptionFixtures.onePrescription())
        );
    }

    public static Ordonnance ordonnanceSignee() {
        return ordonnanceValide(NUMERO).sign();
    }

    public static Ordonnance ordonnanceValideV2(String numero) {
        Ordonnance ordonnance = ordonnanceValide(numero);
        ordonnance.incrementVersion();
        return ordonnance;
    }

    public static OrdonnanceId ordonnanceId() {
        return new OrdonnanceId(new OrdonnanceNumGeneratorStub().generate());
    }


}
