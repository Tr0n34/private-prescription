package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs;

public enum Role {

    ADMINISTRATEUR("ADM"),
    PROFESSIONNEL_DE_SANTE("PDS"),
    MEDECIN("MED");

    private String code;

    Role(String code) {
        this.code = code;
    }


}
