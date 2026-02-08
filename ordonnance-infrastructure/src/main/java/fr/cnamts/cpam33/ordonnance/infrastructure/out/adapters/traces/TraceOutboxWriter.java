package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.TraceOutboxEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class TraceOutboxWriter {

    private final EntityManagerFactory traceEmf;
    private final ObjectMapper mapper;

    public TraceOutboxWriter(@Qualifier("traceEntityManagerFactory") EntityManagerFactory traceEmf,
                             @Qualifier("traceContextMapper") ObjectMapper mapper) {
        this.traceEmf = traceEmf;
        this.mapper = mapper;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void write(Trace trace) {
        EntityManager em = traceEmf.createEntityManager();
        EntityTransaction tx = null;
        try (em) {
            tx = em.getTransaction();
            tx.begin();
            String payload = mapper.writeValueAsString(trace);
            TraceOutboxEntity e = new TraceOutboxEntity()
                    .setPayloadJson(payload)
                    .setCreatedAt(LocalDateTime.now())
                    .setAttempts(0)
                    .setLastError(null);
            em.persist(e);
            em.flush();
            tx.commit();
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            if (ex instanceof RuntimeException re) throw re;
            throw new IllegalStateException(ex);
        }
    }

}
