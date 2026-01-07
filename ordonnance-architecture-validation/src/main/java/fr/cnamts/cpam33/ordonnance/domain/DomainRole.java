package fr.cnamts.cpam33.ordonnance.domain;

public enum DomainRole {

    BUSINESS_OBJECT(
            "Business Object",
            "Un Business Object doit obligatoirement exposer une factory"
    ),
    AGGREGATE_ROOT(
            "Aggregate Root",
            "Un Aggregate Root doit obligatoirement être créé via une factory"
    ),
    DOMAIN_OBJECT(
            "Objet métier",
            "Implémente DomainObject mais n'expose pas de factory"
    );

    final String label;
    final String rule;

    DomainRole(String label, String rule) {
        this.label = label;
        this.rule = rule;
    }

}