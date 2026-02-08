package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.FetchPatientGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ImportPatientApiClient implements FetchPatientGateway {

    private final RestTemplate restTemplate;

    private String apiUrl;

    public ImportPatientApiClient(@Value("${patient.api.url}") String apiUrl,
                                  RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Patient fetchById(ExternalPatientId externalPatientId) {
        return restTemplate.getForObject(apiUrl, Patient.class, externalPatientId);
    }

}