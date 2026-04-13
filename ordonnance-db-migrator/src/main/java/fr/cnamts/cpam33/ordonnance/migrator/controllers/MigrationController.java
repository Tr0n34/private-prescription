package fr.cnamts.cpam33.ordonnance.migrator.controllers;

import fr.cnamts.cpam33.ordonnance.migrator.services.FlywayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/migrations")
public class MigrationController {

    private static final Logger logger = LoggerFactory.getLogger(MigrationController.class);

    private final FlywayService flywayService;

    public MigrationController(FlywayService flywayService) {
        this.flywayService = flywayService;
    }

    @GetMapping
    public ResponseEntity<?> status(@RequestParam(name = "onlyImportant", defaultValue = "false") boolean onlyImportant) {
        logger.info("get status with onlyImportant={}", onlyImportant);
        return ResponseEntity.ok(flywayService.statusAll(onlyImportant));
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate() {
        logger.info("validate migrations");
        return ResponseEntity.ok(flywayService.validateAll());
    }

    @PostMapping("/all")
    public ResponseEntity<?> migrateAll(@RequestParam(name = "onlyImportant", defaultValue = "false") boolean onlyImportant) {
        logger.info("migrateAll with onlyImportant={}", onlyImportant);
        return ResponseEntity.ok(flywayService.migrateAll(onlyImportant));
    }

    @PostMapping("/ordonnance/run")
    public ResponseEntity<?> migrateOrdonnance(@RequestParam(name = "onlyImportant", defaultValue = "false") boolean onlyImportant) {
        logger.info("migrate ordonnance with onlyImportant={}", onlyImportant);
        return ResponseEntity.ok(flywayService.migrateOrdonnance(onlyImportant));
    }

    @PostMapping("/trace/run")
    public ResponseEntity<?> migrateTrace(@RequestParam(name = "onlyImportant", defaultValue = "false") boolean onlyImportant) {
        logger.info("migrate trace with onlyImportant={}", onlyImportant);
        return ResponseEntity.ok(flywayService.migrateTrace(onlyImportant));
    }

}
