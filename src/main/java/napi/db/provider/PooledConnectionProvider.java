package napi.db.provider;

import napi.db.ConnectionProvider;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;

public class PooledConnectionProvider implements ConnectionProvider {

    private final DataSource source;

    PooledConnectionProvider(DataSource source) {
        this.source = source;
    }

    @Override
    public void provide(Consumer<Connection> cb) {
        try (Connection conn = source.getConnection()) {
            cb.accept(conn);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
