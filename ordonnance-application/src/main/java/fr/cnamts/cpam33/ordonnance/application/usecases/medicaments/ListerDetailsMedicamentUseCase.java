package fr.cnamts.cpam33.ordonnance.application.usecases.medicaments;

import fr.cnamts.cpam33.ordonnance.application.kernel.QueryUseCase;
import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.kernel.events.ActeMetierEvent;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.kernel.filters.SortProvider;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.Medicament;
import fr.cnamts.cpam33.ordonnance.domain.models.queries.ListerMedicamentByCodeIdQuery;
import fr.cnamts.cpam33.ordonnance.domain.kernel.filters.PageRequest;
import fr.cnamts.cpam33.ordonnance.domain.kernel.filters.PageResult;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.medicaments.MedicamentRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@ActeMetierEvent(ActeMetierCode.MEDICAMENT_LISTER)
public class ListerDetailsMedicamentUseCase implements QueryUseCase<ListerMedicamentByCodeIdQuery, Medicament> {

    private final SortProvider<Medicament> medicamentSortProvider;
    private final MedicamentRepository medicamentRepository;

    public ListerDetailsMedicamentUseCase(SortProvider<Medicament> medicamentSortProvider,
                                          MedicamentRepository medicamentRepository) {
        this.medicamentSortProvider = medicamentSortProvider;
        this.medicamentRepository = medicamentRepository;
    }

    @Override
    public List<Medicament> execute(ListerMedicamentByCodeIdQuery query) throws DomainException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PageResult<Medicament> execute(ListerMedicamentByCodeIdQuery query, PageRequest pageRequest) {
        var all = medicamentRepository.findByCodeIdAndVarType(query.codeSp(), query.varType());
        var sorted = applySort(all, pageRequest.sort());
        int offset = pageRequest.offset();
        int end = Math.min(offset + pageRequest.size(), sorted.size());
        var content = offset >= sorted.size() ? List.<Medicament>of() : sorted.subList(offset, end);
        return new PageResult<>(content, sorted.size(), pageRequest.page(), pageRequest.size());
    }

    private List<Medicament> applySort(List<Medicament> list, List<PageRequest.SortField> sortFields) {
        var effectiveSort = (sortFields == null || sortFields.isEmpty())
                ? medicamentSortProvider.defaultSort()
                : sortFields;
        Comparator<Medicament> comparator = effectiveSort.stream()
                .map(medicamentSortProvider::comparatorFor)
                .filter(Objects::nonNull)
                .reduce(Comparator::thenComparing)
                .orElse(null);
        return comparator == null ? list : list.stream().sorted(comparator).toList();
    }

}
