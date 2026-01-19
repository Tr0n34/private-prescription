package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers;

import fr.cnamts.cpam33.ordonnance.application.usecases.ProvidePatientUseCase;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.Adapter;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.ExternalPatientDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers.PatientApiMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController implements Adapter {

    private final ProvidePatientUseCase patientService;
    private final PatientApiMapper patientApiMapper;

    public PatientController(ProvidePatientUseCase patientService, PatientApiMapper patientApiMapper) {
        this.patientService = patientService;
        this.patientApiMapper = patientApiMapper;
    }

    @PostMapping
    public ResponseEntity<Void> providePatient(@Valid @RequestBody ExternalPatientDto patientDto) {
        patientService.providePatient(patientApiMapper.toDomain(patientDto));
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/batch")
    public ResponseEntity<Void> providePatientBatch(@RequestBody List<ExternalPatientDto> patientDtos) {
        patientDtos.forEach(patientDto -> { patientService.providePatient(patientApiMapper.toDomain(patientDto));});
        return ResponseEntity.ok().build();
    }

}
