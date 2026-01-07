package fr.cnamts.cpam33.ordonnance.infrastructure.configurations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PdfConfigurationTest {

    public static final String RESOLVE_TEMPLATE_PREFIX = "resolveTemplatePrefix";
    public static final String NORMALIZE_PATH = "normalizePath";
    public static final String CLASSPATH_TEMPLATES = "classpath:templates";
    public static final String TEMPLATE_DIRECTORY = "templateDirectory";
    public static final String TEMPLATES = "templates";
    public static final String RESOLVE_CLASSPATH_RESOURCE = "resolveClasspathResource";
    public static final String RESOLVE_FILE_RESOURCE = "resolveFileResource";
    public static final String SUFFIX = "/";

    private PdfConfiguration configuration;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        configuration = new PdfConfiguration();
    }

    @Test
    void templateEngine_shouldCreateValidEngine() {
        ReflectionTestUtils.setField(configuration, TEMPLATE_DIRECTORY, CLASSPATH_TEMPLATES);
        SpringTemplateEngine engine = configuration.templateEngine();
        assertThat(engine).isNotNull();
        assertThat(engine.getTemplateResolvers()).hasSize(1);
    }

    @Test
    void resolveTemplatePrefix_withClasspathPrefix_shouldResolveCorrectly() {
        ReflectionTestUtils.setField(configuration, TEMPLATE_DIRECTORY, CLASSPATH_TEMPLATES);
        String result = ReflectionTestUtils.invokeMethod(configuration, RESOLVE_TEMPLATE_PREFIX);
        assertThat(result).isNotNull();
        assertThat(result).endsWith(SUFFIX);
        assertThat(result).contains(TEMPLATES);
    }

    @Test
    void resolveTemplatePrefix_withClasspathPrefixAndLeadingSlash_shouldResolveCorrectly() {
        ReflectionTestUtils.setField(configuration, TEMPLATE_DIRECTORY, "classpath:/templates");
        String result = ReflectionTestUtils.invokeMethod(configuration, RESOLVE_TEMPLATE_PREFIX);
        assertThat(result).isNotNull();
        assertThat(result).endsWith(SUFFIX);
    }

    @Test
    void resolveTemplatePrefix_withNonExistentClasspath_shouldThrowException() {
        ReflectionTestUtils.setField(configuration, TEMPLATE_DIRECTORY, "classpath:nonexistent");
        assertThatThrownBy(() ->
                ReflectionTestUtils.invokeMethod(configuration, RESOLVE_TEMPLATE_PREFIX))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Template directory not found in classpath");
    }

    @Test
    void resolveTemplatePrefix_withFilePrefix_shouldResolveCorrectly() throws IOException {
        Path templatePath = tempDir.resolve(TEMPLATES);
        Files.createDirectories(templatePath);
        String fileUrl = "file:" + templatePath.toString();
        ReflectionTestUtils.setField(configuration, TEMPLATE_DIRECTORY, fileUrl);
        String result = ReflectionTestUtils.invokeMethod(configuration, RESOLVE_TEMPLATE_PREFIX);
        assertThat(result).isNotNull();
        assertThat(result).endsWith(SUFFIX);
    }

    @Test
    void resolveTemplatePrefix_withNonExistentFilePath_shouldThrowException() {
        String nonExistentPath = "file:" + tempDir.resolve("nonexistent").toString();
        ReflectionTestUtils.setField(configuration, TEMPLATE_DIRECTORY, nonExistentPath);
        assertThatThrownBy(() ->
                ReflectionTestUtils.invokeMethod(configuration, RESOLVE_TEMPLATE_PREFIX))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Template directory not found");
    }

    @Test
    void resolveTemplatePrefix_withPlainPath_shouldNormalizeCorrectly() throws IOException {
        Path templatePath = tempDir.resolve(TEMPLATES);
        Files.createDirectories(templatePath);
        ReflectionTestUtils.setField(configuration, TEMPLATE_DIRECTORY, templatePath.toString());
        String result = ReflectionTestUtils.invokeMethod(configuration, RESOLVE_TEMPLATE_PREFIX);
        assertThat(result).isNotNull();
        assertThat(result).endsWith(SUFFIX);
        assertThat(result).doesNotContain("\\");
    }

    @Test
    void normalizePath_withBackslashes_shouldReplaceWithForwardSlashes() {
        String pathWithBackslashes = "C:\\Users\\test\\templates";
        String result = ReflectionTestUtils.invokeMethod(configuration, NORMALIZE_PATH, pathWithBackslashes);
        assertThat(result).isEqualTo("C:/Users/test/templates/");
        assertThat(result).doesNotContain("\\");
    }

    @Test
    void normalizePath_withoutTrailingSlash_shouldAddSlash() {
        String pathWithoutSlash = "templates/test";
        String result = ReflectionTestUtils.invokeMethod(configuration, NORMALIZE_PATH, pathWithoutSlash);
        assertThat(result).isEqualTo("templates/test/");
    }

    @Test
    void normalizePath_withTrailingSlash_shouldNotAddExtraSlash() {
        String pathWithSlash = "templates/test/";
        String result = ReflectionTestUtils.invokeMethod(configuration, NORMALIZE_PATH, pathWithSlash);
        assertThat(result).isEqualTo("templates/test/");
    }

    @Test
    void normalizePath_withMixedSlashes_shouldNormalizeAll() {
        String mixedPath = "C:\\Users/test\\templates/";
        String result = ReflectionTestUtils.invokeMethod(configuration, NORMALIZE_PATH, mixedPath);
        assertThat(result).isEqualTo("C:/Users/test/templates/");
        assertThat(result).doesNotContain("\\");
    }

    @Test
    void resolveClasspathResource_withValidPath_shouldResolveCorrectly() {
        String classpathPath = CLASSPATH_TEMPLATES;
        ReflectionTestUtils.setField(configuration, TEMPLATE_DIRECTORY, classpathPath);
        String result = ReflectionTestUtils.invokeMethod(configuration, RESOLVE_CLASSPATH_RESOURCE, classpathPath);
        assertThat(result).isNotNull();
        assertThat(result).endsWith(SUFFIX);
        assertThat(result).contains(TEMPLATES);
    }

    @Test
    void resolveFileResource_withInvalidPath_shouldThrowException() {
        String invalidPath = "file:\0invalid";
        assertThatThrownBy(
                () -> ReflectionTestUtils.invokeMethod(configuration, RESOLVE_FILE_RESOURCE, invalidPath))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Invalid file path");
    }

    @Test
    void createTemplateResolver_shouldConfigureResolverCorrectly() throws IOException {
        Path templatePath = tempDir.resolve(TEMPLATES);
        Files.createDirectories(templatePath);
        ReflectionTestUtils.setField(configuration, TEMPLATE_DIRECTORY, templatePath.toString());
        FileTemplateResolver resolver = ReflectionTestUtils.invokeMethod(configuration, "createTemplateResolver");
        assertThat(resolver).isNotNull();
        assertThat(resolver.getSuffix()).isEqualTo(".html");
        assertThat(resolver.getCheckExistence()).isTrue();
        assertThat(resolver.isCacheable()).isFalse();
    }

    @Test
    void resolveTemplatePrefix_withWindowsPath_shouldHandleCorrectly() throws IOException {
        Path templatePath = tempDir.resolve(TEMPLATES);
        Files.createDirectories(templatePath);
        String fileUrl = "file:" + templatePath.toAbsolutePath().toString().replace("\\", "/");
        ReflectionTestUtils.setField(configuration, TEMPLATE_DIRECTORY, fileUrl);
        String result = ReflectionTestUtils.invokeMethod(configuration, RESOLVE_TEMPLATE_PREFIX);
        assertThat(result).isNotNull();
        assertThat(result).endsWith(SUFFIX);
        assertThat(result).doesNotContain("\\");
    }

    @Test
    void resolveTemplatePrefix_withFileProtocolAndWindowsPath_shouldHandleCorrectly() throws IOException {
        Path templatePath = tempDir.resolve(TEMPLATES);
        Files.createDirectories(templatePath);
        String fileUrl = "file:" + templatePath.toAbsolutePath().toString().replace("\\", "/");
        ReflectionTestUtils.setField(configuration, TEMPLATE_DIRECTORY, fileUrl);
        String result = ReflectionTestUtils.invokeMethod(configuration, RESOLVE_TEMPLATE_PREFIX);
        assertThat(result).isNotNull();
        assertThat(result).endsWith(SUFFIX);
    }

}