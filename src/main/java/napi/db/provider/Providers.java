package napi.db.provider;

import napi.db.ConnectionProvider;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Connection provider factory
 */
public final class Providers {

    public static ConnectionProvider single(Connection connection) {
        return new SingleConnectionProvider(connection);
    }

    public static ConnectionProvider pooled(DataSource source) {
        return new PooledConnectionProvider(source);
    }

}
