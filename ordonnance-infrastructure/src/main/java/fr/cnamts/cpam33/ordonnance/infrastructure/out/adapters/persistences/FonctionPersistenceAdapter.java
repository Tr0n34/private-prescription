package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences;

import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.enums.OrdonnanceExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.Fonction;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.FonctionId;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.FonctionRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.traces.FonctionJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.FonctionEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces.FonctionEntityMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FonctionPersistenceAdapter implements FonctionRepository {

    private final FonctionJpaRepository fonctionJpaRepository;
    private final FonctionEntityMapper fonctionEntityMapper;

    public FonctionPersistenceAdapter(FonctionJpaRepository fonctionJpaRepository, FonctionEntityMapper fonctionEntityMapper) {
        this.fonctionJpaRepository = fonctionJpaRepository;
        this.fonctionEntityMapper = fonctionEntityMapper;
    }

    @Override
    public Optional<Fonction> findById(FonctionId id) {
        FonctionEntity entity = fonctionJpaRepository.findByCode(id.code())
                .orElseThrow(() -> new DomainException(OrdonnanceExceptionCode.BS_ORDONNANCE_PRESCRIPTION_MISSING));
        return Optional.ofNullable(fonctionEntityMapper.toDomain(entity));
    }

    @Override
    public List<Fonction> findAll() {
        return List.of();
    }

    @Override
    public Fonction save(Fonction domainObject) {
        return fonctionEntityMapper.toDomain(fonctionJpaRepository.save(fonctionEntityMapper.toEntity(domainObject)));
    }

}
