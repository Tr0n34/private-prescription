package fr.cnamts.cpam33.ordonnance.infrastructure;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Définition des tests spécifiques pour utiliser H2
 */
@TestConfiguration
@EnableJpaAuditing
public class TestJpaConfiguration {


}