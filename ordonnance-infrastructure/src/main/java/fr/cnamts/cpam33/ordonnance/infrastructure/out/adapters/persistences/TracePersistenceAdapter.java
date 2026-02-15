package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences;

import fr.cnamts.cpam33.ordonnance.domain.kernel.ids.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.ActeMetierId;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.TraceId;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TraceRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TracePersistenceAdapter implements TraceRepository {

    @Override
    public Optional<Trace> findByUtilisateurId(UtilisateurId utilisateurId) {
        return Optional.empty();
    }

    @Override
    public Optional<Trace> findByTraceId(TraceId traceId) {
        return Optional.empty();
    }

    @Override
    public Optional<Trace> findByActeMetierId(ActeMetierId acteMetierId) {
        return Optional.empty();
    }

    @Override
    public Optional<Trace> findById(TraceId domainObjectId) {
        return Optional.empty();
    }

    @Override
    public List<Trace> findAll() {
        return List.of();
    }

    @Override
    public Trace save(Trace domainObject) {
        return null;
    }

}
