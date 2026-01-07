package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers;

import fr.cnamts.cpam33.ordonnance.application.usecases.CreateOrdonnanceUseCase;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.CreateOrdonnanceCmd;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.OrdonnanceDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.PrescriptionDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers.OrdonnanceApiMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordonnances")
public class OrdonnanceController {

    private CreateOrdonnanceUseCase createOrdonnanceUseCase;
    private OrdonnanceApiMapper ordonnanceApiMapper;

    public OrdonnanceController(CreateOrdonnanceUseCase createOrdonnanceUseCase, OrdonnanceApiMapper ordonnanceApiMapper) {
        this.createOrdonnanceUseCase = createOrdonnanceUseCase;
        this.ordonnanceApiMapper = ordonnanceApiMapper;
    }

    @PostMapping
    public ResponseEntity<Void> createEmptyOrdonnance(@Valid @RequestBody OrdonnanceDto ordonnanceDto) {
        CreateOrdonnanceCmd cmd = ordonnanceApiMapper.toCommand(ordonnanceDto);
        createOrdonnanceUseCase.execute(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{id}/prescriptions")
    public ResponseEntity<Void> addPrescriptions(
            @PathVariable("id") String ordonnanceId,
            @RequestBody List<PrescriptionDto> prescriptionDtos) {

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrdonnance(@PathVariable("id")String ordonnanceId) {
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateOrdonnance(OrdonnanceDto ordonnanceDto) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("{id}/sign")
    public ResponseEntity<Void>  sign(@PathVariable("id") String ordonnanceId) {
        return ResponseEntity.noContent().build();
    }

}
