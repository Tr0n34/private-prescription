package fr.cnamts.cpam33.ordonnance.infrastructure.configurations;

import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.enums.PdfExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.InfrastructureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.dialect.SpringStandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class PdfConfiguration {

    public static final String TEMPLATE_EXTENSION = ".html";
    public static final String DEFAULT_TEMPLATE_DIRECTORY = "templates/";
    public static final String FILE = "file:";
    public static final String SEPARATOR = "/";

    private final String templateDirectory;

    public PdfConfiguration(@Value("${ordonnance.pdf.input-directory:classpath:templates}") String templateDirectory) {
        this.templateDirectory = templateDirectory;
    }

    public String getTemplateDirectory() {
        return templateDirectory;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.addTemplateResolver(classpathTemplateResolver());
        engine.addTemplateResolver(fileTemplateResolver());
        engine.addDialect(new SpringStandardDialect());
        return engine;
    }

    @Bean
    public ClassLoaderTemplateResolver classpathTemplateResolver() {
        ClassLoaderTemplateResolver resolver = new org.thymeleaf.templateresolver.ClassLoaderTemplateResolver();
        resolver.setPrefix(DEFAULT_TEMPLATE_DIRECTORY);
        resolver.setSuffix(TEMPLATE_EXTENSION);
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setCacheable(false);
        return resolver;
    }

    @Bean
    public FileTemplateResolver fileTemplateResolver() {
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(resolveFileDirectory(templateDirectory));
        resolver.setSuffix(TEMPLATE_EXTENSION);
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setCheckExistence(true);
        resolver.setCacheable(false);
        return resolver;
    }

    public String resolveFileDirectory(String dir) {
        Path p = null;
        if ( dir.startsWith(FILE) ) {
            String path = dir.substring(FILE.length());
            if (!path.endsWith(SEPARATOR)) path += SEPARATOR;
            p = Paths.get(path);
            if ( !Files.exists(p) ) {
                throw new InfrastructureException(PdfExceptionCode.TECH_PDF_DIRECTORY_NOT_FOUND);
            }
        }
        return ( p != null ) ? p.toAbsolutePath() + SEPARATOR : null;
    }

}