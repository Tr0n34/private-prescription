package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.routes;

import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.api.LocationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Component
public class LocationBuilder {

    private final LocationProperties locationProperties;

    public LocationBuilder(LocationProperties locationProperties) {
        this.locationProperties = locationProperties;
    }

    public URI buildCreatedLocation(String numero) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(locationProperties.pathId())
                .buildAndExpand(numero)
                .toUri();
    }

}
