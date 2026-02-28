package fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.traces;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@RequestScope
@Component
public class TraceRequestContext {

    private String utilisateurIp;
    private String correlationId;
    private String frontPage;

    public TraceRequestContext() {
    }

    public static TraceRequestContext empty() {
        return new TraceRequestContext();
    }

    public String utilisateurIp() {
        return utilisateurIp;
    }

    public TraceRequestContext setUtilisateurIp(String utilisateurIp) {
        this.utilisateurIp = utilisateurIp;
        return this;
    }

    public String correlationId() {
        return correlationId;
    }

    public TraceRequestContext setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    public String frontPage() {
        return frontPage;
    }

    public TraceRequestContext setFrontPage(String frontPage) {
        this.frontPage = frontPage;
        return this;
    }

}
