package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.ActeMetier;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.ActeMetierId;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.ActeMetierRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.repositories.traces.ActeMetierJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.repositories.traces.FonctionJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.ActeMetierEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.FonctionEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces.ActeMetierEntityMapper;
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
    private final FonctionJpaRepository fonctionJpaRepository;

    public ActeMetierPersistanceAdapter(ActeMetierJpaRepository acteMetierJpaRepository,
                                        ActeMetierEntityMapper acteMetierEntityMapper,
                                        FonctionJpaRepository fonctionJpaRepository) {
        this.acteMetierJpaRepository = acteMetierJpaRepository;
        this.acteMetierEntityMapper = acteMetierEntityMapper;
        this.fonctionJpaRepository = fonctionJpaRepository;
    }

    @Override
    public Optional<ActeMetier> findById(ActeMetierId id)  {
        return acteMetierJpaRepository.findByCode(id.code()).map(acteMetierEntityMapper::toDomain);
    }

    @Override
    public List<ActeMetier> findAll() {
        return List.of();
    }

    @Transactional
    public ActeMetier save(ActeMetier acteMetier) {
        ActeMetierEntity entity = acteMetierEntityMapper.toEntity(acteMetier);
        FonctionEntity fonctionEntity = fonctionJpaRepository
                .findByCode(entity.getFonction().getCode())
                .map(f -> {
                    if (!f.getDescription().equals(entity.getFonction().getDescription())) {
                        f.setDescription(entity.getFonction().getDescription());
                    }
                    return f;
                })
                .orElseGet(() -> fonctionJpaRepository.save(entity.getFonction()));
        entity.setFonction(fonctionEntity);
        return acteMetierEntityMapper.toDomain(acteMetierJpaRepository.save(entity));
    }

}
