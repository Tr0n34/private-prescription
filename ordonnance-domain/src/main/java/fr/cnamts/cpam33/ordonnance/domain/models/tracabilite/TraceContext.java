package fr.cnamts.cpam33.ordonnance.domain.models.tracabilite;

public record TraceContext(
        TraceIn in,
        TraceOut out
) {

    public TraceContext {
        if (in == null) throw new NullPointerException("in is null");
        if (out == null) throw new NullPointerException("out is null");
    }

}
