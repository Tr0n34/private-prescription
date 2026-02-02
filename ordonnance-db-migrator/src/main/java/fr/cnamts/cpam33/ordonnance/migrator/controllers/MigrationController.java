package fr.cnamts.cpam33.ordonnance.migrator.controllers;

import fr.cnamts.cpam33.ordonnance.migrator.services.FlywayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/migrations")
public class MigrationController {

    private final FlywayService flywayService;

    public MigrationController(FlywayService flywayService) {
        this.flywayService = flywayService;
    }

    @GetMapping
    public ResponseEntity<?> status() {
        return ResponseEntity.ok(flywayService.statusAll());
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate() {
        return ResponseEntity.ok(flywayService.validateAll());
    }

    @PostMapping("/all")
    public ResponseEntity<?> migrateAll() {
        return ResponseEntity.ok(flywayService.migrateAll());
    }

    @PostMapping("/ordonnance/run")
    public ResponseEntity<?> migrateOrdonnance() {
        return ResponseEntity.ok(flywayService.migrateOrdonnance());
    }

    @PostMapping("/trace/run")
    public ResponseEntity<?> migrateTrace() {
        return ResponseEntity.ok(flywayService.migrateTrace());
    }

}
