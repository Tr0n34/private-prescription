package fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.caches;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.ActeMetierEntity;

import java.util.Map;

public interface ActeMetierCache {

    boolean isReady();

    void refresh();

    Map<String, ActeMetierEntity> snapshot();

    ActeMetierEntity getRequired(String code);

    int size();

}