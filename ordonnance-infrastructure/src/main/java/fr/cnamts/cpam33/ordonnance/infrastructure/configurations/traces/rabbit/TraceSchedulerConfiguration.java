package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces.rabbit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class TraceSchedulerConfiguration {

    @Bean(name = "traceRetryTaskScheduler")
    public ThreadPoolTaskScheduler traceRetryTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("trace-retry-");
        scheduler.setRemoveOnCancelPolicy(true);
        scheduler.initialize();
        return scheduler;
    }

}