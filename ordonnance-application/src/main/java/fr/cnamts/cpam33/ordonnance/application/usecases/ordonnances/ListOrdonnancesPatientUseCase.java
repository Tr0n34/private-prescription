package fr.cnamts.cpam33.ordonnance.application.usecases.ordonnances;

import fr.cnamts.cpam33.ordonnance.application.kernel.QueryUseCase;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.kernel.filters.PageRequest;
import fr.cnamts.cpam33.ordonnance.domain.kernel.filters.PageResult;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.Ordonnance;
import fr.cnamts.cpam33.ordonnance.domain.models.queries.ListerOrdonnancesPatientQuery;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.ordonnances.OrdonnanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListOrdonnancesPatientUseCase implements QueryUseCase<ListerOrdonnancesPatientQuery, Ordonnance> {

    private final OrdonnanceRepository repository;

    public ListOrdonnancesPatientUseCase(OrdonnanceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Ordonnance> execute(ListerOrdonnancesPatientQuery query) {
        List<Ordonnance> ordonnances = repository.findByPatientId(query.patientId());
        ordonnances = ordonnances.stream()
                .filter(o -> !o.createdOn().isBefore(query.dateDebut())
                        && !o.createdOn().isAfter(query.dateFin()))
                .toList();
        if ( query.isSigned() ) {
            ordonnances = ordonnances.stream()
                    .filter(Ordonnance::isSigned)
                    .toList();
        }
        return ordonnances;
    }

    @Override
    public PageResult<Ordonnance> execute(ListerOrdonnancesPatientQuery query, PageRequest pageRequest) throws DomainException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}