package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties({
        TraceOutboxProperties.class,
        TraceApiPublisherProperties.class,
        TraceErrorRetryProperties.class
})
@EnableAsync
public class TraceSelectorConfiguration {

    @Bean("restClientTrace")
    @ConditionalOnProperty(name = "ordonnance.traces.mode", havingValue = "RABBIT_MQ_QUEUEING")
    public RestClient restClientTrace(RestClient.Builder restClientBuilder, TraceApiPublisherProperties traceApiPublisherProperties) {
        return restClientBuilder
                .baseUrl(traceApiPublisherProperties.url() + traceApiPublisherProperties.resource())
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean("traceContextMapper")
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .findAndAddModules()
                .build();
    }

}
