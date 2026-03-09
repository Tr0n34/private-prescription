package fr.cnamts.cpam33.ordonnance.domain.abstracts.domain;

import java.util.Map;

public interface Cache<T extends DomainObject> {

    boolean isReady();

    void refresh();

    Map<String, T> snapshot();

    T getRequired(String code);

    int size();

}
