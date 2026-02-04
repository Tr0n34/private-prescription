package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.thesorimed;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.routines.DataBaseRoutineExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.*;

public class ThesorimedRoutineExecutor implements DataBaseRoutineExecutor {

    private static final Logger logger = LoggerFactory.getLogger(ThesorimedRoutineExecutor.class);

    public static final int COLUMN_INDEX = 1;
    public static final String SELECT = "SELECT ";
    public static final String OPEN_PARENTHESIS = "(";
    public static final String CLOSE_PARENTHESIS = ")";
    public static final String EMPTY = "";

    private final JdbcTemplate jdbcTemplate;

    public ThesorimedRoutineExecutor(@Qualifier("thesorimedJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public <T> T functionScalar(String qualifiedName, Map<String, ?> inParams, Class<T> returnType) {
        throw new UnsupportedOperationException("Utilise plutôt NamedParameterJdbcTemplate ici si tu veux du named.");
    }

    @Override
    public <T> Optional<T> functionScalarOpt(String qualifiedName, Map<String, ?> inParams, Class<T> returnType) {
        throw new UnsupportedOperationException("Utilise plutôt NamedParameterJdbcTemplate ici si tu veux du named.");
    }

    @Override
    public <T> List<T> functionTable(String qualifiedName, Map<String, ?> inParams, RowMapper<T> rowMapper) {
        throw new UnsupportedOperationException("Idem, à faire en NamedParameterJdbcTemplate ou positional.");
    }

    @Override
    @Transactional(transactionManager = "thesorimedTransactionManager", readOnly = true)
    public <T> List<T> functionRefcursor(String qualifiedName, List<?> inParams, RowMapper<T> rowMapper) {
        return jdbcTemplate.execute((ConnectionCallback<List<T>>) con -> {
            logger.trace("Calling refcursor function [{}] with {} param(s): {}", qualifiedName, inParams.size(), inParams);
            String cursorName = openRefcursor(con, qualifiedName, inParams);
            logger.trace("Refcursor [{}] opened by function [{}]", cursorName, qualifiedName);
            List<T> rows = fetchAll(con, cursorName, rowMapper);
            logger.trace("Fetched {} row(s) from refcursor [{}]", rows.size(), cursorName);
            closeCursorQuietly(con, cursorName);
            return rows;
        });
    }

    private String openRefcursor(Connection con, String qualifiedName, List<?> inParams) throws SQLException {
        String sql = buildCallSql(qualifiedName, inParams.size());
        logger.debug("Executing refcursor open SQL: {}", sql);
        try ( PreparedStatement ps = con.prepareStatement(sql) ) {
            bind(ps, inParams);
            try ( ResultSet rs = ps.executeQuery() ) {
                if ( !rs.next() ) {
                    logger.error("Function [{}] did not return a cursor name", qualifiedName);
                    throw new SQLException("No cursor returned by " + qualifiedName);
                }
                return rs.getString(COLUMN_INDEX);
            }
        }
    }

    private <T> List<T> fetchAll(Connection con, String cursorName, RowMapper<T> rowMapper) throws SQLException {
        String safeCursor = escapePgIdentifier(cursorName);
        String sql = fetchAllCursorSql(safeCursor);
        logger.trace("Fetching all rows from cursor [{}]", cursorName);
        List<T> rows = new ArrayList<>();
        try ( Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql) ) {
            int rowNum = 0;
            while ( rs.next() ) {
                T row = rowMapper.mapRow(rs, rowNum++);
                rows.add(row);
                logger.debug("Mapped row {} from cursor [{}]: {}", rowNum-1, cursorName, row);
            }
        }
        return rows;
    }

    private void closeCursorQuietly(Connection con, String cursorName) {
        String safeCursor = escapePgIdentifier(cursorName);
        String sql = closeCursor(safeCursor);
        try ( Statement st = con.createStatement() ) {
            st.execute(sql);
            logger.debug("Refcursor [{}] closed", cursorName);
        } catch (SQLException e) {
            logger.warn("Failed to close refcursor [{}] (ignored): {}", cursorName, e.getMessage());
        }
    }

    private static String fetchAllCursorSql(String cursorName) {
        return "FETCH ALL IN \"" + cursorName + "\"";
    }

    private static String closeCursor(String cursorName) {
        return "close \"" + cursorName + "\"";
    }

    private String buildCallSql(String qualifiedName, int paramCount) {
        return SELECT + qualifiedName + OPEN_PARENTHESIS + qMarks(paramCount) + CLOSE_PARENTHESIS;
    }

    private static String escapePgIdentifier(String identifier) {
        return identifier == null ? EMPTY : identifier.replace("\"", "\"\"");
    }

    private static String qMarks(int n) {
        String qmarks = EMPTY;
        if ( n > 0 ) {
            qmarks = String.join(", ", Collections.nCopies(n, "?"));
        }
        return qmarks;
    }

    private static void bind(PreparedStatement ps, List<?> params) throws SQLException {
        for ( int i = 0; i < params.size(); i++ ) {
            ps.setObject(i + COLUMN_INDEX, params.get(i));
        }
    }

}

