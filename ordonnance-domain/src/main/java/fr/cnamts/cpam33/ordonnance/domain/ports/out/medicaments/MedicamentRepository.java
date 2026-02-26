package fr.cnamts.cpam33.ordonnance.domain.ports.out.medicaments;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.Medicament;

import java.util.List;
import java.util.Optional;

public interface MedicamentRepository {

    List<Medicament> findByCodeIdAndVarType(String codeId, String varType);

}
