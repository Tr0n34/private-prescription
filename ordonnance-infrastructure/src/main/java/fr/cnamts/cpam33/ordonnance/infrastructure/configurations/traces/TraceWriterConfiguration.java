package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.Trace;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Configuration
@EnableConfigurationProperties(TraceWriterProperties.class)
public class TraceWriterConfiguration {

    @Bean
    public BlockingQueue<Trace> traceQueue(TraceWriterProperties props) {
        return new ArrayBlockingQueue<>(props.queueCapacity());
    }

}
