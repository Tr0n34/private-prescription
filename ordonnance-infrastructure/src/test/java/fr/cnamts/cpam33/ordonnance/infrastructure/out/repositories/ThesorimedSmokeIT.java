package fr.cnamts.cpam33.ordonnance.infrastructure.out.repositories;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.routines.DataBaseRoutineExecutor;
import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.databases.ThesorimedDataSourceConfiguration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("smoke")
@JdbcTest
@ActiveProfiles("integration")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ThesorimedDataSourceConfiguration.class)
public class ThesorimedSmokeIT {

    @Autowired
    @Qualifier("thesorimedDataSource")
    private DataSource thesorimedDataSource;

    @Autowired
    @Qualifier("thesorimedRoutineExecutor")
    private DataBaseRoutineExecutor executor;

    @Test
    @Transactional(transactionManager = "thesorimedTransactionManager")
    void should_load_sql_and_call_refcursor_function() throws Exception {
        List<Map<String, Object>> rows = executor.functionRefcursor(
                "thesorimed.get_the_spe_details",
                List.of("3", new java.math.BigDecimal("1")),
                new ColumnMapRowMapper()
        );
        assertThat(rows).isNotNull();
    }

}
