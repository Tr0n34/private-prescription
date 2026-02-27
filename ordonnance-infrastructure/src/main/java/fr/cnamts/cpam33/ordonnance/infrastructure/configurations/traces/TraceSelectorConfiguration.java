package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;

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
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        return restClientBuilder
                .baseUrl(traceApiPublisherProperties.url() + traceApiPublisherProperties.resource())
                .requestFactory(new JdkClientHttpRequestFactory(httpClient))
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
