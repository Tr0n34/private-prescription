package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.FetchPatientGateway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ImportPatientApiClient implements FetchPatientGateway {

    private final RestClient restClient;

    private String apiUrl;

    public ImportPatientApiClient(@Value("${patient.api.url}") String apiUrl,
                                  @Qualifier("restClientPatient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public Patient fetchById(ExternalPatientId externalPatientId) {
        return restClient.get()
                .uri(this.apiUrl, externalPatientId.numero())
                .retrieve()
                .body(Patient.class);
    }

}