package fr.cnamts.cpam33.ordonnance.application.usecases.medicaments;

import fr.cnamts.cpam33.ordonnance.application.kernel.QueryUseCase;
import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.kernel.events.ActeMetierEvent;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.Medicament;
import fr.cnamts.cpam33.ordonnance.domain.models.queries.ListerMedicamentByCodeIdQuery;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.medicaments.MedicamentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ActeMetierEvent(ActeMetierCode.MEDICAMENT_LISTER)
public class ListerMedicamentUseCase implements QueryUseCase<ListerMedicamentByCodeIdQuery, Medicament> {

    private final MedicamentRepository medicamentRepository;

    public ListerMedicamentUseCase(MedicamentRepository medicamentRepository) {
        this.medicamentRepository = medicamentRepository;
    }

    @Override
    public List<Medicament> execute(ListerMedicamentByCodeIdQuery listerMedicamentByCodeIdQuery) {
        return  medicamentRepository.findByCodeIdAndVarType(listerMedicamentByCodeIdQuery.codeId(), listerMedicamentByCodeIdQuery.varType()).stream().toList();
    }

}
