package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identifiants.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.ImportPatientCandidate;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.ExternalPatientPersistenceAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExternalPatientPersistenceAdapterTest {

    @Mock
    private ImportPatientAdapter importPatientAdapter;

    private ExternalPatientPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ExternalPatientPersistenceAdapter(importPatientAdapter);
    }

    @Test
    void should_delegate_fetch_by_id_to_client() {
        ExternalPatientId externalId = new ExternalPatientId("EXT-123");
        ImportPatientCandidate expectedPatient = mock(ImportPatientCandidate .class);
        when(importPatientAdapter.fetchById(externalId)).thenReturn(expectedPatient);
        ImportPatientCandidate result = adapter.fetchById(externalId);
        assertNotNull(result);
        assertEquals(expectedPatient, result);
        verify(importPatientAdapter, times(1)).fetchById(externalId);
        verifyNoMoreInteractions(importPatientAdapter);
    }

}
