package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.routes;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Component
public class LocationBuilder {

    public static final String PATH_ID = "/{id}";

    public URI buildCreatedLocation(String numero) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(PATH_ID)
                .buildAndExpand(numero)
                .toUri();
    }

}
