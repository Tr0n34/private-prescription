package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.admin;

import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.BoundedContextHint;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.ErrorDescriptor;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.ErrorCatalogPersistanceAdapter;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.errors.ErrorEntityMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@BoundedContextHint("ERROR_CATALOG")
@RestController
@RequestMapping("/admin/errors")
public class ErrorCatalogController {

    private final ErrorCatalogPersistanceAdapter errorCatalogPersistanceAdapter;
    private final ErrorEntityMapper errorEntityMapper;

    public ErrorCatalogController(ErrorCatalogPersistanceAdapter errorCatalogPersistanceAdapter,
                                  ErrorEntityMapper errorEntityMapper) {
        this.errorCatalogPersistanceAdapter = errorCatalogPersistanceAdapter;
        this.errorEntityMapper = errorEntityMapper;
    }

    @GetMapping
    public ResponseEntity<List<ErrorDescriptor>> getErrorCatalog() {
        List<ErrorDescriptor> errors = errorCatalogPersistanceAdapter.findAll().stream()
                .map(errorEntityMapper::toDescriptor)
                .toList();
        return ResponseEntity.ok(errors);
    }

}
