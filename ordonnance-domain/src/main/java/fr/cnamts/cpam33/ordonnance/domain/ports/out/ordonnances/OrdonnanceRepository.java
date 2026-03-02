package fr.cnamts.cpam33.ordonnance.domain.ports.out.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.kernel.ports.RepositoryPort;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.Ordonnance;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.MedecinId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identifiants.OrdonnanceId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identifiants.PatientId;

import java.util.List;

public interface OrdonnanceRepository extends RepositoryPort<Ordonnance, OrdonnanceId> {

    List<Ordonnance> findByMedecinId(MedecinId medecin);

    List<Ordonnance> findByPatientId(PatientId patient);

    void delete(OrdonnanceId ordonnanceId);

}
