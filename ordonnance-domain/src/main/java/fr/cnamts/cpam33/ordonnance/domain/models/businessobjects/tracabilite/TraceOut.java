package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite;

import java.util.List;

public record TraceOut(
        TraceStatus status,
        List<TraceAttribute> traceAttributes,
        TraceFailure error
) {

    public TraceOut {
        traceAttributes = List.copyOf(traceAttributes);
    }

    public static TraceOut success(List<TraceAttribute> result) {
        return new TraceOut(TraceStatus.SUCCESS, result, null);
    }

    public static TraceOut failure(TraceFailure error, List<TraceAttribute> traceAttributes) {
        return new TraceOut(TraceStatus.FAILURE, traceAttributes, error);
    }

}