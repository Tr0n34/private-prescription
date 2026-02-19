package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.providers;

import com.github.f4b6a3.uuid.UuidCreator;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TraceNumGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidTraceIdNumGenerator implements TraceNumGenerator {

    @Override
    public String generate() {
        return newTraceId().toString();
    }

    public UUID newTraceId() {
        return UuidCreator.getTimeOrderedEpoch();
    }

}
