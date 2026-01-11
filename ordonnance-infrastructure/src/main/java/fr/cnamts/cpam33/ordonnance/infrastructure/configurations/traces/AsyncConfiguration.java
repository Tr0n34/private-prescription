package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Bean(name = "traceEnqueueExecutor")
    public ThreadPoolTaskExecutor traceEnqueueExecutor(
            TraceEnqueueExecutorProperties props) {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(props.corePoolSize());
        exec.setMaxPoolSize(props.maxPoolSize());
        exec.setQueueCapacity(props.queueCapacity());
        exec.setThreadNamePrefix(props.threadNamePrefix());
        exec.setWaitForTasksToCompleteOnShutdown(
                props.waitForTasksToCompleteOnShutdown()
        );
        exec.initialize();
        return exec;
    }

}
