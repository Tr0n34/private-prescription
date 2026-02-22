package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.Medicament;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.medicaments.MedicamentRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.thesorimed.ThesorimedRoutineExecutor;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.medicaments.MedicamentRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class MedicamentPersistenceAdapter implements MedicamentRepository {

    public static final String THESORIMED_GET_THE_SPE_DETAILS = "thesorimed.get_the_spe_details";

    private final ThesorimedRoutineExecutor executor;
    private final MedicamentRowMapper medicamentRowMapper;

    public MedicamentPersistenceAdapter(ThesorimedRoutineExecutor executor,
                                        MedicamentRowMapper medicamentRowMapper) {
        this.executor = executor;
        this.medicamentRowMapper = medicamentRowMapper;
    }

    @Override
    public List<Medicament> findByCodeIdAndVarType(String codeId, String varType) {
        List<Map<String, Object>> rows = executor.functionRefcursor(
                THESORIMED_GET_THE_SPE_DETAILS,
                List.of(codeId, new BigDecimal(varType)),
                new ColumnMapRowMapper()
        );
        return rows.stream()
                .map(medicamentRowMapper::map)
                .toList();
    }

}
