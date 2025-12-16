package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects;

public record Nom (
        String value
){

    public Nom {
        if ( value == null || value.isBlank() ) {

        }
    }

}
