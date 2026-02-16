package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces;

import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.kernel.events.ActeMetierEvent;
import fr.cnamts.cpam33.ordonnance.domain.kernel.events.TraceCommand;
import fr.cnamts.cpam33.ordonnance.domain.kernel.ids.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.TraceAttribute;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.TraceContext;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TracePublisher;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActeMetierAspectTest {

    private TracePublisher publisher;
    private Clock clock;
    private ActeMetierAspect aspect;

    @BeforeEach
    void setUp() {
        publisher = mock(TracePublisher.class);
        clock = Clock.fixed(Instant.parse("2026-01-10T10:00:00Z"), ZoneId.of("UTC"));
        aspect = new ActeMetierAspect(publisher, clock);
    }

    @Test
    void should_publish_trace_with_context() throws Throwable {
        TraceCommand command = mock(TraceCommand.class);
        when(command.utilisateurId()).thenReturn(new UtilisateurId("123456789"));
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        when(pjp.proceed()).thenReturn("RESULT_OK");
        when(pjp.getArgs()).thenReturn(new Object[]{command});
        ActeMetierEvent event = mock(ActeMetierEvent.class);
        when(event.value()).thenReturn(ActeMetierCode.ORD_CREER);
        Object result = aspect.around(pjp, event);

        assertEquals("RESULT_OK", result);
        ArgumentCaptor<Trace> traceCaptor = ArgumentCaptor.forClass(Trace.class);
        verify(publisher, times(1)).publish(traceCaptor.capture());
        Trace captured = traceCaptor.getValue();
        assertEquals(ActeMetierCode.ORD_CREER.name(), captured.acteMetierId().code());
        assertEquals("123456789", captured.utilisateurId().numero());
        TraceContext context = captured.context();
        assertNotNull(context);
        List<TraceAttribute> attributes = context.attributes();
        assertTrue(attributes.stream().anyMatch(attr -> attr.name().contains("TraceCommand")));
        assertTrue(attributes.stream()
                .anyMatch(attr -> attr.name().equals("result") && "RESULT_OK".equals(attr.value().value())));
    }
}
