package fr.cnamts.cpam33.ordonnance.infrastructure.technical.pdf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Objects;

public final class PDFBase64Codec {

    private PDFBase64Codec() {
        throw new UnsupportedOperationException("utility class");
    }

    public static String encode(byte[] pdfBytes) {
        Objects.requireNonNull(pdfBytes, "pdfBytes must not be null");
        return Base64.getEncoder().encodeToString(pdfBytes);
    }

    public static byte[] decode(String base64) {
        Objects.requireNonNull(base64, "base64 must not be null");
        String cleaned = stripDataUrlPrefix(base64.trim());
        return Base64.getDecoder().decode(cleaned);
    }

    public static String encodeFile(Path pdfPath) throws IOException {
        Objects.requireNonNull(pdfPath, "pdfPath must not be null");
        return encode(Files.readAllBytes(pdfPath));
    }

    public static void decodeToFile(String base64, Path outputPdfPath) throws IOException {
        Objects.requireNonNull(outputPdfPath, "outputPdfPath must not be null");
        byte[] bytes = decode(base64);

        Path parent = outputPdfPath.getParent();
        if (parent != null) Files.createDirectories(parent);

        Files.write(outputPdfPath, bytes);
    }

    private static String stripDataUrlPrefix(String s) {
        int comma = s.indexOf(',');
        if (comma > 0 && s.regionMatches(true, 0, "data:", 0, 5)) {
            return s.substring(comma + 1).trim();
        }
        return s;
    }
}
