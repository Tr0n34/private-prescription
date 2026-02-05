package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.Trace;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Configuration
@EnableConfigurationProperties({
        TraceEnqueueExecutorProperties.class,
        TraceWriterProperties.class
})
@EnableAsync
public class TraceConfiguration {

    @Bean
    @Qualifier("traceContextMapper")
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .findAndAddModules()
                .build();
    }

    @Bean
    public BlockingQueue<Trace> traceQueue(TraceWriterProperties props) {
        return new ArrayBlockingQueue<>(props.queueCapacity());
    }

    @Bean("traceEnqueueExecutor")
    public ThreadPoolTaskExecutor traceEnqueueExecutor(TraceEnqueueExecutorProperties props) {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(props.corePoolSize());
        pool.setMaxPoolSize(props.maxPoolSize());
        pool.setQueueCapacity(props.queueCapacity());
        pool.setThreadNamePrefix(props.threadNamePrefix());
        pool.setWaitForTasksToCompleteOnShutdown(props.waitForTasksToCompleteOnShutdown());
        pool.initialize();
        return pool;
    }

}
