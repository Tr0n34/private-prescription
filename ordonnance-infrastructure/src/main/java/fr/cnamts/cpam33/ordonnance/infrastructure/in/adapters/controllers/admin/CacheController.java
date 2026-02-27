package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.admin;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.ActeMetier;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.caches.ActeMetierCache;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.caches.ErrorCatalogCache;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.BoundedContextHint;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.ErrorCatalogEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@BoundedContextHint("CACHE")
@RestController
@RequestMapping("/admin/caches")
public class CacheController {

    private static final Logger logger = LoggerFactory.getLogger(CacheController.class);

    private final ActeMetierCache acteMetierCache;
    private final ErrorCatalogCache errorCatalogCache;

    public CacheController(ActeMetierCache acteMetierCache, ErrorCatalogCache errorCatalogCache) {
        this.acteMetierCache = acteMetierCache;
        this.errorCatalogCache = errorCatalogCache;
    }

    @GetMapping("/acteMetiers")
    public ResponseEntity<?> getActeMetiers() {
        return ResponseEntity.ok().body(acteMetierCache.snapshot());
    }

    @GetMapping("/acteMetiers/{code}")
    public ResponseEntity<ActeMetier> getActeMetier(@PathVariable("code") String code) {
        return ResponseEntity.ok().body(acteMetierCache.getRequired(code));
    }

    @GetMapping("/errors")
    public ResponseEntity<?> getErrors() {
        return ResponseEntity.ok().body(errorCatalogCache.snapshot());
    }

    @GetMapping("/errors/{code}")
    public ResponseEntity<ErrorCatalogEntity> getError(@PathVariable("code") String code) {
        return ResponseEntity.ok().body(errorCatalogCache.getRequired(code));
    }

}
