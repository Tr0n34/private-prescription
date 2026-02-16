package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters;

import fr.cnamts.cpam33.ordonnance.infrastructure.kernel.pdf.PDFStorage;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.storages.PDFFileSystemStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PDFFileSystemStorageIT {

    @TempDir
    Path tempDir;

    @Test
    void should_create_bean_and_store_pdf_on_filesystem() {
        new ApplicationContextRunner()
                .withBean(PDFFileSystemStorage.class)
                .withPropertyValues("ordonnance.repositories.storage=" + tempDir.toString())
                .run(ctx -> {
                    PDFStorage storage = ctx.getBean(PDFStorage.class); // ou PDFFileSystemStorage.class
                    String key = "ti/doc";
                    byte[] bytes = "from-ti".getBytes(StandardCharsets.UTF_8);
                    storage.save(key, bytes);
                    assertTrue(storage.exists(key));
                    assertArrayEquals(bytes, storage.load(key));
                });
    }

}