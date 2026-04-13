package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.providers.traces;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.ActeMetier;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.ActeMetierId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.Fonction;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.FonctionId;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.InfrastructureException;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.enums.ErrorResolvingExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.traces.ActeMetierJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.ActeMetierEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DatabaseActeMetierResolver {

    private final ActeMetierJpaRepository acteMetierJpaRepository;

    public DatabaseActeMetierResolver(ActeMetierJpaRepository acteMetierJpaRepository) {
        this.acteMetierJpaRepository = acteMetierJpaRepository;
    }

    public ActeMetier resolve(ActeMetierCode acteMetierCode) {
        return resolveByCode(acteMetierCode.toString());
    }

    public ActeMetier resolveByCode(String acteMetierCode) {
        ActeMetierEntity entity = acteMetierJpaRepository.findByCode(acteMetierCode).orElseThrow(
                () -> new InfrastructureException(ErrorResolvingExceptionCode.TECH_RESOLVER_ACTE_METIER_CODE_INEXISTANT,
                        Map.of("acteMetier", acteMetierCode))
        );
        return new ActeMetier(
                new ActeMetierId(entity.getCode()),
                entity.getObjetMetierName(),
                new Fonction(new FonctionId(entity.getFonction().getCode()), entity.getFonction().getDescription())
        );
    }

}
