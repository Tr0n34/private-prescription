package fr.cnamts.cpam33.ordonnance.infrastructure.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ClockConfiguration {

    @Bean
    public Clock clock(@Value("${server.timezone}") String timezone) {
        return Clock.system(ZoneId.of(timezone));
    }


}
