package fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.caches;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.ActeMetier;

import java.util.Map;

public interface ActeMetierCache {

    boolean isReady();

    void refresh();

    Map<String, ActeMetier> snapshot();

    ActeMetier getRequired(String code);

    int size();

}