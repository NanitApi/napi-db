package napi.db.handler;

import napi.db.ConnectionProvider;
import napi.db.QueryHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;

public class BlockingHandler implements QueryHandler {

    private final ConnectionProvider provider;

    public BlockingHandler(ConnectionProvider provider) {
        this.provider = provider;
    }

    @Override
    public void query(Consumer<ResultSet> result, String sql, Object... args) {
        withPreparedStatement(sql, statement -> {
            try {
                applyArgs(statement, args);
                ResultSet res = statement.executeQuery();
                result.accept(res);
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void update(String sql, Object... args) {
        withPreparedStatement(sql, statement -> {
            try {
                applyArgs(statement, args);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void update(Consumer<ResultSet> result, String sql, Object... args) {
        withPreparedStatement(sql, statement -> {
            try {
                applyArgs(statement, args);
                statement.executeUpdate();
                ResultSet keys = statement.getGeneratedKeys();
                result.accept(keys);
                keys.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, PreparedStatement.RETURN_GENERATED_KEYS);
    }

    @Override
    public void execute(String sql) {
        withStatement(statement -> {
            try {
                statement.execute(sql);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void applyArgs(PreparedStatement st, Object... args) throws SQLException {
        for (int i = 1; i <= args.length; i++) {
            st.setObject(i, args[i-1]);
        }
    }

    private void withPreparedStatement(String sql, Consumer<PreparedStatement> st, int flag) {
        provider.provide((conn) -> {
            try (PreparedStatement statement = conn.prepareStatement(sql, flag)){
                st.accept(statement);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void withPreparedStatement(String sql, Consumer<PreparedStatement> st) {
        withPreparedStatement(sql, st, PreparedStatement.NO_GENERATED_KEYS);
    }

    private void withStatement(Consumer<Statement> st) {
        provider.provide((conn) -> {
            try (Statement statement = conn.createStatement()) {
                st.accept(statement);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }
}
