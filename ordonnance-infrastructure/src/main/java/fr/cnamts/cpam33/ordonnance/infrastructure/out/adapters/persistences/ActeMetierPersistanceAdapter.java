package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.ActeMetier;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.ActeMetierId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.Fonction;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.ActeMetierRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.traces.ActeMetierJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.traces.FonctionJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.ActeMetierEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.FonctionEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces.ActeMetierEntityMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces.FonctionEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ActeMetierPersistanceAdapter implements ActeMetierRepository {

    private static final Logger logger =  LoggerFactory.getLogger(ActeMetierPersistanceAdapter.class);

    private final ActeMetierJpaRepository acteMetierJpaRepository;
    private final ActeMetierEntityMapper acteMetierEntityMapper;
    private final FonctionEntityMapper fonctionEntityMapper;
    private final FonctionJpaRepository fonctionJpaRepository;

    public ActeMetierPersistanceAdapter(ActeMetierJpaRepository acteMetierJpaRepository,
                                        ActeMetierEntityMapper acteMetierEntityMapper,
                                        FonctionEntityMapper fonctionEntityMapper,
                                        FonctionJpaRepository fonctionJpaRepository) {
        this.acteMetierJpaRepository = acteMetierJpaRepository;
        this.acteMetierEntityMapper = acteMetierEntityMapper;
        this.fonctionEntityMapper = fonctionEntityMapper;
        this.fonctionJpaRepository = fonctionJpaRepository;
    }

    @Override
    public Optional<ActeMetier> findById(ActeMetierId id)  {
        return acteMetierJpaRepository.findByCode(id.code()).map(acteMetierEntityMapper::toDomain);
    }

    @Override
    public List<ActeMetier> findAll() {
        return acteMetierJpaRepository.findAll().stream().map(acteMetierEntityMapper::toDomain).toList();
    }

    @Transactional
    public ActeMetier save(ActeMetier acteMetier) {
        ActeMetierEntity entity = acteMetierEntityMapper.toEntity(acteMetier);
        FonctionEntity fonctionEntity = fonctionJpaRepository
                .findByCode(entity.getFonction().getCode())
                .map(f -> {
                    if ( !f.getDescription().equals(entity.getFonction().getDescription()) ) {
                        f.setDescription(entity.getFonction().getDescription());
                    }
                    return f;
                })
                .orElseGet(() -> fonctionJpaRepository.save(entity.getFonction()));
        entity.setFonction(fonctionEntity);
        logger.debug("save ActeMetier : {}", entity.getCode());
        return acteMetierEntityMapper.toDomain(acteMetierJpaRepository.save(entity));
    }

    @Override
    public Fonction findFonctionByActeMetierId(ActeMetierId acteMetierId) {
        return acteMetierJpaRepository.findByCode(acteMetierId.code())
                .map(ActeMetierEntity::getFonction)
                .map(FonctionEntity::getCode)
                .flatMap(fonctionJpaRepository::findByCode)
                .map(fonctionEntityMapper::toDomain)
                .orElseThrow();
    }
}
