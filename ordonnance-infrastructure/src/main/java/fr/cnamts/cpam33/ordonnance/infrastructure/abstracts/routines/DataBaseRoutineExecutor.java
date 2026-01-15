package fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.routines;

import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DataBaseRoutineExecutor {

    /** Appel fonction scalaire : select schema.fn(:p1, ...) */
    <T> T functionScalar(String qualifiedName, Map<String, ?> inParams, Class<T> returnType);

    /** Appel fonction scalaire nullable. */
    <T> Optional<T> functionScalarOpt(String qualifiedName, Map<String, ?> inParams, Class<T> returnType);

    /** Appel fonction set-returning : select * from schema.fn(:p1, ...) */
    <T> List<T> functionTable(String qualifiedName, Map<String, ?> inParams, RowMapper<T> rowMapper);

    /** Appel procédure : call schema.proc(:p1, ...). Retourne éventuellement des OUT params. */
    <T> List<T> functionRefcursor(String qualifiedName, List<?> inParams, RowMapper<T> rowMapper);

}

