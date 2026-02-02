package fr.cnamts.cpam33.ordonnance.infrastructure.technical.urls;

import java.nio.file.Path;
import java.util.Objects;

public final class PathJoiner {

    private PathJoiner() {
        throw new UnsupportedOperationException("utility class");
    }


    public static String join(String... parts) {
        if (parts == null || parts.length == 0) return "";

        StringBuilder sb = new StringBuilder(64);
        for (String part : parts) {
            String seg = sanitizeSegment(part);
            if (seg == null) continue;

            if (sb.length() > 0) sb.append('/');
            sb.append(seg);
        }
        return sb.toString();
    }

    public static String joinOrNull(String... parts) {
        String res = join(parts);
        return res.isEmpty() ? null : res;
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

    private static String sanitizeSegment(String part) {
        if (part == null) return null;

        String s = part.trim();
        if (s.isEmpty()) return null;

        // Normalisation slash
        s = s.replace('\\', '/');

        // Trim des '/' en début/fin (sans substring en boucle)
        int start = 0;
        int end = s.length();

        while (start < end && s.charAt(start) == '/') start++;
        while (end > start && s.charAt(end - 1) == '/') end--;

        if (start == end) return null; // segment = "/" ou "////"

        // Optionnel : si tu veux compacter les doubles // internes au segment.
        // (rarement nécessaire, mais safe)
        // return compactSlashes(s.substring(start, end));

        return s.substring(start, end);
    }

    private static String compactSlashes(String s) {
        Objects.requireNonNull(s);
        if (!s.contains("//")) return s;

        StringBuilder out = new StringBuilder(s.length());
        boolean prevSlash = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '/') {
                if (!prevSlash) out.append(c);
                prevSlash = true;
            } else {
                out.append(c);
                prevSlash = false;
            }
        }
        return out.toString();
    }

}
