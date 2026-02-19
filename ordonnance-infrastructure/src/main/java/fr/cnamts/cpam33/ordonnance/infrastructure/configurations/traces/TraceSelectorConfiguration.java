package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.Trace;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestClient;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Configuration
@EnableConfigurationProperties({
        TraceEnqueueExecutorProperties.class,
        TraceWriterProperties.class,
        TraceBackpressureProperties.class,
        TraceOutboxProperties.class,
        TraceApiPublisherProperties.class
})
@EnableAsync
public class TraceSelectorConfiguration {

    @Bean("restClientTrace")
    @ConditionalOnProperty(name = "ordonnance.traces.mode", havingValue = "RABBIT_MQ_QUEUEING")
    public RestClient restClientTrace(RestClient.Builder restClientBuilder, TraceApiPublisherProperties traceApiPublisherProperties) {
        return restClientBuilder
                .baseUrl(traceApiPublisherProperties.url())
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean("traceContextMapper")
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .findAndAddModules()
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "ordonnance.traces.mode", havingValue = "INTERNAL_QUEUEING")
    public BlockingQueue<Trace> traceQueue(TraceWriterProperties props) {
        return new ArrayBlockingQueue<>(props.queueCapacity());
    }

    @Bean("traceEnqueueExecutor")
    @ConditionalOnProperty(name = "ordonnance.traces.mode", havingValue = "INTERNAL_QUEUEING")
    public ThreadPoolTaskExecutor traceEnqueueExecutor(TraceEnqueueExecutorProperties props) {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(props.corePoolSize());
        pool.setMaxPoolSize(props.maxPoolSize());
        pool.setQueueCapacity(props.queueCapacity());
        pool.setThreadNamePrefix(props.threadNamePrefix());
        pool.setWaitForTasksToCompleteOnShutdown(props.waitForTasksToCompleteOnShutdown());
        pool.setRejectedExecutionHandler((r, executor) -> r.run());
        pool.initialize();
        return pool;
    }

}
