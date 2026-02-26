package fr.cnamts.cpam33.ordonnance.infrastructure.in.interceptors;

import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.traces.TraceRequestContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TraceHeaderInterceptor implements HandlerInterceptor {

    private final TraceRequestContext traceRequestContext;

    public TraceHeaderInterceptor(TraceRequestContext traceRequestContext) {
        this.traceRequestContext = traceRequestContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        traceRequestContext.setCorrelationId(request.getHeader("correlationId"));
        traceRequestContext.setFrontPage(request.getHeader("frontPage"));
        String ip = firstNonBlank(
                request.getHeader("utilisateurIp"),
                request.getHeader("X-Forwarded-For"),
                request.getHeader("X-Real-IP"),
                request.getRemoteAddr()
        );
        traceRequestContext.setUtilisateurIp(extractFirstIp(ip));
        return true;
    }

    private static String firstNonBlank(String... values) {
        for (String v : values) if (v != null && !v.isBlank()) return v;
        return null;
    }

    private static String extractFirstIp(String xForwardedFor) {
        if (xForwardedFor == null) return null;
        int comma = xForwardedFor.indexOf(',');
        return comma >= 0 ? xForwardedFor.substring(0, comma).trim() : xForwardedFor.trim();
    }

}
