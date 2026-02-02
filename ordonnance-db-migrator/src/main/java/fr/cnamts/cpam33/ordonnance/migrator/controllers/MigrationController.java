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
    public ResponseEntity<?> status(@RequestParam(name = "onlyImportant", defaultValue = "false") boolean onlyImportant) {
        return ResponseEntity.ok(flywayService.statusAll(onlyImportant));
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate() {
        return ResponseEntity.ok(flywayService.validateAll());
    }

    @PostMapping("/all")
    public ResponseEntity<?> migrateAll(@RequestParam(name = "onlyImportant", defaultValue = "false") boolean onlyImportant) {
        return ResponseEntity.ok(flywayService.migrateAll(onlyImportant));
    }

    @PostMapping("/ordonnance/run")
    public ResponseEntity<?> migrateOrdonnance(@RequestParam(name = "onlyImportant", defaultValue = "false") boolean onlyImportant) {
        return ResponseEntity.ok(flywayService.migrateOrdonnance(onlyImportant));
    }

    @PostMapping("/trace/run")
    public ResponseEntity<?> migrateTrace(@RequestParam(name = "onlyImportant", defaultValue = "false") boolean onlyImportant) {
        return ResponseEntity.ok(flywayService.migrateTrace(onlyImportant));
    }

}
