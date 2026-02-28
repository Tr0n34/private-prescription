package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.storages;

import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.enums.PdfExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.InfrastructureError;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.InfrastructureException;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.pdf.PDFStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Component
public class PDFFileSystemStorage implements PDFStorage{

    private static final Logger logger = LoggerFactory.getLogger(PDFFileSystemStorage.class);

    public static final String ERROR_PDF_KEY = "pdf_key";
    public static final char SLASH = '/';
    public static final String PDF_EXTENSION = ".pdf";

    private final Path rootDirectory;
    private Map<String, Object> errorPlaceHolders;

    public PDFFileSystemStorage(@Value("ordonnance.repositories.storage") String rootDirectory) {
        InfrastructureError.requireNotNull(rootDirectory,
                cause -> new InfrastructureException(PdfExceptionCode.TECH_PDF_ROOT_DIR_EMPTY, errorPlaceHolders));
        this.rootDirectory = Path.of(rootDirectory);
        logger.debug("PDF storage root directory: {}", this.rootDirectory);
        errorPlaceHolders = new HashMap<>();
        errorPlaceHolders.put(ERROR_PDF_KEY, new HashMap<>());
    }

    @Override
    public void save(String key, byte[] pdfBytes) throws InfrastructureException {
        this.errorPlaceHolders.put(ERROR_PDF_KEY, key);
        InfrastructureError.requireNotBlank(key, cause -> new InfrastructureException(PdfExceptionCode.TECH_PDF_KEY_INVALID, errorPlaceHolders));
        InfrastructureError.requireNotNull(pdfBytes, cause -> new InfrastructureException(PdfExceptionCode.TECH_PDF_KEY_INVALID, errorPlaceHolders));
        Path path = resolveKeyToPath(key);
        FileBytesWriter.write(
                path,
                pdfBytes,
                cause -> new InfrastructureException(PdfExceptionCode.TECH_PDF_DIRECTORY_CREATE_ERROR, errorPlaceHolders),
                cause -> new InfrastructureException(PdfExceptionCode.TECH_PDF_WRITE_ERROR, errorPlaceHolders)
        );
    }

    @Override
    public byte[] load(String key) throws InfrastructureException {
        this.errorPlaceHolders.put(ERROR_PDF_KEY, key);
        String safeKey = InfrastructureError.requireNotBlank(
                key,
                cause -> new InfrastructureException(PdfExceptionCode.TECH_PDF_KEY_INVALID, errorPlaceHolders)
        );
        Path path = resolveKeyToPath(safeKey);
        return InfrastructureError.io(
                () -> Files.readAllBytes(path),
                cause -> new InfrastructureException(PdfExceptionCode.TECH_PDF_READ_ERROR, errorPlaceHolders)
        );
    }

    @Override
    public boolean exists(String key) throws InfrastructureException {
        this.errorPlaceHolders.put(ERROR_PDF_KEY, key);
        String safeKey = InfrastructureError.requireNotBlank(
                key,
                cause -> new InfrastructureException(PdfExceptionCode.TECH_PDF_KEY_INVALID)
        );
        return Files.exists(resolveKeyToPath(safeKey));
    }

    private Path resolveKeyToPath(String key) {
        String safe = key.replace('\\', SLASH);
        while ( safe.startsWith(String.valueOf(SLASH)) ) {
            safe = safe.substring(1);
        }
        if ( safe.contains("..") ) {
            throw new IllegalArgumentException("Invalid key (path traversal): " + key);
        }
        if ( !safe.endsWith(PDF_EXTENSION) ) {
            safe = safe + PDF_EXTENSION;
        }
        return rootDirectory.resolve(safe);
    }

}
