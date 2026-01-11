package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters;

import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.Ordonnance;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.OrdonnanceId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins.MedecinId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.patients.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.ordonnances.OrdonnanceRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.OrdonnanceIdEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances.OrdonnanceEntityMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.repositories.ordonnances.OrdonnanceJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrdonnanceRepositoryAdapter implements OrdonnanceRepository {

    private final OrdonnanceJpaRepository ordonnanceJpaRepository;
    private final OrdonnanceEntityMapper ordonnanceEntityMapper;

    public OrdonnanceRepositoryAdapter(OrdonnanceEntityMapper ordonnanceEntityMapper, OrdonnanceJpaRepository ordonnanceJpaRepository) {
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
