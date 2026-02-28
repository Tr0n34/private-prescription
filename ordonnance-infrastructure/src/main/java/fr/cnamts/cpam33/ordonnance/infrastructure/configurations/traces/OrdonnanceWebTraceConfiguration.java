package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces;

import fr.cnamts.cpam33.ordonnance.infrastructure.in.interceptors.TraceHeaderInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class OrdonnanceWebTraceConfiguration implements WebMvcConfigurer {

    private final TraceHeaderInterceptor traceHeaderInterceptor;

    public OrdonnanceWebTraceConfiguration(TraceHeaderInterceptor traceHeaderInterceptor) {
        this.traceHeaderInterceptor = traceHeaderInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(traceHeaderInterceptor);
    }

}
