package fr.cnamts.cpam33.ordonnance.infrastructure.out.providers;

import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.patients.ExternalPatientDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternalPatientApiClient {

    private final RestTemplate restTemplate;

    @Value("${patient.api.url}")
    private String apiUrl;

    public ExternalPatientApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ExternalPatientDto fetchPatient(String patientId) {
        return restTemplate.getForObject(apiUrl, ExternalPatientDto.class, patientId);
    }
}