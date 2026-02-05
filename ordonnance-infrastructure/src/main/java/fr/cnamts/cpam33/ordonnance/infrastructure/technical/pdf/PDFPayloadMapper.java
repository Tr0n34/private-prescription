package fr.cnamts.cpam33.ordonnance.infrastructure.technical.pdf;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.PDFPayload;

import java.util.Objects;

public final class PDFPayloadMapper {

    private PDFPayloadMapper() {
        throw new UnsupportedOperationException("utility class");
    }

    public static byte[] toBytes(PDFPayload payload) {
        Objects.requireNonNull(payload, "payload must not be null");
        return PDFBase64Codec.decode(payload.base64());
    }

    public static PDFPayload fromBytes(String filename, byte[] pdfBytes) {
        Objects.requireNonNull(filename, "filename must not be null");
        Objects.requireNonNull(pdfBytes, "pdfBytes must not be null");
        return new PDFPayload(filename, "application/pdf", PDFBase64Codec.encode(pdfBytes));
    }

}
