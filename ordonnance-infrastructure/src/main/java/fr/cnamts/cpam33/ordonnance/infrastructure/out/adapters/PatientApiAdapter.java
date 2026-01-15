package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters;

import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.PatientRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.Adapter;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances.PatientEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PatientApiAdapter implements Adapter {

    private static final Logger logger = LoggerFactory.getLogger(PatientApiAdapter.class);

    private final PatientRepository patientRepository;
    private final ExternalPatientApiClient externalPatientApiClient;
    private final PatientEntityMapper pattientEntityMapper;

    public PatientApiAdapter(PatientRepository patientRepository, ExternalPatientApiClient externalPatientApiClient, PatientEntityMapper pattientEntityMapper) {
        this.patientRepository = patientRepository;
        this.externalPatientApiClient = externalPatientApiClient;
        this.pattientEntityMapper = pattientEntityMapper;
    }


}
