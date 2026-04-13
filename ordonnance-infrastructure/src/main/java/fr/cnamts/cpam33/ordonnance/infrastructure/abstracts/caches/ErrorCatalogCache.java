package fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.caches;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.ErrorCatalogEntity;

import java.util.Map;

public interface ErrorCatalogCache {

    boolean isReady();

    void refresh();

    Map<String, ErrorCatalogEntity> snapshot();

    ErrorCatalogEntity getRequired(String code);

    int size();

}
