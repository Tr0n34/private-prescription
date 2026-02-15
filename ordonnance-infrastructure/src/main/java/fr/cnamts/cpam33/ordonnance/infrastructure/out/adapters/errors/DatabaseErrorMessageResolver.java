package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.errors;

import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.ExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.Adapter;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.ErrorMessageDomainResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.ErrorMessageInfrastructureResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.InfraStructureExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.ErrorDescriptor;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.ordonnances.ErrorCatalogJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.ErrorCatalogEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DatabaseErrorMessageResolver implements ErrorMessageDomainResolver, ErrorMessageInfrastructureResolver, Adapter {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseErrorMessageResolver.class);

    public static final String UNRESOLVED_ERROR_MESSAGE = "Error code not found : %s";

    private static final Pattern TOKEN = Pattern.compile("\\{([a-zA-Z0-9_\\-.]+)\\}");
    public static final String TEMPLATE_PLACEHOLDER_DEFAULT = "";
    public static final int INITIAL_CAPACITY = 16;
    public static final char OPEN_PARENTHESIS = '{';
    public static final char CLOSED_PARENTHESIS = '}';

    private final ErrorCatalogJpaRepository errorCatalogJpaRepository;

    public DatabaseErrorMessageResolver(ErrorCatalogJpaRepository errorCatalogJpaRepository) {
        this.errorCatalogJpaRepository = errorCatalogJpaRepository;
    }

    @Override
    public ErrorDescriptor resolve(ExceptionCode exceptionCode, Map<String, Object> placeHolders) {
        return resolveByCode(exceptionCode.toString(), placeHolders);
    }

    @Override
    public ErrorDescriptor resolve(InfraStructureExceptionCode code, Map<String, Object> placeHolders) {
        return resolveByCode(code.toString(), placeHolders);
    }

    @Override
    public ErrorDescriptor resolve(InfraStructureExceptionCode code) {
        return resolveByCode(code.toString(), null);
    }

    public ErrorDescriptor resolveByCode(String exceptionCode, Map<String, ?> placeHolders) {
        ErrorCatalogEntity entity = errorCatalogJpaRepository.findByCodeAndActiveTrue(exceptionCode).orElseThrow(
                () -> new IllegalStateException(String.format(UNRESOLVED_ERROR_MESSAGE, exceptionCode))
        );
        String rendered = renderTemplate(entity.getMessage(), placeHolders == null ? Map.of() : placeHolders);
        logger.debug("{} : {}", exceptionCode, rendered);
        return ErrorDescriptor.of(
                entity.getCode(),
                rendered,
                entity.getHttpStatus(),
                LocalDateTime.now(),
                entity.getBoundedContext());
    }

    private String renderTemplate(String template, Map<String, ?> placeHolders) {
        String result = TEMPLATE_PLACEHOLDER_DEFAULT;
        if ( template != null && !template.isBlank() ) {
            Matcher matcher = TOKEN.matcher(template);
            if ( !matcher.find() ) {
                result = template;
            } else {
                StringBuilder out = new StringBuilder(template.length() + INITIAL_CAPACITY);
                int last = 0;
                do {
                    out.append(template, last, matcher.start());
                    String key = matcher.group(1);
                    Object value = placeHolders.get(key);
                    if ( value == null ) {
                        out.append(OPEN_PARENTHESIS).append(key).append(CLOSED_PARENTHESIS);
                    } else {
                        out.append(value);
                    }
                    last = matcher.end();
                } while ( matcher.find() );
                out.append(template, last, template.length());
                result = out.toString();
            }
        }
        return result;
    }

}
