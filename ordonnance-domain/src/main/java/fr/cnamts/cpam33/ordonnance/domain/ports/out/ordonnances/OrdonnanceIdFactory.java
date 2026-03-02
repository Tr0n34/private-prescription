package fr.cnamts.cpam33.ordonnance.domain.ports.out.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.kernel.identifiants.OrdonnanceId;

public class OrdonnanceIdFactory {

    private final OrdonnanceNumGenerator numGenerator;

    public OrdonnanceIdFactory(OrdonnanceNumGenerator numGenerator) {
        this.numGenerator = numGenerator;
    }

    public static OrdonnanceIdFactory withGenerator(OrdonnanceNumGenerator numGenerator) {
        return new OrdonnanceIdFactory(numGenerator);
    }

    public OrdonnanceId create() {
        return new OrdonnanceId(numGenerator.generate());
    }


}
