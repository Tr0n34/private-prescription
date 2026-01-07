package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainObjectNotFound;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.ActeMetier;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.ActeMetierId;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.ActeMetierRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces.ActeMetierEntityMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.repositories.traces.ActeMetierJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class ActeMetierPersistanceAdapter implements ActeMetierRepository {

    private final ActeMetierJpaRepository acteMetierJpaRepository;
    private final ActeMetierEntityMapper acteMetierEntityMapper;

    public ActeMetierPersistanceAdapter(ActeMetierJpaRepository acteMetierJpaRepository, ActeMetierEntityMapper acteMetierEntityMapper) {
        this.acteMetierJpaRepository = acteMetierJpaRepository;
        this.acteMetierEntityMapper = acteMetierEntityMapper;
    }

    @Override
    public Optional<ActeMetier> findById(ActeMetierId id) throws DomainObjectNotFound {
        return acteMetierJpaRepository.findByCode(id.code()).map(acteMetierEntityMapper::toDomain);
    }

    @Override
    public ActeMetier save(ActeMetier domainObject) {
        return acteMetierEntityMapper.toDomain(acteMetierJpaRepository.save(acteMetierEntityMapper.toEntity(domainObject)));
    }

}
