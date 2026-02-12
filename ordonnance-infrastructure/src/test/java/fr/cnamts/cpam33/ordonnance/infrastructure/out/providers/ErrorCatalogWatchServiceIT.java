package fr.cnamts.cpam33.ordonnance.infrastructure.out.providers;

import fr.cnamts.cpam33.ordonnance.infrastructure.TestWatcherConfig;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.batch.ErrorCatalogLoader;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.providers.ErrorCatalogWatchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@SpringBootTest(
        classes = { TestWatcherConfig.class, ErrorCatalogWatchService.class },
        properties = "ordonnance.loaders.errors.watcher.enabled=true"
)
class ErrorCatalogWatchServiceIT {

    @Autowired(required = false)
    private ErrorCatalogWatchService service;

    @Autowired
    private ErrorCatalogLoader loader;

    @Test
    void bean_exists() {
        assertThat(service).isNotNull();
    }

    @Test
    void service_is_loaded_when_enabled() {
        assertThat(service).isNotNull();
    }

    @Test
    void default_properties_are_injected() {
        assertThat(service.getReloadDelay()).isEqualTo(5000);
        String filePath = service.getFilePath();
        assertThat(filePath).isNotBlank();
        assertThat(filePath).contains("errors.json");
    }

    @Test
    void performReload_delegates_to_loader() {
        service.performReload();
        verify(loader).reload();
    }

}
