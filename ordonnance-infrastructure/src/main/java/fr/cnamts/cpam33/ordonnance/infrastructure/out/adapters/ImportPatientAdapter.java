package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identifiants.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.ImportPatientCandidate;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.FetchPatientGateway;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.patients.ExternalPatientResponseDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.patients.ImportPatientDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.patients.ExternalPatientDtoMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.patients.ImportPatientApiMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ImportPatientAdapter implements FetchPatientGateway {

    private static final Logger logger = LoggerFactory.getLogger(ImportPatientAdapter.class);

    private final RestClient restClient;
    private final ImportPatientApiMapper importPatientApiMapper;
    private final String apiUrl;
    private final ExternalPatientDtoMapper externalPatientDtoMapper;

    public ImportPatientAdapter(@Value("${patient.api.url}") String apiUrl,
                                @Qualifier("restClientPatient") RestClient restClient,
                                ImportPatientApiMapper importPatientApiMapper,
                                ExternalPatientDtoMapper externalPatientDtoMapper) {
        this.apiUrl = apiUrl;
        this.restClient = restClient;
        this.importPatientApiMapper = importPatientApiMapper;
        this.externalPatientDtoMapper = externalPatientDtoMapper;
    }

    @Override
    public ImportPatientCandidate fetchById(ExternalPatientId externalPatientId) {
        ExternalPatientResponseDto response = restClient.get()
                .uri(apiUrl, externalPatientId.numero())
                .retrieve()
                .body(ExternalPatientResponseDto.class);
        logger.debug("ACL to normalized ImportPatientDto. Fetched external patient with external id: {}", externalPatientId);
        ImportPatientDto importPatientDto = externalPatientDtoMapper.toImportDto(response);
        return importPatientApiMapper.toDomainWithoutId(importPatientDto);
    }

}