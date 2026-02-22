package fr.cnamts.cpam33.ordonnance.infrastructure.technical.urls;

import java.util.Optional;

public final class PathJoiner {

    public static final char SLASH = '/';
    public static final int DEFAULT_CAPACITY = 64;

    private PathJoiner() {
        throw new UnsupportedOperationException("utility class");
    }

    public static Optional<String> join(String... parts) {
        Optional<String> result = Optional.empty();
        if ( parts != null ) {
            StringBuilder sb = new StringBuilder(DEFAULT_CAPACITY);
            for ( String part : parts ) {
                sanitizeSegment(part).ifPresent(segment -> {
                    if ( !sb.isEmpty() ) {
                        sb.append(SLASH);
                    }
                    sb.append(segment);
                });
            }
            result = Optional.of(sb.toString());
        }
        return result;
    }

    static Optional<String> sanitizeSegment(String part) {
        return Optional.ofNullable(part)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> s.replace('\\', SLASH))
                .map(s -> s.replaceAll("^/+", "").replaceAll("/+$", ""))
                .filter(s -> !s.isEmpty());
    }

}
