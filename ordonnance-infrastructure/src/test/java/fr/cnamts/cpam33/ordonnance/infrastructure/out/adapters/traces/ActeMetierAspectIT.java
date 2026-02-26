package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces;

import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.kernel.ids.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.kernel.traces.Traceable;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.TraceIn;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.TraceOut;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.ActeMetierRepository;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TracePublisher;
import fr.cnamts.cpam33.ordonnance.infrastructure.contexts.TestAspectConfiguration;
import fr.cnamts.cpam33.ordonnance.infrastructure.fixtures.stubs.DummyActeMetierService;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.ActeMetierAspect;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.providers.traces.TraceContextFactory;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.providers.traces.TraceInBuilder;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.providers.traces.TraceOutBuilder;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest(
        classes = {
                ActeMetierAspect.class,
                TraceContextFactory.class,
                DummyActeMetierService.class
        },
        properties = {
                // évite surprises sur proxy interface vs class
                "spring.aop.proxy-target-class=true"
        }
)
@Import(TestAspectConfiguration.class)
@ActiveProfiles("integration")
class ActeMetierAspectIT {


    @Autowired DummyActeMetierService service;
    @Autowired TracePublisher publisher;
    @Autowired
    TraceInBuilder traceInBuilder;
    @Autowired
    TraceOutBuilder traceOutBuilder;

    @AfterEach
    void resetMocks() {
        clearInvocations(publisher);
    }
/*
    @Test
    void should_publish_trace_when_method_called_with_traceable() {
        Traceable cmd = mock(Traceable.class);
        when(cmd.utilisateurId()).thenReturn(new UtilisateurId("123456789"));
        TraceIn in = mock(TraceIn.class);
        TraceOut out = mock(TraceOut.class);
        when(traceInBuilder.build(any(), eq(cmd))).thenReturn(in);
        when(traceOutBuilder.build(eq("RESULT_OK"), isNull())).thenReturn(out);
        String result = service.ok(cmd);

        assertEquals("RESULT_OK", result);

        ArgumentCaptor<Trace> captor = ArgumentCaptor.forClass(Trace.class);
        verify(publisher).publish(captor.capture());

        Trace trace = captor.getValue();
        assertNotNull(trace);
        assertEquals(ActeMetierCode.ORD_CREER.name(), trace.acteMetierId().code());
        assertEquals("123456789", trace.utilisateurId().numero());
        assertNotNull(trace.context());
    }

    @Test
    void should_publish_trace_even_when_exception_thrown() {
        Traceable cmd = mock(Traceable.class);
        when(cmd.utilisateurId()).thenReturn(new UtilisateurId("123456789"));
        TraceIn in = mock(TraceIn.class);
        TraceOut out = mock(TraceOut.class);
        when(traceInBuilder.build(any(), eq(cmd))).thenReturn(in);
        when(traceOutBuilder.build(isNull(), any(Throwable.class))).thenReturn(out);
        assertThrows(IllegalStateException.class, () -> service.ko(cmd));
        verify(publisher).publish(any(Trace.class));
    }

    @Test
    void should_not_publish_when_no_traceable_argument() {
        String result = service.withoutTrace();
        assertEquals("NO_TRACE", result);
        verify(publisher, never()).publish(any());
    }

*/

}
