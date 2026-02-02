package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers;

import fr.cnamts.cpam33.ordonnance.application.usecases.patients.RegisterPatientUseCase;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.Adapter;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.acls.patients.PatientACL;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.routes.LocationBuilder;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.patients.CesPatientDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers.PatientApiMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.PatientDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController implements Adapter {

    private final LocationBuilder locationBuilder;
    private final RegisterPatientUseCase registerPatientUseCase;
    private final PatientACL patientACL;

    public PatientController(LocationBuilder locationBuilder,
                             RegisterPatientUseCase registerPatientUseCase,
                             PatientACL patientACL) {
        this.locationBuilder = locationBuilder;
        this.registerPatientUseCase = registerPatientUseCase;
        this.patientACL = patientACL;
    }

    @PostMapping
    public ResponseEntity<Void> createPatient(@Valid @RequestBody PatientDto patientDto) {
        Patient patient = registerPatientUseCase.registerPatient(patientACL.toDomain(patientDto));
        URI location = locationBuilder.buildCreatedLocation(patient.patientId().numero());
        return ResponseEntity.created(location).build();
    }

    @PostMapping(path = "/batch")
    public ResponseEntity<Void> createPatients(@RequestBody List<PatientDto> patientDtos) {
        patientDtos.forEach(patientDto -> {
            registerPatientUseCase.registerPatient(patientACL.toDomain(patientDto));
        });
        return ResponseEntity.ok().build();
    }

    @PostMapping("/import")
    public ResponseEntity<Void> importFromExternal(CesPatientDto cesPatientDto) {
        patientACL.toDomain(cesPatientDto);
        return ResponseEntity.ok().build();
    }

}
