package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers;

import fr.cnamts.cpam33.ordonnance.application.usecases.patients.ImportPatientUseCase;
import fr.cnamts.cpam33.ordonnance.application.usecases.patients.RegisterPatientUseCase;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.BoundedContextHint;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.acls.patients.PatientACL;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.routes.LocationBuilder;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.routes.Routes;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.Adapter;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.patients.PatientDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@BoundedContextHint("PATIENT")
@RestController
@RequestMapping("/patients")
public class PatientController implements Adapter {

    private static final Logger logger = LoggerFactory.getLogger(PatientController.class.getName());

    private final LocationBuilder locationBuilder;
    private final RegisterPatientUseCase registerPatientUseCase;
    private final ImportPatientUseCase importPatientUseCase;
    private final PatientACL patientACL;

    public PatientController(LocationBuilder locationBuilder,
                             RegisterPatientUseCase registerPatientUseCase,
                             ImportPatientUseCase importPatientUseCase,
                             PatientACL patientACL) {
        this.locationBuilder = locationBuilder;
        this.registerPatientUseCase = registerPatientUseCase;
        this.importPatientUseCase = importPatientUseCase;
        this.patientACL = patientACL;
    }

    @PostMapping
    public ResponseEntity<Void> createPatient(
            @Valid @RequestBody PatientDto patientDto,
            @RequestHeader("userId")  String userId) {
        Patient patient = registerPatientUseCase.registerPatient(patientACL.toDomain(patientDto, userId));
        URI location = locationBuilder.buildCreatedLocation(patient.patientId().numero());
        return ResponseEntity.created(location).build();
    }

    @PostMapping(path = "/batch")
    public ResponseEntity<Void> createPatients(
            @RequestBody List<PatientDto> patientDtos,
            @RequestHeader("userId")  String userId) {
        logger.trace("createPatients mode Batch");
        patientDtos.forEach(
                patientDto -> registerPatientUseCase.registerPatient(patientACL.toDomain(patientDto, userId))
        );
        return ResponseEntity.ok().build();
    }

    @PostMapping(Routes.Patient.IMPORT_BY_EXTERNAL_ID)
    public ResponseEntity<Void> importFromExternal(@PathVariable("externalId") String externalId, @RequestHeader("userId")  String userId) {
        Patient importedPatient = importPatientUseCase.execute(patientACL.toDomain(externalId, userId));
        URI location = locationBuilder.buildCreatedLocation(importedPatient.patientId().numero());
        return ResponseEntity.created(location).build();
    }

}
