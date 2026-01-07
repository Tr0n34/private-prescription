package fr.cnamts.cpam33.ordonnance.domain.fixtures;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Signature;

public class SignatureFixtures {

    public static final String VALUE = "123456789AREFEFKOJ554";

    public static Signature signature() {
        return new Signature(VALUE);
    }

}
