package fr.cnamts.cpam33.ordonnance.infrastructure.contexts;

import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TraceNumGenerator;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TracePublisher;
import fr.cnamts.cpam33.ordonnance.infrastructure.fixtures.stubs.DummyActeMetierService;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.providers.UuidTraceIdNumGenerator;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces.TraceInBuilder;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces.TraceOutBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.mockito.Mockito.mock;

@TestConfiguration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class TestAspectConfiguration {

    @Bean
    Clock clock() {
        return Clock.fixed(Instant.parse("2026-01-10T10:00:00Z"), ZoneId.of("UTC"));
    }

    @Bean
    @Primary
    TracePublisher tracePublisher() {
        return mock(TracePublisher.class);
    }


    @Bean
    @Primary
    TraceInBuilder traceInBuilder() {
        return mock(TraceInBuilder.class);
    }

    @Bean
    @Primary
    TraceOutBuilder traceOutBuilder() {
        return mock(TraceOutBuilder.class);
    }

    @Bean
    DummyActeMetierService dummyActeMetierService() {
        return new DummyActeMetierService();
    }

    @Bean
    TraceNumGenerator traceNumGenerator() {
        return new UuidTraceIdNumGenerator();
    }


}
