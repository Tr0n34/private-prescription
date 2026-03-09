package fr.cnamts.cpam33.ordonnance.application.usecases.medicaments;

import fr.cnamts.cpam33.ordonnance.application.abstracts.QueryUseCase;
import fr.cnamts.cpam33.ordonnance.application.views.mappers.MedicamentViewMapper;
import fr.cnamts.cpam33.ordonnance.application.views.medicaments.MedicamentView;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.events.ActeMetierEvent;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.application.filters.PageRequest;
import fr.cnamts.cpam33.ordonnance.application.filters.PageResult;
import fr.cnamts.cpam33.ordonnance.application.filters.SortProvider;
import fr.cnamts.cpam33.ordonnance.application.queries.ListerMedicamentByCodeIdQuery;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.medicaments.MedicamentRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@ActeMetierEvent(ActeMetierCode.MEDICAMENT_LISTER)
public class ListerDetailsMedicamentUseCase implements QueryUseCase<ListerMedicamentByCodeIdQuery, MedicamentView> {

    private final SortProvider<MedicamentView> medicamentSortProvider;
    private final MedicamentRepository medicamentRepository;
    private final MedicamentViewMapper  medicamentViewMapper;

    public ListerDetailsMedicamentUseCase(@Qualifier("medicamentSortProvider") SortProvider<MedicamentView> medicamentSortProvider,
                                          MedicamentRepository medicamentRepository,
                                          MedicamentViewMapper medicamentViewMapper) {
        this.medicamentSortProvider = medicamentSortProvider;
        this.medicamentRepository = medicamentRepository;
        this.medicamentViewMapper = medicamentViewMapper;
    }

    @Override
    public List<MedicamentView> execute(ListerMedicamentByCodeIdQuery query) throws DomainException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PageResult<MedicamentView> execute(ListerMedicamentByCodeIdQuery query, PageRequest pageRequest) {
        var all = medicamentRepository.findByCodeIdAndVarType(query.codeSp(), query.varType());
        var views = all.stream()
                .map(medicamentViewMapper::toView)
                .toList();
        var sorted = applySort(views, pageRequest.sort());
        int offset = pageRequest.offset();
        int end = Math.min(offset + pageRequest.size(), sorted.size());
        var content = offset >= sorted.size() ? List.<MedicamentView>of() : sorted.subList(offset, end);
        return new PageResult<>(content, sorted.size(), pageRequest.page(), pageRequest.size());
    }


    private List<MedicamentView> applySort(List<MedicamentView> list, List<PageRequest.SortField> sortFields) {
        var effectiveSort = (sortFields == null || sortFields.isEmpty())
                ? medicamentSortProvider.defaultSort()
                : sortFields;
        Comparator<MedicamentView> comparator = effectiveSort.stream()
                .map(medicamentSortProvider::comparatorFor)
                .filter(Objects::nonNull)
                .reduce(Comparator::thenComparing)
                .orElse(null);
        return comparator == null ? list : list.stream().sorted(comparator).toList();
    }

}
