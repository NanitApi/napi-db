package napi.db.provider;

import napi.db.ConnectionProvider;

import java.sql.Connection;
import java.util.function.Consumer;

public class SingleConnectionProvider implements ConnectionProvider {

    private final Connection conn;

    SingleConnectionProvider(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void provide(Consumer<Connection> cb) {
        cb.accept(conn);
    }
}
