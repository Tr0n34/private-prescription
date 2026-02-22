package fr.cnamts.cpam33.ordonnance.domain.ports.out.medicaments;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.Medicament;

import java.util.Optional;

public interface MedicamentRepository {

    Optional<Medicament> findByCodeIdAndVarType(String codeId, String varType);

}
