package fr.cnamts.cpam33.ordonnance.application.usecases.medicaments;

import fr.cnamts.cpam33.ordonnance.application.kernel.QueryUseCase;
import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.kernel.events.ActeMetierEvent;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.NotFound;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.Medicament;
import fr.cnamts.cpam33.ordonnance.domain.models.queries.ListerMedicamentByCodeIdQuery;
import fr.cnamts.cpam33.ordonnance.domain.models.queries.PageRequest;
import fr.cnamts.cpam33.ordonnance.domain.models.queries.PageResult;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.medicaments.MedicamentRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@ActeMetierEvent(ActeMetierCode.MEDICAMENT_LISTER)
public class ListerDetailsMedicamentUseCase implements QueryUseCase<ListerMedicamentByCodeIdQuery, Medicament> {

    private final MedicamentRepository medicamentRepository;

    public ListerDetailsMedicamentUseCase(MedicamentRepository medicamentRepository) {
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
        if (sortFields == null || sortFields.isEmpty()) return list;
        Comparator<Medicament> comparator = null;
        for (var field : sortFields) {
            Comparator<Medicament> c = comparatorFor(field.field());
            if (c == null) continue;
            if (field.direction() == PageRequest.Direction.DESC) {
                c = c.reversed();
            }
            comparator = comparator == null ? c : comparator.thenComparing(c);
        }
        return comparator == null ? list : list.stream().sorted(comparator).toList();
    }

    private Comparator<Medicament> comparatorFor(String field) {
        return switch (field) {
            case "nom" -> Comparator.comparing(
                    Medicament::nom,
                    Comparator.nullsLast(String::compareToIgnoreCase)
            );
            case "cipUcd" -> Comparator.comparing(
                    Medicament::cipUcd,
                    Comparator.nullsLast(String::compareToIgnoreCase)
            );
            case "catcCode" -> Comparator.comparing(
                    Medicament::catcCode,
                    Comparator.nullsLast(String::compareToIgnoreCase)
            );
            default -> null;
        };
    }

}
