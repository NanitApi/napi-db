package napi.db;

import java.sql.ResultSet;
import java.util.function.Consumer;

public interface QueryHandler {

    /**
     * Execute query and get result
     * @param result Result of query
     * @param sql SQL string
     * @param args Arguments for SQL placeholders
     */
    void query(Consumer<ResultSet> result, String sql, Object... args);

    /**
     * Execute update
     * @param sql SQL string
     * @param args Arguments for SQL placeholders
     */
    void update(String sql, Object... args);

    /**
     * Execute update with generated keys return
     * @param result ResultSet with generated keys
     * @param sql SQL string
     * @param args Arguments for SQL placeholders
     */
    void update(Consumer<ResultSet> result, String sql, Object... args);

    /**
     * Execute SQL
     * @param sql SQL string
     */
    void execute(String sql);
}
