package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects;

public record MedecinId(
        String value,
        Rpps rpps
) {

    public MedecinId {
        if ( value == null || value.isBlank() ) {
            throw new IllegalArgumentException("PatientId invalide");
        }
    }
}
