package fr.cnamts.cpam33.ordonnance.domain.ports.out.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.RepositoryPort;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.Ordonnance;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.OrdonnanceId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins.MedecinId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.patients.PatientId;

import java.util.List;

public interface OrdonnanceRepository extends RepositoryPort<Ordonnance, OrdonnanceId> {

    List<Ordonnance> findByMedecinId(MedecinId medecin);

    List<Ordonnance> findByPatientId(PatientId patient);

    void delete(OrdonnanceId ordonnanceId);

}
