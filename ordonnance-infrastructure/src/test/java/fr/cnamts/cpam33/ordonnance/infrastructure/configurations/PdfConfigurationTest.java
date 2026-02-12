package fr.cnamts.cpam33.ordonnance.infrastructure.configurations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class PdfConfigurationTest {

    private PdfConfiguration pdfConfiguration;

    @BeforeEach
    void setUp() {
        pdfConfiguration = new PdfConfiguration("file:src/test/resources/templates");
    }

    @Test
    void testFileDirectoryResolution() {
        String resolvedPath = pdfConfiguration.resolveFileDirectory(pdfConfiguration.getTemplateDirectory());
        assertNotNull(resolvedPath, "Resolved path should not be null");
        assertTrue(resolvedPath.endsWith("/"), "Path should end with '/'");
        File dir = new File(resolvedPath);
        assertTrue(Files.exists(dir.toPath()), "Resolved directory must exist");
    }

    @Test
    void testFileTemplateResolverCreation() {
        FileTemplateResolver resolver = pdfConfiguration.fileTemplateResolver();
        assertNotNull(resolver, "FileTemplateResolver should not be null");
        assertEquals(".html", resolver.getSuffix(), "Suffix should be .html");
        assertEquals("HTML", resolver.getTemplateMode().name(), "Template mode should be HTML");
        assertEquals(pdfConfiguration.resolveFileDirectory(pdfConfiguration.getTemplateDirectory()), resolver.getPrefix());
    }

    @Test
    void testClassLoaderTemplateResolverCreation() {
        ClassLoaderTemplateResolver resolver = pdfConfiguration.classpathTemplateResolver();
        assertNotNull(resolver, "ClassLoaderTemplateResolver should not be null");
        assertEquals(PdfConfiguration.DEFAULT_TEMPLATE_DIRECTORY, resolver.getPrefix());
        assertEquals(".html", resolver.getSuffix());
        assertEquals("HTML", resolver.getTemplateMode().name());
    }

    @Test
    void testTemplateEngineCreation() {
        SpringTemplateEngine engine = pdfConfiguration.templateEngine();
        assertNotNull(engine, "SpringTemplateEngine should not be null");
        boolean hasClassLoader = engine.getTemplateResolvers().stream().anyMatch(ClassLoaderTemplateResolver.class::isInstance);
        boolean hasFile = engine.getTemplateResolvers().stream().anyMatch(FileTemplateResolver.class::isInstance);
        assertTrue(hasClassLoader, "Template engine should have ClassLoaderTemplateResolver");
        assertTrue(hasFile, "Template engine should have FileTemplateResolver");
    }

}