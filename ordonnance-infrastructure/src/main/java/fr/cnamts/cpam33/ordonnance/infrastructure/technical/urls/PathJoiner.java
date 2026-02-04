package fr.cnamts.cpam33.ordonnance.infrastructure.technical.urls;

import java.util.Optional;

public final class PathJoiner {

    public static final char SLASH = '/';
    public static final String DOUBLE_SLASH = "//";
    public static final int DEFAULT_CAPACITY = 64;

    private PathJoiner() {
        throw new UnsupportedOperationException("utility class");
    }

    public static Optional<String> join(String... parts) {
        Optional<String> result = Optional.empty();
        if ( parts != null ) {
            StringBuilder sb = new StringBuilder(DEFAULT_CAPACITY);
            for ( String part : parts ) {
                Optional<String> opt = sanitizeSegment(part);
                if ( opt.isEmpty() ) continue;
                if ( !sb.isEmpty() ) sb.append(SLASH);
                sb.append(opt.get());
            }
            result = Optional.of(sb.toString());
        }
        return result;
    }

    public static String ensureLeadingSlash(String path) {
        if (path == null || path.isBlank()) return "/";
        String s = path.trim().replace('\\', '/');
        return s.startsWith("/") ? s : "/" + s;
    }

    public static String ensureNoLeadingSlash(String path) {
        if (path == null) return null;
        String s = path.trim().replace('\\', '/');
        while (s.startsWith("/")) s = s.substring(1);
        return s;
    }

    static Optional<String> sanitizeSegment(String part) {
        return Optional.ofNullable(part)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> s.replace('\\', SLASH))
                .map(s -> {
                    int start = 0, end = s.length();
                    while (start < end && s.charAt(start) == SLASH) start++;
                    while (end > start && s.charAt(end - 1) == SLASH) end--;
                    return start < end ? s.substring(start, end) : null;
                });
    }

    private static Optional<String> compactSlashesOpt(String input) {
        return Optional.ofNullable(input)
                .map(s -> {
                    if (!s.contains(DOUBLE_SLASH)) return s;
                    StringBuilder out = new StringBuilder(s.length());
                    boolean prevSlash = false;
                    for ( int i = 0, len = s.length(); i < len; i++ ) {
                        char c = s.charAt(i);
                        if (c == SLASH) {
                            if ( !prevSlash ) out.append(c);
                            prevSlash = true;
                        } else {
                            out.append(c);
                            prevSlash = false;
                        }
                    }
                    return out.toString();
                });
    }

}
