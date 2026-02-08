package fr.cnamts.cpam33.ordonnance.infrastructure.actuator;

import fr.cnamts.cpam33.ordonnance.infrastructure.actuators.endpoints.TraceMetricsEndpoint;
import fr.cnamts.cpam33.ordonnance.infrastructure.actuators.metrics.TraceMetrics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraceMetricsEndpointTest {

    @Test
    void should_never_return_null_map() {
        TraceMetrics metrics = mock(TraceMetrics.class);
        TraceMetricsEndpoint endpoint = new TraceMetricsEndpoint(metrics);
        Map<String, Object> result = endpoint.traceMetrics();
        assertThat(result).isNotNull();
    }

    @Test
    void should_expose_all_trace_metrics() {
        TraceMetrics metrics = mock(TraceMetrics.class);
        when(metrics.offeredCount()).thenReturn(10L);
        when(metrics.enqueuedCount()).thenReturn(8L);
        when(metrics.enqueueTimeoutCount()).thenReturn(1L);
        when(metrics.outboxedCount()).thenReturn(1L);
        when(metrics.outboxWriteFailedCount()).thenReturn(0L);
        TraceMetricsEndpoint endpoint = new TraceMetricsEndpoint(metrics);
        Map<String, Object> result = endpoint.traceMetrics();

        assertThat(result)
                .hasSize(5)
                .containsEntry("offered", 10L)
                .containsEntry("enqueued", 8L)
                .containsEntry("enqueueTimeout", 1L)
                .containsEntry("outboxed", 1L)
                .containsEntry("outboxWriteFailed", 0L);
        verify(metrics).offeredCount();
        verify(metrics).enqueuedCount();
        verify(metrics).enqueueTimeoutCount();
        verify(metrics).outboxedCount();
        verify(metrics).outboxWriteFailedCount();
        verifyNoMoreInteractions(metrics);
    }

    @Test
    void should_preserve_metrics_order() {
        TraceMetrics metrics = mock(TraceMetrics.class);
        TraceMetricsEndpoint endpoint = new TraceMetricsEndpoint(metrics);
        Map<String, Object> result = endpoint.traceMetrics();
        Iterator<String> keys = result.keySet().iterator();

        assertThat(keys.next()).isEqualTo("offered");
        assertThat(keys.next()).isEqualTo("enqueued");
        assertThat(keys.next()).isEqualTo("enqueueTimeout");
        assertThat(keys.next()).isEqualTo("outboxed");
        assertThat(keys.next()).isEqualTo("outboxWriteFailed");
        assertThat(keys).isExhausted();
    }

    @Test
    void should_expose_contract_required_keys_and_value_types_for_monitoring() {
        TraceMetrics metrics = mock(TraceMetrics.class);
        when(metrics.offeredCount()).thenReturn(0L);
        when(metrics.enqueuedCount()).thenReturn(0L);
        when(metrics.enqueueTimeoutCount()).thenReturn(0L);
        when(metrics.outboxedCount()).thenReturn(0L);
        when(metrics.outboxWriteFailedCount()).thenReturn(0L);
        TraceMetricsEndpoint endpoint = new TraceMetricsEndpoint(metrics);
        Map<String, Object> result = endpoint.traceMetrics();

        assertThat(result.keySet()).containsExactly(
                "offered",
                "enqueued",
                "enqueueTimeout",
                "outboxed",
                "outboxWriteFailed"
        );
        assertThat(result.get("offered")).isInstanceOf(Long.class);
        assertThat(result.get("enqueued")).isInstanceOf(Long.class);
        assertThat(result.get("enqueueTimeout")).isInstanceOf(Long.class);
        assertThat(result.get("outboxed")).isInstanceOf(Long.class);
        assertThat(result.get("outboxWriteFailed")).isInstanceOf(Long.class);
        assertThat((Long) result.get("offered")).isGreaterThanOrEqualTo(0L);
        assertThat((Long) result.get("enqueued")).isGreaterThanOrEqualTo(0L);
        assertThat((Long) result.get("enqueueTimeout")).isGreaterThanOrEqualTo(0L);
        assertThat((Long) result.get("outboxed")).isGreaterThanOrEqualTo(0L);
        assertThat((Long) result.get("outboxWriteFailed")).isGreaterThanOrEqualTo(0L);
    }


}
