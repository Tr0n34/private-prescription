package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects;

public record PatientId(
        String value
) {

    public PatientId {
        if ( value == null || value.isBlank() ) {
            throw new IllegalArgumentException("PatientId invalide");
        }
    }

}
