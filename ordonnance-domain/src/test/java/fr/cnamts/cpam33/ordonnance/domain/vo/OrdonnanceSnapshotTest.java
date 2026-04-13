package fr.cnamts.cpam33.ordonnance.domain.vo;


import fr.cnamts.cpam33.ordonnance.domain.fixtures.OrdonnanceFixtures;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.SignatureFixtures;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.enums.OrdonnanceExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.Ordonnance;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Signature;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.snapshots.OrdonnanceSnapshot;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class OrdonnanceSnapshotTest {

    @Test
    void constructor_shouldCreateSuccessfully_whenAllParametersProvided() {
        Ordonnance ordonnance = OrdonnanceFixtures.ordonnanceValide();
        Signature signature = SignatureFixtures.signature();
        LocalDateTime now = LocalDateTime.now();
        OrdonnanceSnapshot snapshot = new OrdonnanceSnapshot(ordonnance, signature, now);
        assertNotNull(snapshot);
        assertEquals(ordonnance, snapshot.ordonnance());
        assertEquals(signature, snapshot.signature());
        assertEquals(now, snapshot.emmittedOn());
    }

    @Test
    void constructor_shouldThrowException_whenOrdonnanceIsNull() {
        Signature signature = SignatureFixtures.signature();
        LocalDateTime now = LocalDateTime.now();

        DomainException ex = assertThrows(
                DomainException.class,
                () -> new OrdonnanceSnapshot(null, signature, now)
        );
        assertEquals(OrdonnanceExceptionCode.BS_ORDONNANCE_ID_MISSING, ex.getCode());
    }

    @Test
    void constructor_shouldThrowException_whenSignatureIsNull() {
        Ordonnance ordonnance = OrdonnanceFixtures.ordonnanceValide();
        LocalDateTime now = LocalDateTime.now();
        DomainException ex = assertThrows(
                DomainException.class,
                () -> new OrdonnanceSnapshot(ordonnance, null, now)
        );
        assertEquals(OrdonnanceExceptionCode.BS_ORDONNANCE_SNAPSHOT_MUST_BE_SIGNED, ex.getCode());
    }
}

