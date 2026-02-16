package fr.cnamts.cpam33.ordonnance.infrastructure.kernel.caches;

import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.ActeMetier;

import java.util.Map;

public interface ActeMetierCache {

    boolean isReady();

    void refresh();

    Map<String, ActeMetier> snapshot();

    ActeMetier getRequired(String code);

    int size();

}