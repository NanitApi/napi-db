package napi.db.handler;

import napi.db.QueryHandler;

import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class AsyncHandler implements QueryHandler {

    private final QueryHandler blocking;
    private final Executor pool;

    public AsyncHandler(QueryHandler blocking, Executor pool) {
        this.blocking = blocking;
        this.pool = pool;
    }

    @Override
    public void query(Consumer<ResultSet> result, String sql, Object... args) {
        CompletableFuture.runAsync(()->blocking.query(result, sql, args), pool);
    }

    @Override
    public void update(String sql, Object... args) {
        CompletableFuture.runAsync(()->blocking.update( sql, args), pool);
    }

    @Override
    public void update(Consumer<ResultSet> result, String sql, Object... args) {
        CompletableFuture.runAsync(()->blocking.update(result, sql, args), pool);
    }

    @Override
    public void execute(String sql) {
        CompletableFuture.runAsync(()->blocking.execute(sql), pool);
    }
}
