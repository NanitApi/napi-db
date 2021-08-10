package napi.db;

import napi.db.handler.AsyncHandler;
import napi.db.handler.BlockingHandler;
import napi.db.provider.Providers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 * General class to manipulate with database
 */
public class Database {

    private final QueryHandler blockHandler;
    private final QueryHandler asyncHandler;

    private Database(QueryHandler blocking, QueryHandler async) {
        this.blockHandler = blocking;
        this.asyncHandler = async;
    }

    /**
     * Get blocking query handler
     * @return Blocking query handler instance
     */
    public QueryHandler blocking() {
        return blockHandler;
    }

    /**
     * Get async query handler
     * @return Async query handler instance
     */
    public QueryHandler async() {
        return asyncHandler;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Properties props;
        private String url;
        private DataSource source;
        private Executor executor;

        private Builder() {
            props = new Properties();
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder url(JdbcUrl url) {
            this.url = url.toString();
            return this;
        }

        public Builder user(String user) {
            props.put("user", user);
            return this;
        }

        public Builder password(String password) {
            props.put("password", password);
            return this;
        }

        /**
         * Set data source. If not specified, single connection will be used.
         * @param source DataSource instance
         * @return Current Builder
         */
        public Builder source(DataSource source) {
            this.source = source;
            return this;
        }

        /**
         * Set connection property
         * @param key Property key
         * @param value Property value
         * @return Current Builder
         */
        public Builder property(String key, Object value) {
            props.put(key, value);
            return this;
        }

        /**
         * Set executor for async operations.
         * If it not specified, common pool will be used.
         * @param executor Executor instance
         * @return Current Builder
         */
        public Builder executor(Executor executor) {
            this.executor = executor;
            return this;
        }

        public Database build() throws SQLException {
            if (url == null)
                throw new SQLException("Connection URL cannot be null");

            ConnectionProvider provider;

            if (source == null) {
                Connection conn = DriverManager.getConnection(url, props);
                provider = Providers.single(conn);
            } else {
                provider = Providers.pooled(source);
            }

            if (executor == null) {
                executor = ForkJoinPool.commonPool();
            }

            QueryHandler blockingHandler = new BlockingHandler(provider);
            QueryHandler asyncHandler = new AsyncHandler(blockingHandler, executor);

            return new Database(blockingHandler, asyncHandler);
        }
    }

}
