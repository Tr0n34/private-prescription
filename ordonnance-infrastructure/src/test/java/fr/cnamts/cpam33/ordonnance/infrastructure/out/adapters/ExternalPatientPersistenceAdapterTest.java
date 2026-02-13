package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.ExternalPatientPersistenceAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExternalPatientPersistenceAdapterTest {

    @Mock
    private ImportPatientApiClient importPatientApiClient;

    private ExternalPatientPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ExternalPatientPersistenceAdapter(importPatientApiClient);
    }

    @Test
    void should_delegate_fetch_by_id_to_client() {
        ExternalPatientId externalId = new ExternalPatientId("EXT-123");
        Patient expectedPatient = mock(Patient.class);
        when(importPatientApiClient.fetchById(externalId)).thenReturn(expectedPatient);
        Patient result = adapter.fetchById(externalId);
        assertNotNull(result);
        assertEquals(expectedPatient, result);
        verify(importPatientApiClient, times(1)).fetchById(externalId);
        verifyNoMoreInteractions(importPatientApiClient);
    }

}
