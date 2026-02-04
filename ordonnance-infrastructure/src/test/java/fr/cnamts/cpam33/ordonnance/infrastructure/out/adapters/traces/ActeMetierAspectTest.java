import fr.cnamts.cpam33.ordonnance.domain.models.events.ActeMetierEvent;
import fr.cnamts.cpam33.ordonnance.domain.models.events.TraceCommand;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.TraceAttribute;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.TraceContext;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.MedecinId;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TracePublisher;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces.ActeMetierAspect;
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
        // GIVEN
        TraceCommand command = mock(TraceCommand.class);
        when(command.medecinId()).thenReturn(new MedecinId("123456789", null));

        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        when(pjp.proceed()).thenReturn("RESULT_OK");
        when(pjp.getArgs()).thenReturn(new Object[]{command});

        ActeMetierEvent event = mock(ActeMetierEvent.class);
        when(event.value()).thenReturn(ActeMetierCode.ACT_ORD_CREER);

        // WHEN
        Object result = aspect.around(pjp, event);

        // THEN
        assertEquals("RESULT_OK", result);

        ArgumentCaptor<Trace> traceCaptor = ArgumentCaptor.forClass(Trace.class);
        verify(publisher, times(1)).publish(traceCaptor.capture());

        Trace captured = traceCaptor.getValue();
        assertEquals(ActeMetierCode.ACT_ORD_CREER.name(), captured.acteMetierId().code());
        assertEquals("123456789", captured.medecinId().id());

        // Vérifier le TraceContext
        TraceContext context = captured.context();
        assertNotNull(context);
        List<TraceAttribute> attributes = context.attributes();

        // Vérifier qu'on a bien au moins un argument
        assertTrue(attributes.stream().anyMatch(attr -> attr.name().equals("arg0")));

        // Vérifier le résultat
        assertTrue(attributes.stream()
                .anyMatch(attr -> attr.name().equals("result") && "RESULT_OK".equals(attr.value().value())));
    }
}
