package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.Medecin;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.MedecinId;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.MedecinExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.medecins.MedecinRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.Adapter;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.ordonnances.MedecinJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.medecins.MedecinEntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MedecinPersistenceAdapter implements MedecinRepository, Adapter {

    private final MedecinJpaRepository  medecinJpaRepository;
    private final MedecinEntityMapper medecinEntityMapper;

    public MedecinPersistenceAdapter(MedecinJpaRepository medecinJpaRepository, MedecinEntityMapper medecinEntityMapper) {
        this.medecinJpaRepository = medecinJpaRepository;
        this.medecinEntityMapper = medecinEntityMapper;
    }

    @Override
    public Optional<Medecin> findById(MedecinId medecinId) {
        return Optional.of(medecinEntityMapper.toDomain(medecinJpaRepository.findByExternalId(medecinId.id()).orElseThrow(
                () -> new DomainException(MedecinExceptionCode.BS_MEDECIN_NOT_FOUND)
        )));
    }

    @Override
    public List<Medecin> findAll() {
        return List.of();
    }

    @Override
    public Medecin save(Medecin medecin) {
        return medecinEntityMapper.toDomain(medecinJpaRepository.save(medecinEntityMapper.toEntity(medecin))
        );
    }

}
