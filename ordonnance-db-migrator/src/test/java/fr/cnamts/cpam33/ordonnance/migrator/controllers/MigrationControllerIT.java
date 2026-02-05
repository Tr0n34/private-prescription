package fr.cnamts.cpam33.ordonnance.migrator.controllers;

import fr.cnamts.cpam33.ordonnance.migrator.dto.FlywayActionResult;
import fr.cnamts.cpam33.ordonnance.migrator.dto.FlywayDatabaseStatus;
import fr.cnamts.cpam33.ordonnance.migrator.dto.FlywayValidateResult;
import fr.cnamts.cpam33.ordonnance.migrator.fixtures.FlywayMigrationFixtures;
import fr.cnamts.cpam33.ordonnance.migrator.services.FlywayService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IfProfileValue(
        name = "spring.profiles.active",
        values = { "integration" }
)
@ActiveProfiles("integration")
@WebMvcTest(controllers = MigrationController.class)
@AutoConfigureMockMvc
class MigrationControllerIT {

    @Autowired MockMvc mvc;

    @MockBean FlywayService flywayService;

    @Test
    void get_status_shouldReturnStatusAll_defaultOnlyImportantFalse() throws Exception {
        List<FlywayDatabaseStatus> payload = List.of(
                new FlywayDatabaseStatus(
                        "ordonnance",
                        "1",
                        "desc",
                        0,
                        2,
                        0,
                        new String[0],
                        new String[0],
                        List.of()
                )
        );
        when(flywayService.statusAll(false)).thenReturn(payload);
        mvc.perform(get("/admin/migrations"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$[0].datasource").value("ordonnance"))
                .andExpect(jsonPath("$[0].currentVersion").value("1"))
                .andExpect(jsonPath("$[0].appliedCount").value(2));
        
        verify(flywayService).statusAll(false);
        verifyNoMoreInteractions(flywayService);
    }

    @Test
    void get_status_shouldPassOnlyImportantTrue_whenParamIsTrue() throws Exception {
        when(flywayService.statusAll(true)).thenReturn(List.of());
        mvc.perform(get("/admin/migrations").param("onlyImportant", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(content().json("[]"));

        verify(flywayService).statusAll(true);
        verifyNoMoreInteractions(flywayService);
    }

    @Test
    void post_validate_shouldReturnValidateAll() throws Exception {
        List<FlywayValidateResult> payload = List.of(
                new FlywayValidateResult("ordonnance", true, null, null, null, null),
                new FlywayValidateResult("trace", false, "boom", null, "1.2", "Bad desc.sql")
        );
        when(flywayService.validateAll()).thenReturn(payload);
        mvc.perform(post("/admin/migrations/validate"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$[0].datasource").value("ordonnance"))
                .andExpect(jsonPath("$[0].validationSuccessful").value(true))
                .andExpect(jsonPath("$[1].datasource").value("trace"))
                .andExpect(jsonPath("$[1].validationSuccessful").value(false))
                .andExpect(jsonPath("$[1].invalidMigrationVersion").value("1.2"))
                .andExpect(jsonPath("$[1].invalidMigrationDescription").value("Bad desc.sql"));

        verify(flywayService).validateAll();
        verifyNoMoreInteractions(flywayService);
    }

    @Test
    void post_migrateAll_shouldCallService_withDefaultOnlyImportantFalse() throws Exception {
        List<FlywayActionResult> payload = List.of(
                FlywayMigrationFixtures.flywayActionResultFromOrdonnanceMigrationSuccessWith2Executions()
        );
        when(flywayService.migrateAll(false)).thenReturn(payload);
        mvc.perform(post("/admin/migrations/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$[0].datasource").value("ordonnance"))
                .andExpect(jsonPath("$[0].success").value(true))
                .andExpect(jsonPath("$[0].migrationsExecuted").value(2))
                .andExpect(jsonPath("$[0].targetSchemaVersion").value("10"));

        verify(flywayService).migrateAll(false);
        verifyNoMoreInteractions(flywayService);
    }

    @Test
    void post_migrateOrdonnance_shouldCallService_withOnlyImportantTrue() throws Exception {
        FlywayActionResult payload = FlywayMigrationFixtures.flywayActionResultFromOrdonnanceMigrationSuccess();
        when(flywayService.migrateOrdonnance(true)).thenReturn(payload);
        mvc.perform(post("/admin/migrations/ordonnance/run").param("onlyImportant", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.datasource").value("ordonnance"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.migrations[0].script").value("V1__init.sql"));

        verify(flywayService).migrateOrdonnance(true);
        verifyNoMoreInteractions(flywayService);
    }

    @Test
    void post_migrateTrace_shouldCallService_withDefaultOnlyImportantFalse() throws Exception {
        FlywayActionResult payload = FlywayMigrationFixtures.flywayActionResultFromTraceMigrationFailure();
        when(flywayService.migrateTrace(false)).thenReturn(payload);
        mvc.perform(post("/admin/migrations/trace/run"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.datasource").value("trace"))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorType").value("RuntimeException"));

        verify(flywayService).migrateTrace(false);
        verifyNoMoreInteractions(flywayService);
    }

    @Test
    void migrateAll_shouldReturn409_whenServiceThrowsResponseStatusException() throws Exception {
        when(flywayService.migrateAll(false))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Migration already running"));
        mvc.perform(post("/admin/migrations/all"))
                .andExpect(status().isConflict());

        verify(flywayService).migrateAll(false);
        verifyNoMoreInteractions(flywayService);
    }
}
