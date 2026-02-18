package fr.cnamts.cpam33.ordonnance.domain.kernel.traces;

public enum TraceMaskMode {
    FULL,          // tout masquer => "***"
    PREFIX,        // garder N chars au début
    SUFFIX,        // garder N chars à la fin
    EMAIL,         // john.doe@x.com => j***@x.com (ex)
    HASH_SHA256,   // hash stable (attention sel si besoin)
    LAST4          // alias SUFFIX 4
}
