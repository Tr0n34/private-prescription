package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite;

import com.google.common.base.MoreObjects;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public record TraceContext(
    List<TraceAttribute> attributes
) {

    public TraceContext {
        Objects.requireNonNull(attributes, "attributes is null");
        attributes = Collections.unmodifiableList(attributes);
    }

}
