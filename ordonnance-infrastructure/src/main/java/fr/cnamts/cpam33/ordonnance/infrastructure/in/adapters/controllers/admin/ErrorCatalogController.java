package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.admin;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.errors.ErrorCatalogPersistanceAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/errors")
public class ErrorCatalogController {

    private final ErrorCatalogPersistanceAdapter errorCatalogPersistanceAdapter;

    public ErrorCatalogController(ErrorCatalogPersistanceAdapter errorCatalogPersistanceAdapter) {
        this.errorCatalogPersistanceAdapter = errorCatalogPersistanceAdapter;
    }

}
