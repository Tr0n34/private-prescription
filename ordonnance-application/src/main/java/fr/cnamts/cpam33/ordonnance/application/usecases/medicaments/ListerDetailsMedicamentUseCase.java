package fr.cnamts.cpam33.ordonnance.application.usecases.medicaments;

import fr.cnamts.cpam33.ordonnance.application.kernel.QueryUseCase;
import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.kernel.events.ActeMetierEvent;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.NotFound;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.Medicament;
import fr.cnamts.cpam33.ordonnance.domain.models.queries.ListerMedicamentByCodeIdQuery;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.medicaments.MedicamentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ActeMetierEvent(ActeMetierCode.MEDICAMENT_LISTER)
public class ListerDetailsMedicamentUseCase implements QueryUseCase<ListerMedicamentByCodeIdQuery, Medicament> {

    private final MedicamentRepository medicamentRepository;

    public ListerDetailsMedicamentUseCase(MedicamentRepository medicamentRepository) {
        this.medicamentRepository = medicamentRepository;
    }

    @Override
    public List<Medicament> execute(ListerMedicamentByCodeIdQuery listerMedicamentByCodeIdQuery) {
        List<Medicament> medicaments = medicamentRepository.findByCodeIdAndVarType(
                listerMedicamentByCodeIdQuery.codeSp(),
                listerMedicamentByCodeIdQuery.varType())
                .stream().toList();
        if ( medicaments.isEmpty() ) {
            throw NotFound.of(Medicament.class.getName(), listerMedicamentByCodeIdQuery.codeSp());
        }
        return medicaments;
    }

}
