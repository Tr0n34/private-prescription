package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite;

public record TraceValue(
        Object value
) {

    public TraceValue {
        if ( value == null ) {
            throw new NullPointerException("value is null");
        }
    }

}
