package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences;

import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.Ordonnance;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identifiants.OrdonnanceId;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.ordonnances.OrdonnanceJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.OrdonnanceEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.OrdonnanceIdEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances.OrdonnanceEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdonnancePersistenceAdapterTest {

    private OrdonnanceJpaRepository ordonnanceJpaRepository;
    private OrdonnanceEntityMapper ordonnanceEntityMapper;
    private OrdonnancePersistenceAdapter adapter;

    @BeforeEach
    void setup() {
        ordonnanceJpaRepository = mock(OrdonnanceJpaRepository.class);
        ordonnanceEntityMapper = mock(OrdonnanceEntityMapper.class);
        adapter = new OrdonnancePersistenceAdapter(ordonnanceEntityMapper, ordonnanceJpaRepository);
    }

    @Test
    void should_return_optional_empty_when_not_found() {
        OrdonnanceId id = new OrdonnanceId("123");
        when(ordonnanceJpaRepository.findById(any())).thenReturn(Optional.empty());
        Optional<Ordonnance> result = adapter.findById(id);
        assertTrue(result.isEmpty());
        verify(ordonnanceJpaRepository).findById(
                argThat(entity -> entity.getNumero().equals("123"))
        );
        verifyNoInteractions(ordonnanceEntityMapper);
    }

    @Test
    void should_map_and_return_ordonnance_when_found() {
        OrdonnanceId id = new OrdonnanceId("123");
        OrdonnanceEntity entity = new OrdonnanceEntity();
        Ordonnance domain = mock(Ordonnance.class);
        when(ordonnanceJpaRepository.findById(new OrdonnanceIdEntity("123")))
                .thenReturn(Optional.of(entity));
        when(ordonnanceEntityMapper.toDomain(entity)).thenReturn(domain);
        Optional<Ordonnance> result = adapter.findById(id);
        assertTrue(result.isPresent());
        assertSame(domain, result.get());
        verify(ordonnanceJpaRepository).findById(new OrdonnanceIdEntity("123"));
        verify(ordonnanceEntityMapper).toDomain(entity);
        verifyNoMoreInteractions(ordonnanceJpaRepository, ordonnanceEntityMapper);
    }

}

