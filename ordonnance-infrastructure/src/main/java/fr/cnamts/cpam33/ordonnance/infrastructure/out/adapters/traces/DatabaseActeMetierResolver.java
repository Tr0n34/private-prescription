package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.ActeMetier;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.ActeMetierId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.Fonction;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.FonctionId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.traces.ActeMetierJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.traces.FonctionJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.ActeMetierEntity;
import org.springframework.stereotype.Component;

@Component
public class DatabaseActeMetierResolver {

    public static final String UNRESOLVED_ERROR_MESSAGE = "Error code not found : %s";

    private final ActeMetierJpaRepository acteMetierJpaRepository;

    public DatabaseActeMetierResolver(ActeMetierJpaRepository acteMetierJpaRepository) {
        this.acteMetierJpaRepository = acteMetierJpaRepository;
    }

    public ActeMetier resolve(ActeMetierCode acteMetierCode) {
        return resolveByCode(acteMetierCode.toString());
    }


    public ActeMetier resolveByCode(String acteMetierCode) {
        ActeMetierEntity entity = acteMetierJpaRepository.findByCode(acteMetierCode).orElseThrow(
                () -> new IllegalStateException(String.format(UNRESOLVED_ERROR_MESSAGE, acteMetierCode))
        );
        return new ActeMetier(
                new ActeMetierId(entity.getCode()),
                entity.getObjetMetierName(),
                new Fonction(new FonctionId(entity.getFonction().getCode()), entity.getFonction().getDescription())
        );
    }

}
