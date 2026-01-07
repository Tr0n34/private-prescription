package fr.cnamts.cpam33.ordonnance.infrastructure.in.dto;

import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.medecins.MedecinIdDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.patients.PatientIdDto;
import jakarta.validation.Valid;

public record OrdonnanceDto(
        @Valid PatientIdDto patientId,
        @Valid MedecinIdDto medecinId
) {

}
