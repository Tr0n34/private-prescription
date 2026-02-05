package fr.cnamts.cpam33.ordonnance.infrastructure.technical.pdf;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.InfrastructureException;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.OpsError;
import fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.enums.PdfExceptionCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Map;

public final class PDFBase64Codec {

    public static final char COMMA = ',';
    public static final String ERROR_PDF = "pdf";

    private PDFBase64Codec() {
        throw new UnsupportedOperationException("utility class");
    }

    public static String encode(byte[] pdfBytes) {
        OpsError.requireNotNull(pdfBytes,
                cause -> new InfrastructureException(PdfExceptionCode.TECH_PDF_EMPTY));
        return Base64.getEncoder().encodeToString(pdfBytes);
    }

    public static byte[] decode(String base64) {
        OpsError.requireNotNull(base64,
                cause -> new InfrastructureException(PdfExceptionCode.TECH_PDF_BASE64_EMPTY));
        String cleaned = stripDataUrlPrefix(base64.trim());
        return Base64.getDecoder().decode(cleaned);
    }

    public static String encodeFile(Path pdfPath) throws IOException {
        OpsError.requireNotNull(pdfPath,
                cause -> new InfrastructureException(
                        PdfExceptionCode.TECH_PDF_PATH_INVALID,
                        Map.of(ERROR_PDF, pdfPath)));
        return encode(Files.readAllBytes(pdfPath));
    }

    public static void decodeToFile(String base64, Path outputPdfPath) throws IOException {
        OpsError.requireNotNull(outputPdfPath,
                cause -> new InfrastructureException(
                        PdfExceptionCode.TECH_PDF_DIRECTORY_NOT_FOUND,
                        Map.of(ERROR_PDF, outputPdfPath)));
        byte[] bytes = decode(base64);
        Path parent = outputPdfPath.getParent();
        if ( parent != null ) Files.createDirectories(parent);
        Files.write(outputPdfPath, bytes);
    }

    private static String stripDataUrlPrefix(String s) {
        int comma = s.indexOf(COMMA);
        if ( comma > 0 && s.regionMatches(true, 0, "data:", 0, 5)) {
            return s.substring(comma + 1).trim();
        }
        return s;
    }
}
