package fr.cnamts.cpam33.ordonnance.domain.kernel.traces;

import java.util.Map;

public interface TraceDataExtractor<T extends Traceable> {

    Class<T> supports();

    Map<String, String> extract(T command);

}