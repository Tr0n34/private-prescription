package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces;


import com.github.f4b6a3.uuid.UuidCreator;
import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.kernel.events.ActeMetierEvent;
import fr.cnamts.cpam33.ordonnance.domain.kernel.ids.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.kernel.traces.Traceable;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.*;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.ActeMetierRepository;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TraceNumGenerator;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TracePublisher;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.ActeMetierAspect;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.providers.traces.TraceContextFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


class ActeMetierAspectTest {

    private ActeMetierRepository acteMetierRepository;
    private TracePublisher publisher;
    private TraceContextFactory traceContextFactory;
    private ProceedingJoinPoint pjp;
    private TraceNumGenerator traceNumGenerator;

    private Clock clock;
    private ActeMetierAspect aspect;

    @BeforeEach
    void setUp() {
        publisher = mock(TracePublisher.class);
        traceContextFactory = mock(TraceContextFactory.class);
        acteMetierRepository = mock(ActeMetierRepository.class);
        when(acteMetierRepository.findFonctionByActeMetierId(any()))
                .thenReturn(new Fonction(new FonctionId("FONC"), "test"));
        pjp = mock(ProceedingJoinPoint.class);
        traceNumGenerator = new TraceNumGenerator() {
            @Override
            public String generate() {
                return UuidCreator.getTimeOrderedEpoch().toString();
            }
        };
        clock = Clock.fixed(Instant.parse("2026-01-10T09:00:00Z"), ZoneId.of("Europe/Paris"));
        aspect = new ActeMetierAspect(acteMetierRepository, publisher, traceContextFactory, traceNumGenerator, clock);
    }

    @Test
    void should_publish_trace_when_traceable_argument_present_and_method_returns() throws Throwable {
        Traceable traceable = mock(Traceable.class);
        when(traceable.utilisateurId()).thenReturn(new UtilisateurId("123456789"));
        ActeMetierEvent event = mock(ActeMetierEvent.class);
        when(event.value()).thenReturn(ActeMetierCode.ORD_CREER);
        Object result = "OK";
        when(pjp.getArgs()).thenReturn(new Object[]{ traceable, "other" });
        when(pjp.proceed()).thenReturn(result);
        TraceContext traceContext = stubTraceContext();
        when(traceContextFactory.build(eq(pjp), eq(traceable), eq(result), isNull())).thenReturn(traceContext);
        ArgumentCaptor<Trace> traceCaptor = ArgumentCaptor.forClass(Trace.class);
        Object returned = aspect.around(pjp, event);
        assertSame(result, returned);
        verify(publisher, times(1)).publish(traceCaptor.capture());
        Trace published = traceCaptor.getValue();
        assertEquals(ActeMetierCode.ORD_CREER.name(), published.acteMetierId().code());
        assertEquals("123456789", published.utilisateurId().numero());
        assertEquals("String", published.boundedContext());
        verify(traceContextFactory).build(eq(pjp), eq(traceable), eq(result), isNull());
    }

    @Test
    void should_not_publish_when_no_traceable_argument_present() throws Throwable {
        ActeMetierEvent event = mock(ActeMetierEvent.class);
        when(event.value()).thenReturn(ActeMetierCode.ORD_CREER);
        when(pjp.getArgs()).thenReturn(new Object[]{ "a", 123 });
        when(pjp.proceed()).thenReturn("OK");
        aspect.around(pjp, event);
        verifyNoInteractions(traceContextFactory);
        verifyNoInteractions(publisher);
    }

    @Test
    void should_publish_trace_even_when_exception_thrown() throws Throwable {
        Traceable traceable = mock(Traceable.class);
        when(traceable.utilisateurId()).thenReturn(new UtilisateurId("123456789"));
        ActeMetierEvent event = mock(ActeMetierEvent.class);
        when(event.value()).thenReturn(ActeMetierCode.ORD_CREER);
        RuntimeException boom = new RuntimeException("boom");
        when(pjp.getArgs()).thenReturn(new Object[]{ traceable });
        when(pjp.proceed()).thenThrow(boom);
        TraceContext traceContext = stubTraceContext();
        when(traceContextFactory.build(eq(pjp), eq(traceable), isNull(), eq(boom))).thenReturn(traceContext);
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> aspect.around(pjp, event));
        assertSame(boom, thrown);
        verify(publisher, times(1)).publish(any(Trace.class));
        verify(traceContextFactory).build(eq(pjp), eq(traceable), isNull(), eq(boom));
    }

    @Test
    void should_swallow_exception_from_publisher_publish() throws Throwable {
        Traceable traceable = mock(Traceable.class);
        when(traceable.utilisateurId()).thenReturn(new UtilisateurId("123456789"));

        ActeMetierEvent event = mock(ActeMetierEvent.class);
        when(event.value()).thenReturn(ActeMetierCode.ORD_CREER);

        when(pjp.getArgs()).thenReturn(new Object[]{ traceable });
        when(pjp.proceed()).thenReturn("OK");

        TraceContext traceContext = stubTraceContext();
        when(traceContextFactory.build(eq(pjp), eq(traceable), eq("OK"), isNull()))
                .thenReturn(traceContext);

        doThrow(new RuntimeException("broker down")).when(publisher).publish(any(Trace.class));

        assertDoesNotThrow(() -> aspect.around(pjp, event));
        verify(publisher, times(1)).publish(any(Trace.class));
    }

    @Test
    void should_not_publish_when_args_is_null() throws Throwable {
        ActeMetierEvent event = mock(ActeMetierEvent.class);
        when(event.value()).thenReturn(ActeMetierCode.ORD_CREER);

        when(pjp.getArgs()).thenReturn(null);
        when(pjp.proceed()).thenReturn("OK");

        aspect.around(pjp, event);

        verifyNoInteractions(traceContextFactory);
        verifyNoInteractions(publisher);
    }

    private static TraceContext stubTraceContext() {
        TraceIn in = mock(TraceIn.class);
        TraceOut out = mock(TraceOut.class);
        when(out.status()).thenReturn(null);
        when(out.traceAttributes()).thenReturn(java.util.List.of());

        TraceContext ctx = mock(TraceContext.class);
        when(ctx.in()).thenReturn(in);
        when(ctx.out()).thenReturn(out);
        return ctx;
    }

}
