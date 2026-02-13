package fr.cnamts.cpam33.ordonnance.application.services;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.ActeMetier;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.ActeMetierId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.TraceContext;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.TraceId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TracePublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraceServiceTest {

    @Mock
    private TracePublisher tracePublisher;

    private TraceService service;
    private Clock clock;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(Instant.parse("2026-02-13T09:00:00Z"), ZoneOffset.UTC);
        service = new TraceService(clock, tracePublisher);
    }

    @Test
    void should_publish_trace_with_acteMetierCode() {
        ActeMetierCode code = ActeMetierCode.ACT_ORD_CREER;
        UtilisateurId utilisateurId = new UtilisateurId("u-123");
        TraceContext context = mock(TraceContext.class);
        service.trace(code, utilisateurId, context);
        ActeMetierId expectedId = new ActeMetierId(code.name());
        verify(tracePublisher).publish(argThat(trace ->
                trace != null
                        && trace.utilisateurId().equals(utilisateurId)
                        && trace.context().equals(context)
                        && trace.timestamp().equals(LocalDateTime.ofInstant(clock.instant(), clock.getZone()))
                        && trace.acteMetierId().equals(expectedId)
        ));
        verifyNoMoreInteractions(tracePublisher);
    }
    @Test
    void should_publish_trace_with_acteMetier() {
        ActeMetier acteMetier = mock(ActeMetier.class);
        ActeMetierCode code = ActeMetierCode.ACT_ORD_CREER;
        ActeMetierId expectedId = new ActeMetierId(code.name());
        when(acteMetier.acteMetierId()).thenReturn(expectedId);
        UtilisateurId utilisateurId = new UtilisateurId("u-999");
        TraceContext context = mock(TraceContext.class);
        service.trace(acteMetier, utilisateurId, context);
        verify(tracePublisher).publish(argThat(t -> t != null
                && t.acteMetierId().equals(expectedId)
                && t.utilisateurId().equals(utilisateurId)
                && t.timestamp().equals(LocalDateTime.ofInstant(clock.instant(), clock.getZone()))
        ));
        verify(acteMetier).acteMetierId();
        verifyNoMoreInteractions(tracePublisher, acteMetier);
    }

    @Test
    void archiver_should_throw() {
        assertThrows(UnsupportedOperationException.class, () -> service.archiver(new TraceId("12345678")));
        verifyNoInteractions(tracePublisher);
    }

}
