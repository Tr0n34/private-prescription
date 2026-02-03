package fr.cnamts.cpam33.ordonnance.infrastructure.out.repositories;

import fr.cnamts.cpam33.ordonnance.infrastructure.TestJpaConfiguration;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.ordonnances.ErrorCatalogJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.ErrorCatalogEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("integration")
@DataJpaTest
@Import(TestJpaConfiguration.class)
@EntityScan(basePackageClasses = ErrorCatalogEntity.class)
@EnableJpaRepositories(basePackageClasses = ErrorCatalogJpaRepository.class)
public class ErrorCatalogJpaRepositoryIT {

    @Autowired
    private ErrorCatalogJpaRepository repository;

    @Test
    void findByCodeAndActiveTrue_shouldReturnEntity_whenActiveTrue() {
        ErrorCatalogEntity entity = new ErrorCatalogEntity();
        entity.setCode("ERR001");
        entity.setDescription("Test error");
        entity.setActive(true);
        entity.setBoundedContext("ORDONNANCE");
        entity.setHttpStatus(400);
        entity.setMessage("Test message");
        repository.save(entity);
        Optional<ErrorCatalogEntity> result = repository.findByCodeAndActiveTrue("ERR001");
        assertThat(result).isPresent();
        assertThat(result.get().getCode()).isEqualTo("ERR001");
        assertThat(result.get().isActive()).isTrue();
    }

    @Test
    void findByCodeAndActiveTrue_shouldReturnEmpty_whenActiveFalse() {
        ErrorCatalogEntity entity = new ErrorCatalogEntity();
        entity.setCode("ERR002");
        entity.setDescription("Test error");
        entity.setBoundedContext("ORDONNANCE");
        entity.setHttpStatus(400);
        entity.setMessage("Test message");
        entity.setActive(false);
        repository.save(entity);
        Optional<ErrorCatalogEntity> result = repository.findByCodeAndActiveTrue("ERR002");
        assertThat(result).isEmpty();
    }

    @Test
    void findByCodeAndActiveTrue_shouldReturnEmpty_whenCodeDoesNotExist() {
        Optional<ErrorCatalogEntity> result = repository.findByCodeAndActiveTrue("UNKNOWN");
        assertThat(result).isEmpty();
    }

}
