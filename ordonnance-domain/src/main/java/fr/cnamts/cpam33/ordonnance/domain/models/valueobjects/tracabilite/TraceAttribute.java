package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite;

public record TraceAttribute(
        String name,
        TraceValue value
) {

    public TraceAttribute {
        if ( name == null || name.isEmpty() ) {
            throw new NullPointerException("name is null");
        }
        if ( value == null ) {
            throw new NullPointerException("value is null");
        }
    }


}
