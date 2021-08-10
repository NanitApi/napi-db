package napi.db;

import java.sql.Connection;
import java.util.function.Consumer;

public interface ConnectionProvider {

    void provide(Consumer<Connection> cb);

}
