package fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.patients;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalPatientResponseDto(
        @JsonProperty("con_id_externe")
        String conIdExterne,
        @JsonProperty("con_nom_naissance")
        String conNomNaissance,
        @JsonProperty("con_nom_marital")
        String conNomMarital,
        @JsonProperty("con_prenom")
        String conPrenom,
        @JsonProperty("con_date_naissance")
        LocalDate conDateNaissance
) {}
