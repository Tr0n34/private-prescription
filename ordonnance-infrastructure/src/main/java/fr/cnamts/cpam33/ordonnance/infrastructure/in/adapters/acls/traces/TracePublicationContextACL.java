package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.acls.traces;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.traces.TracePublicationContextProvider;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.traces.TraceRequestContext;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.traces.TracePublicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TracePublicationContextACL implements TracePublicationContextProvider {

    private static final Logger logger = LoggerFactory.getLogger(TracePublicationContextACL.class.getName());

    private final String schemaVersion;
    private final String applicationId;
    private final TraceRequestContext requestContext;

    public TracePublicationContextACL(
            @Value("${ordonnance.traces.schema-version}") String schemaVersion,
            @Value("${ordonnance.name}") String applicationId,
            TraceRequestContext requestContext
    ) {
        this.schemaVersion = schemaVersion;
        this.applicationId = applicationId;
        this.requestContext = requestContext;
    }

    @Override
    public TracePublicationContext current() {
        logger.trace("TracePublicationContextACL current() : schemaVersion={}, applicationId={}, correlationId={}",
                schemaVersion, applicationId, requestContext.correlationId());
        return new TracePublicationContext(
                schemaVersion,
                applicationId,
                requestContext.utilisateurIp(),
                requestContext.correlationId(),
                requestContext.frontPage()
        );
    }

}
