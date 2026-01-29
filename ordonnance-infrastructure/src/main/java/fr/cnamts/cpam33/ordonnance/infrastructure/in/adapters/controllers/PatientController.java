package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers;

import fr.cnamts.cpam33.ordonnance.application.usecases.patients.ImportPatientUseCase;
import fr.cnamts.cpam33.ordonnance.application.usecases.patients.RegisterPatientUseCase;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.Adapter;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers.ImportPatientApiMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers.PatientApiMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.ExternalPatientDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.ImportPatientDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController implements Adapter {

    private final RegisterPatientUseCase patientService;
    private final PatientApiMapper patientApiMapper;

    public PatientController(RegisterPatientUseCase patientService,
                             PatientApiMapper patientApiMapper) {
        this.patientService = patientService;
        this.patientApiMapper = patientApiMapper;
    }

    @PostMapping
    public ResponseEntity<Void> providePatient(@Valid @RequestBody ExternalPatientDto patientDto) {
        patientService.registerPatient(patientApiMapper.toDomain(patientDto));
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/batch")
    public ResponseEntity<Void> providePatientBatch(@RequestBody List<ExternalPatientDto> patientDtos) {
        patientDtos.forEach(patientDto -> {
            patientService.registerPatient(patientApiMapper.toDomain(patientDto));
        });
        return ResponseEntity.ok().build();
    }

    @PostMapping("/import")
    public ResponseEntity<Void> importFromExternal(@PathVariable("patientId") String patientId) {
        return ResponseEntity.ok().build();
    }

}
