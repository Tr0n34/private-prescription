package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.storages;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.InfrastructureException;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.OpsError;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.pdf.PDFStorage;
import fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.enums.PdfExceptionCode;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class PDFFileSystemStorage implements PDFStorage {

    public static final String ERROR_PDF_KEY = "pdf_key";
    public static final String ERROR_PDF = "pdf";
    public static final char SLASH = '/';
    public static final String PDF_EXTENSION = ".pdf";

    private final Path rootDir;
    private Map<String, Object> errorPlaceHolders;

    public PDFFileSystemStorage(Path rootDir, Map<String, Object> errorPlaceHolders) {
        OpsError.requireNotNull(rootDir,
                cause -> new InfrastructureException(PdfExceptionCode.TECH_PDF_ROOT_DIR_EMPTY, errorPlaceHolders));
        this.rootDir = rootDir;
        errorPlaceHolders.put(ERROR_PDF, rootDir);
    }

    @Override
    public void save(String key, byte[] pdfBytes) throws InfrastructureException {
        errorPlaceHolders.put(ERROR_PDF_KEY, key);
        OpsError.requireNotBlank(key, cause -> new InfrastructureException(PdfExceptionCode.TECH_PDF_KEY_INVALID, errorPlaceHolders));
        OpsError.requireNotNull(pdfBytes, cause -> new InfrastructureException(PdfExceptionCode.TECH_PDF_KEY_INVALID, errorPlaceHolders));
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
        errorPlaceHolders.put(ERROR_PDF_KEY, key);
        String safeKey = OpsError.requireNotBlank(
                key,
                cause -> new InfrastructureException(PdfExceptionCode.TECH_PDF_KEY_INVALID, errorPlaceHolders)
        );
        Path path = resolveKeyToPath(safeKey);
        return OpsError.io(
                () -> Files.readAllBytes(path),
                cause -> new InfrastructureException(PdfExceptionCode.TECH_PDF_READ_ERROR, errorPlaceHolders)
        );
    }

    @Override
    public boolean exists(String key) throws InfrastructureException {
        errorPlaceHolders.put(ERROR_PDF_KEY, key);
        String safeKey = OpsError.requireNotBlank(
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
        return rootDir.resolve(safe);
    }

}
