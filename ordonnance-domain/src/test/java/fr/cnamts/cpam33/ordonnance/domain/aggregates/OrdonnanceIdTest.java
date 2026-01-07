package fr.cnamts.cpam33.ordonnance.domain.aggregates;

import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.OrdonnanceId;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.OrdonnanceInvalideException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrdonnanceIdTest {

    @Test
    void should_create_valid_ordonnance_id() {
        OrdonnanceId id = new OrdonnanceId("123456");
        assertEquals("123456", id.numero());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "ABC123", "12-34"})
    void should_reject_invalid_numero(String numero) {
        assertThrows(
                OrdonnanceInvalideException.class,
                () -> new OrdonnanceId(numero)
        );
    }

    @Test
    void should_reject_null_numero() {
        assertThrows(
                OrdonnanceInvalideException.class,
                () -> new OrdonnanceId(null)
        );
    }

}
