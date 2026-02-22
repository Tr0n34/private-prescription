package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.enums.TraceExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.InfrastructureException;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.ActeMetierEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.TraceOverflowEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.OffsetDateTime;

@Component
@ConditionalOnProperty(name = "ordonnance.traces.mode", havingValue = "INTERNAL_QUEUEING")
public class TraceOutboxWriter {

    private final EntityManagerFactory traceEmf;
    private final ObjectMapper mapper;
    private final Clock clock;

    public TraceOutboxWriter(@Qualifier("traceEntityManagerFactory") EntityManagerFactory traceEmf,
                             @Qualifier("traceContextMapper") ObjectMapper mapper,
                             Clock clock) {
        this.traceEmf = traceEmf;
        this.mapper = mapper;
        this.clock = clock;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void write(Trace trace) {
        EntityManager em = traceEmf.createEntityManager();
        EntityTransaction transaction = null;
        try (em) {
            transaction = em.getTransaction();
            transaction.begin();
            TraceOverflowEntity traceOverflowEntity =createTraceOverflowEntity(em, trace);
            em.persist(traceOverflowEntity);
            em.flush();
            transaction.commit();
        } catch (Exception ex) {
            if ( transaction != null && transaction.isActive() ) {
                transaction.rollback();
            }
            if ( ex instanceof RuntimeException re ) {
                throw re;
            }
            throw new InfrastructureException(TraceExceptionCode.TECH_TRACE_OUTBOX_WRITE_CRASH);
        }
    }

    private TraceOverflowEntity createTraceOverflowEntity(EntityManager em, Trace trace) throws JsonProcessingException {
        String traceId = trace.traceId().numero();
        String utilisateurId = trace.utilisateurId().numero();
        String boundedContext = trace.boundedContext();
        String eventCode = trace.acteMetierId().code();
        ActeMetierEntity acteMetierRef = em.getReference(ActeMetierEntity.class, eventCode);
        String traceInJson = mapper.writeValueAsString(trace.context().in());
        String traceOutJson = mapper.writeValueAsString(trace.context().out());
        return new TraceOverflowEntity()
                .setTraceId(traceId)
                .setActeMetier(acteMetierRef)
                .setUtilisateurId(utilisateurId)
                .setBoundedContext(boundedContext)
                .setTraceIn(traceInJson)
                .setTraceOut(traceOutJson)
                .setCreatedOn(OffsetDateTime.now(clock).toString());
    }

}
