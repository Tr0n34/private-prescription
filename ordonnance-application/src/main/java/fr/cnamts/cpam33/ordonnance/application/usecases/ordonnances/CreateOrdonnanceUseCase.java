package fr.cnamts.cpam33.ordonnance.application.usecases.ordonnances;

import fr.cnamts.cpam33.ordonnance.application.abstracts.CommandUseCase;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.Ordonnance;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.Medecin;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.ordonnances.CreateOrdonnanceCmd;
import fr.cnamts.cpam33.ordonnance.domain.models.events.ActeMetierEvent;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.NotFound;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.medecins.MedecinRepository;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.ordonnances.OrdonnanceIdFactory;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.ordonnances.OrdonnanceNumGenerator;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.ordonnances.OrdonnanceRepository;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@ActeMetierEvent(ActeMetierCode.ACT_ORD_CREER)
public class CreateOrdonnanceUseCase implements CommandUseCase<CreateOrdonnanceCmd, Ordonnance> {

    private final OrdonnanceRepository ordonnanceRepository;
    private final PatientRepository patientRepository;
    private final MedecinRepository medecinRepository;
    private final OrdonnanceNumGenerator ordonnanceNumGenerator;

    public CreateOrdonnanceUseCase(OrdonnanceRepository ordonnanceRepository,
                                   PatientRepository patientRepository,
                                   MedecinRepository medecinRepository,
                                   OrdonnanceNumGenerator ordonnanceNumGenerator) {
        this.ordonnanceRepository = ordonnanceRepository;
        this.patientRepository = patientRepository;
        this.medecinRepository = medecinRepository;
        this.ordonnanceNumGenerator = ordonnanceNumGenerator;
    }

    @Override
    public Ordonnance execute(CreateOrdonnanceCmd command) throws DomainException {
        Ordonnance ordonnance = Ordonnance.of(
                OrdonnanceIdFactory.withGenerator(ordonnanceNumGenerator).create(),
                patientRepository.findById(command.patientId()).orElseThrow(
                        () -> NotFound.of(Patient.class.getName(), command.patientId().externalId())),
                medecinRepository.findById(command.medecinId()).orElseThrow(
                        () -> NotFound.of(Medecin.class.getName(), command.medecinId().rpps())),
                List.of());
        return ordonnanceRepository.save(ordonnance);
    }

}
