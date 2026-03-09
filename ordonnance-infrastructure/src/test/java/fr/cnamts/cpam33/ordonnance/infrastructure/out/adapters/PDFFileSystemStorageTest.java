package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.InfrastructureException;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.storages.PDFFileSystemStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class PDFFileSystemStorageTest {

    @TempDir
    Path tempDir;

    @Test
    void should_save_and_load_pdf_bytes() throws Exception {
        PDFFileSystemStorage storage = new PDFFileSystemStorage(tempDir.toString());
        String key = "folder/doc-1";
        byte[] bytes = "hello".getBytes(StandardCharsets.UTF_8);
        storage.save(key, bytes);
        assertTrue(storage.exists(key));
        byte[] loaded = storage.load(key);
        assertArrayEquals(bytes, loaded);
        assertTrue(Files.exists(tempDir.resolve("folder/doc-1.pdf")));
    }

    @Test
    void should_normalize_backslashes() throws Exception {
        PDFFileSystemStorage storage = new PDFFileSystemStorage(tempDir.toString());
        String key = "a\\b\\c";
        byte[] bytes = "x".getBytes(StandardCharsets.UTF_8);
        storage.save(key, bytes);
        assertTrue(Files.exists(tempDir.resolve("a/b/c.pdf")));
        assertTrue(storage.exists("a/b/c"));
    }

    @Test
    void should_reject_path_traversal() {
        PDFFileSystemStorage storage = new PDFFileSystemStorage(tempDir.toString());
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> storage.save("../secret", new byte[]{1})
        );
        assertTrue(ex.getMessage().contains("path traversal"));
    }

    @Test
    void should_throw_infrastructure_exception_when_load_missing_file() {
        PDFFileSystemStorage storage = new PDFFileSystemStorage(tempDir.toString());
        assertThrows(InfrastructureException.class, () -> storage.load("missing"));
    }

}
