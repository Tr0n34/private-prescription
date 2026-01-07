package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.providers.ErrorCatalogWatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/errors/watcher")
public class ErrorCatalogWatcherController {

    private static final Logger logger = LoggerFactory.getLogger(ErrorCatalogWatcherController.class);

    private final ErrorCatalogWatchService watcher;

    public ErrorCatalogWatcherController(ErrorCatalogWatchService watcher) {
        this.watcher = watcher;
    }

    @PostMapping("/start")
    public ResponseEntity<Void> start() {
        watcher.startWatching();
        logger.info("Watcher started via admin endpoint");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/stop")
    public ResponseEntity<Void> stop() {
        watcher.stopWatching();
        logger.info("Watcher stopped via admin endpoint");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> status() {
        return ResponseEntity.ok(watcher.isRunning());
    }

}
