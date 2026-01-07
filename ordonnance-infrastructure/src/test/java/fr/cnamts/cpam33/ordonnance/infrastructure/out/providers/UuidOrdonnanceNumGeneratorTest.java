package fr.cnamts.cpam33.ordonnance.infrastructure.out.providers;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UuidOrdonnanceNumGeneratorTest {

    private final UuidOrdonnanceNumGenerator generator =
            new UuidOrdonnanceNumGenerator();

    @Test
    void should_generate_a_valid_uuid() {
        String result = generator.generate();
        assertThat(result).isNotNull();
        UUID uuid = UUID.fromString(result);
        assertThat(uuid).isNotNull();
    }

    @Test
    void should_generate_different_values_on_each_call() {
        String first = generator.generate();
        String second = generator.generate();
        assertThat(first).isNotEqualTo(second);
    }
}