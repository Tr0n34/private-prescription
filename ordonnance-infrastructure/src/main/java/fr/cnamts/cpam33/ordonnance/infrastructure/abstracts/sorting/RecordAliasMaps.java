package fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.sorting;


import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class RecordAliasMaps {

    private RecordAliasMaps() {}

    /**
     * Cette méthode construit une <class>Map</class> contenant les alias définit par le fichier
     * <b>YAML</b> ordonnances. Il ignore les champs <code>enabled=false</code>
     * @param cfgByCanonicalField
     * @return
     * @param <C>
     */
    public static <C extends HasAliases & HasEnabled> Map<String, String> buildAliasMap(Map<String, C> cfgByCanonicalField) {
        return cfgByCanonicalField == null
                ? Map.of()
                : cfgByCanonicalField.entrySet().stream()
                .filter(e -> e.getValue() != null && e.getValue().enabled())
                .flatMap(e -> aliasesEntries(e.getKey(), e.getValue()))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static <C extends HasAliases> Stream<Map.Entry<String, String>> aliasesEntries(String canonical, C cfg) {
        return cfg == null || cfg.aliases() == null
                ? Stream.empty()
                : cfg.aliases().stream()
                .filter(a -> a != null && !a.isBlank())
                .map(a -> Map.entry(a.toLowerCase(), canonical));
    }
}