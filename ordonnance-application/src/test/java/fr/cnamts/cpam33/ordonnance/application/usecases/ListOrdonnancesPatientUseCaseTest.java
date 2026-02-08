package fr.cnamts.cpam33.ordonnance.application.usecases;

import fr.cnamts.cpam33.ordonnance.application.fixtures.ListerOrdonnancePatientQueryFixtures;
import fr.cnamts.cpam33.ordonnance.application.usecases.ordonnances.ListOrdonnancesPatientUseCase;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.OrdonnanceFixtures;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.PatientFixtures;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.Ordonnance;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.queries.ListerOrdonnancesPatientQuery;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.ordonnances.OrdonnanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ListOrdonnancesPatientUseCaseTest {

    private OrdonnanceRepository ordonnanceRepository;
    private ListOrdonnancesPatientUseCase useCase;

    @BeforeEach
    void setUp() {
        ordonnanceRepository = mock(OrdonnanceRepository.class);
        useCase = new ListOrdonnancesPatientUseCase(ordonnanceRepository);
    }

    @Test
    void executeShouldFilterByDateAndSignatureFalse() {
        PatientId patientId = PatientFixtures.patientValide().patientId();
        Ordonnance o1 = OrdonnanceFixtures.ordonnanceValide("123456");
        Ordonnance o2 = OrdonnanceFixtures.ordonnanceValide("1234567");
        Ordonnance o3 = OrdonnanceFixtures.ordonnanceValide("12345678");
        when(ordonnanceRepository.findByPatientId(patientId)).thenReturn(Arrays.asList(o1, o2, o3));
        ListerOrdonnancesPatientQuery query = ListerOrdonnancePatientQueryFixtures.betweenYesterdayAndToday();
        List<Ordonnance> result = useCase.execute(query);
        assertEquals(3, result.size());
        assertEquals("123456", result.getFirst().ordonnanceId().numero());
        verify(ordonnanceRepository, times(1)).findByPatientId(patientId);
    }

}
