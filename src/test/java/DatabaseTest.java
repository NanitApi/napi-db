import napi.db.Database;
import napi.db.JdbcDriver;
import napi.db.JdbcUrl;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class DatabaseTest {

    @Test
    public void testConnection() throws SQLException {
        Database db = connect();
        db.blocking().execute("CREATE TABLE IF NOT EXISTS test ( id INTEGER PRIMARY KEY AUTOINCREMENT, name string );");
    }

    @Test
    public void testUpdate() throws SQLException {
        Database db = connect();
        db.blocking().update("insert into test(name) values(?)", "vasya");
    }

    @Test
    public void testUpdateWithKeys() throws SQLException {
        Database db = connect();
        db.blocking().update(keys -> {
            try {
                if (keys.next()) {
                    System.out.println("Id " + keys.getObject(1));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }, "insert into test(name) values(?)", "vasya");
    }

    @Test
    public void testQuery() throws SQLException {
        Database db = connect();
        db.blocking().query(result -> {
            try {
                if (result.next()) {
                    System.out.println("Id " + result.getObject(1));
                    System.out.println("Name " + result.getObject(2));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }, "select * from test where id=?", 2);
    }

    private Database connect() throws SQLException {
        return Database.builder()
                .url(JdbcUrl.local()
                        .driver(JdbcDriver.SQLITE)
                        .path("E:\\test.db")
                        .build())
                .user("user")
                .password("password")
                .build();
    }
}
