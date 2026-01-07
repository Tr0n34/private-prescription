package fr.cnamts.cpam33.ordonnance.infrastructure.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.dialect.SpringStandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class PdfConfiguration {

    private static final String CLASSPATH_PREFIX = "classpath:";
    private static final String PATH_SEPARATOR = "/";
    public static final String TEMPLATE_EXTENSION = ".html";
    public static final String DEFAULT_TEMPLATE_DIRECTORY = "templates/";

    @Value("${ordonnance.pdf.input-directory:classpath:templates}")
    private String templateDirectory;

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(createTemplateResolver());
        engine.addDialect(new SpringStandardDialect());
        return engine;
    }

    private FileTemplateResolver createTemplateResolver() {
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCacheable(false);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setCheckExistence(true);
        resolver.setPrefix(resolveTemplatePrefix());
        resolver.setSuffix(TEMPLATE_EXTENSION);
        return resolver;
    }

    private String resolveTemplatePrefix() {
        String normalizedPath = normalizePath(templateDirectory);
        if ( templateDirectory.startsWith(CLASSPATH_PREFIX) ) {
            normalizedPath = resolveClasspathResource(templateDirectory);
        } else if (templateDirectory.startsWith("file:")) {
            normalizedPath = resolveFileResource(templateDirectory);
        }
        return normalizedPath;
    }

    private String normalizePath(String path) {
        String normalized = path.replace("\\", PATH_SEPARATOR);
        return normalized.endsWith(PATH_SEPARATOR)
                ? normalized
                : normalized + PATH_SEPARATOR;
    }

    private String resolveClasspathResource(String normalizedPath) {
        String resourcePath = templateDirectory.substring(CLASSPATH_PREFIX.length());
        if ( resourcePath.startsWith(PATH_SEPARATOR) ) {
            resourcePath = resourcePath.substring(1);
        }
        URL resource = getClass().getClassLoader().getResource(resourcePath);
        if ( resource == null ) {
            throw new IllegalStateException(String.format("Template directory not found in classpath: '%s'", resourcePath));
        }
        return resource.getPath() + PATH_SEPARATOR;
    }

    private String resolveFileResource(String filePath) {
        try {
            String pathWithoutPrefix = filePath.substring("file:".length());
            Path path = Paths.get(pathWithoutPrefix);
            if ( !Files.exists(path )) {
                throw new IllegalStateException(String.format("Template directory not found: '%s'", pathWithoutPrefix));
            }
            return path.toUri().getPath();
        } catch (InvalidPathException e) {
            throw new IllegalStateException(String.format("Invalid file path: '%s'", filePath), e);
        }
    }

}