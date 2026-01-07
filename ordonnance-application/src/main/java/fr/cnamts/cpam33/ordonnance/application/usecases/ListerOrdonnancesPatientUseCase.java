package fr.cnamts.cpam33.ordonnance.application.usecases;

import fr.cnamts.cpam33.ordonnance.application.abstracts.QueryUseCase;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.Ordonnance;
import fr.cnamts.cpam33.ordonnance.domain.models.queries.ListerOrdonnancesPatientQuery;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.ordonnances.OrdonnanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListerOrdonnancesPatientUseCase implements QueryUseCase<ListerOrdonnancesPatientQuery, Ordonnance> {

    private final OrdonnanceRepository repository;

    public ListerOrdonnancesPatientUseCase(OrdonnanceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Ordonnance> execute(ListerOrdonnancesPatientQuery query) {
        List<Ordonnance> ordonnances = repository.findByPatientId(query.patientId());
        ordonnances = ordonnances.stream()
                .filter(o -> !o.createdOn().isBefore(query.dateDebut())
                        && !o.createdOn().isAfter(query.dateFin()))
                .toList();
        if (query.isSigned()) {
            ordonnances = ordonnances.stream()
                    .filter(Ordonnance::isSigned)
                    .toList();
        }
        return ordonnances;
    }

}