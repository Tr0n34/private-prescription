package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainObjectNotFound;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.OrdonnanceExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.Fonction;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.FonctionId;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.FonctionRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.FonctionEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces.FonctionEntityMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.repositories.traces.FonctionJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class FonctionPersistanceAdapter implements FonctionRepository {

    private final FonctionJpaRepository fonctionJpaRepository;
    private final FonctionEntityMapper fonctionEntityMapper;

    public FonctionPersistanceAdapter(FonctionJpaRepository fonctionJpaRepository, FonctionEntityMapper fonctionEntityMapper) {
        this.fonctionJpaRepository = fonctionJpaRepository;
        this.fonctionEntityMapper = fonctionEntityMapper;
    }

    @Override
    public Optional<Fonction> findById(FonctionId id) throws DomainObjectNotFound {
        FonctionEntity entity = fonctionJpaRepository.findByCode(id.code())
                .orElseThrow(() -> new DomainObjectNotFound(
                        OrdonnanceExceptionCode.BS_ORDONNANCE_PRESCRIPTION_MISSING));
        return Optional.ofNullable(fonctionEntityMapper.toDomain(entity));
    }

    @Override
    public Fonction save(Fonction domainObject) {
        return fonctionEntityMapper.toDomain(fonctionJpaRepository.save(fonctionEntityMapper.toEntity(domainObject)));
    }

}
