package fr.cnamts.cpam33.ordonnance.application.usecases.ordonnances;

import fr.cnamts.cpam33.ordonnance.application.abstracts.CommandUseCase;
import fr.cnamts.cpam33.ordonnance.application.views.OrdonnanceView;
import fr.cnamts.cpam33.ordonnance.application.views.mappers.OrdonnanceViewMapper;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.events.ActeMetierEvent;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.NotFound;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.Ordonnance;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.Medecin;
import fr.cnamts.cpam33.ordonnance.application.commands.ordonnances.CreateOrdonnanceCmd;
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
@ActeMetierEvent(ActeMetierCode.ORD_CREER)
public class CreateOrdonnanceUseCase implements CommandUseCase<CreateOrdonnanceCmd, OrdonnanceView> {

    private final OrdonnanceRepository ordonnanceRepository;
    private final PatientRepository patientRepository;
    private final MedecinRepository medecinRepository;
    private final OrdonnanceNumGenerator ordonnanceNumGenerator;
    private final OrdonnanceViewMapper ordonnanceViewMapper;

    public CreateOrdonnanceUseCase(OrdonnanceRepository ordonnanceRepository,
                                   PatientRepository patientRepository,
                                   MedecinRepository medecinRepository,
                                   OrdonnanceNumGenerator ordonnanceNumGenerator, OrdonnanceViewMapper ordonnanceViewMapper) {
        this.ordonnanceRepository = ordonnanceRepository;
        this.patientRepository = patientRepository;
        this.medecinRepository = medecinRepository;
        this.ordonnanceNumGenerator = ordonnanceNumGenerator;
        this.ordonnanceViewMapper = ordonnanceViewMapper;
    }

    @Override
    public OrdonnanceView execute(CreateOrdonnanceCmd command) throws DomainException {
        Ordonnance ordonnance = Ordonnance.of(
                OrdonnanceIdFactory.withGenerator(ordonnanceNumGenerator).create(),
                patientRepository.findById(command.patientId()).orElseThrow(
                        () -> NotFound.of(Patient.class.getName(), command.patientId().numero())),
                medecinRepository.findById(command.medecinId()).orElseThrow(
                        () -> NotFound.of(Medecin.class.getName(), command.medecinId().rpps())),
                List.of());
        return ordonnanceViewMapper.toView(ordonnanceRepository.save(ordonnance));
    }

}
