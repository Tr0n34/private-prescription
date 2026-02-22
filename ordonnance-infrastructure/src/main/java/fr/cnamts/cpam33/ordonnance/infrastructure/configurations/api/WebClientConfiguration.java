package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.api;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties({
        PatientApiProperties.class
})
public class WebClientConfiguration {

    public static final String HEADER_ACCEPT = "Accept";

    @Bean("restClientPatient")
    public RestClient restClientPatient(RestClient.Builder restClientBuilder, PatientApiProperties patientApiProperties) {
        return restClientBuilder
                .baseUrl(patientApiProperties.url())
                .defaultHeader(HEADER_ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean("restClientMedecin")
    public RestClient restClientMedecin(RestClient.Builder restClientBuilder, PatientApiProperties patientApiProperties) {
        return restClientBuilder
                .baseUrl(patientApiProperties.url())
                .defaultHeader(HEADER_ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

}
