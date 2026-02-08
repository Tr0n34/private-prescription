package fr.cnamts.cpam33.ordonnance.infrastructure.actuators.endpoints;

import fr.cnamts.cpam33.ordonnance.infrastructure.actuators.metrics.TraceMetrics;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@WebEndpoint(id = "traceMetrics")
public class TraceMetricsEndpoint {

    private final TraceMetrics metrics;

    public TraceMetricsEndpoint(TraceMetrics metrics) {
        this.metrics = metrics;
    }

    @ReadOperation
    public Map<String, Object> traceMetrics() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("offered", metrics.offeredCount());
        out.put("enqueued", metrics.enqueuedCount());
        out.put("enqueueTimeout", metrics.enqueueTimeoutCount());
        out.put("outboxed", metrics.outboxedCount());
        out.put("outboxWriteFailed", metrics.outboxWriteFailedCount());
        return out;
    }

}
