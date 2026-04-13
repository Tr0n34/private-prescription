package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences;

import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.Ordonnance;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.MedecinId;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.OrdonnanceId;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.ordonnances.OrdonnanceRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.ordonnances.OrdonnanceJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.OrdonnanceIdEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances.OrdonnanceEntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrdonnancePersistenceAdapter implements OrdonnanceRepository {

    private final OrdonnanceJpaRepository ordonnanceJpaRepository;
    private final OrdonnanceEntityMapper ordonnanceEntityMapper;

    public OrdonnancePersistenceAdapter(OrdonnanceEntityMapper ordonnanceEntityMapper, OrdonnanceJpaRepository ordonnanceJpaRepository) {
        this.ordonnanceEntityMapper = ordonnanceEntityMapper;
        this.ordonnanceJpaRepository = ordonnanceJpaRepository;
    }

    @Override
    public Optional<Ordonnance> findById(OrdonnanceId ordonnanceId) {
        return ordonnanceJpaRepository
                .findById(new OrdonnanceIdEntity(ordonnanceId.numero()))
                .map(ordonnanceEntityMapper::toDomain);
    }

    @Override
    public List<Ordonnance> findAll() {
        return List.of();
    }

    @Override
    public List<Ordonnance> findByMedecinId(MedecinId medecin) {
        return List.of();
    }

    @Override
    public List<Ordonnance> findByPatientId(PatientId patient) {
        return List.of();
    }

    @Override
    public Ordonnance save(Ordonnance ordonnance) {
        return null;
    }

    @Override
    public void delete(OrdonnanceId ordonnanceId) {
        ordonnanceJpaRepository.deleteById(new OrdonnanceIdEntity(ordonnanceId.numero()));
    }

}
