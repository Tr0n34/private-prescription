package fr.cnamts.cpam33.ordonnance.domain.objects;

import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NomTest {

    @Test
    void should_create_nom() {
        Nom nom = new Nom("Dupont");
        assertEquals("Dupont", nom.value());
    }

    @Test
    void should_fail_when_nom_is_blank() {
        assertThrows(DomainException.class, () -> new Nom(" "));
    }

}
