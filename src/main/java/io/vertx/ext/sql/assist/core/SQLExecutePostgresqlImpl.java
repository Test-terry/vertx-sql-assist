package io.vertx.ext.sql.assist.core;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLOperations;
import io.vertx.ext.sql.UpdateResult;

import java.util.List;

/**
 * JasyncPostgresClient版的SQL实现
 *
 * @author <a href="http://szmirren.com">Mirren</a>
 */
public class SQLExecutePostgresqlImpl implements SQLExecute<SQLOperations> {
    /**
     * SQL客户端
     */
    private final SQLOperations client;

    public SQLExecutePostgresqlImpl(SQLOperations client) {
        super();
        this.client = client;
    }

    @Override
    public SQLOperations getClient() {
        return client;
    }

    @Override
    public Future<JsonObject> queryAsObj(SqlAndParams qp) {
        return this.queryExecute(qp)
                .map(ResultSet::getRows)
                .map(rows -> rows.isEmpty()? null: rows.get(0));
    }

    @Override
    public Future<List<JsonObject>> queryAsListObj(SqlAndParams qp) {
        return this.queryExecute(qp)
                .map(ResultSet::getRows);
    }

    @Override
    public Future<List<JsonArray>> queryAsListArray(SqlAndParams qp) {
        return this.queryExecute(qp)
                .map(ResultSet::getResults);
    }

    @Override
    public Future<JsonArray> insert(SqlAndParams qp) {
        return this.queryExecute(qp)
                .map(ResultSet::getResults)
                .map(results-> results.isEmpty()?new JsonArray():results.get(0));
    }

    @Override
    public Future<Integer> update(SqlAndParams qp) {
        return this.updateExecute(qp)
                .map(UpdateResult::getUpdated);
    }

    /**
     * 执行查询
     *
     * @param qp
     */
    public Future<ResultSet> queryExecute(SqlAndParams qp) {
        Promise<ResultSet> result = Promise.promise();
        if (qp.getParams() == null) {
            client.query(qp.getSql(), result);
        } else {
            client.queryWithParams(qp.getSql(), qp.getParams(), result);
        }
        return result.future();
    }

    /**
     * 执行更新
     *
     * @param qp
     */
    public Future<UpdateResult> updateExecute(SqlAndParams qp) {
        Promise<UpdateResult> result = Promise.promise();
        if (qp.getParams() == null) {
            client.update(qp.getSql(), result);
        } else {
            client.updateWithParams(qp.getSql(), qp.getParams(), result);
        }
        return result.future();
    }
}
