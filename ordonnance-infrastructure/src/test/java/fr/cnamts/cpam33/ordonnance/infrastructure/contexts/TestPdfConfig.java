package fr.cnamts.cpam33.ordonnance.infrastructure.contexts;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.PdfConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.dialect.SpringStandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@TestConfiguration
@ActiveProfiles("integration")
public class TestPdfConfig {

    @Value("${ordonnance.pdf.output-directory}")
    private Path outputDir;

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCacheable(false);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setCheckExistence(true);
        resolver.setPrefix(PdfConfiguration.DEFAULT_TEMPLATE_DIRECTORY);
        resolver.setSuffix(PdfConfiguration.TEMPLATE_EXTENSION);
        engine.setTemplateResolver(resolver);
        engine.setDialect(new SpringStandardDialect());
        return engine;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    public Path getOutputDir() {
        return outputDir;
    }

}
