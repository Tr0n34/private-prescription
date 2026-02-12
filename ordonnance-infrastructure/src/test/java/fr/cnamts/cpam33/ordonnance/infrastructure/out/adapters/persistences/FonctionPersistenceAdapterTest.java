package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences;

import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.Fonction;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.FonctionId;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.traces.FonctionJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.FonctionEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces.FonctionEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FonctionPersistenceAdapterTest {

    private FonctionJpaRepository fonctionJpaRepository;
    private FonctionEntityMapper fonctionEntityMapper;
    private FonctionPersistenceAdapter adapter;
    private FonctionId id;
    private FonctionEntity entity;
    private Fonction domain;

    @BeforeEach
    void setup() {
        fonctionJpaRepository = mock(FonctionJpaRepository.class);
        fonctionEntityMapper = mock(FonctionEntityMapper.class);
        adapter = new FonctionPersistenceAdapter(fonctionJpaRepository, fonctionEntityMapper);
        id = new FonctionId("F001");
        entity = new FonctionEntity();
        domain = mock(Fonction.class);
    }

    @Test
    void should_return_fonction_when_findById_success() {
        when(fonctionJpaRepository.findByCode("F001")).thenReturn(Optional.of(entity));
        when(fonctionEntityMapper.toDomain(entity)).thenReturn(domain);
        Optional<Fonction> result = adapter.findById(id);
        assertTrue(result.isPresent());
        assertSame(domain, result.get());
        verify(fonctionJpaRepository).findByCode("F001");
        verify(fonctionEntityMapper).toDomain(entity);
        verifyNoMoreInteractions(fonctionJpaRepository, fonctionEntityMapper);
    }

    @Test
    void should_throw_domain_exception_when_findById_not_found() {
        when(fonctionJpaRepository.findByCode("F001")).thenReturn(Optional.empty());
        assertThrows(DomainException.class, () -> adapter.findById(id));
        verify(fonctionJpaRepository).findByCode("F001");
        verifyNoMoreInteractions(fonctionJpaRepository);
        verifyNoInteractions(fonctionEntityMapper);
    }

    @Test
    void should_return_optional_empty_when_mapper_returns_null() {
        when(fonctionJpaRepository.findByCode("F001")).thenReturn(Optional.of(entity));
        when(fonctionEntityMapper.toDomain(entity)).thenReturn(null);
        Optional<Fonction> result = adapter.findById(id);
        assertTrue(result.isEmpty());
        verify(fonctionJpaRepository).findByCode("F001");
        verify(fonctionEntityMapper).toDomain(entity);
        verifyNoMoreInteractions(fonctionJpaRepository, fonctionEntityMapper);
    }

    @Test
    void should_save_fonction() {
        FonctionEntity toSave = new FonctionEntity();
        FonctionEntity saved = new FonctionEntity();
        when(fonctionEntityMapper.toEntity(domain)).thenReturn(toSave);
        when(fonctionJpaRepository.save(toSave)).thenReturn(saved);
        when(fonctionEntityMapper.toDomain(saved)).thenReturn(domain);
        Fonction result = adapter.save(domain);
        assertSame(domain, result);
        verify(fonctionEntityMapper).toEntity(domain);
        verify(fonctionJpaRepository).save(toSave);
        verify(fonctionEntityMapper).toDomain(saved);
        verifyNoMoreInteractions(fonctionJpaRepository, fonctionEntityMapper);
    }

}
