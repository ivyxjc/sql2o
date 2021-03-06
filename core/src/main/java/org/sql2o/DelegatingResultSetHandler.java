package org.sql2o;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: dimzon
 * Date: 4/7/14
 * Time: 11:06 PM
 */
public class DelegatingResultSetHandler<E> implements ResultSetHandler<E> {
    private final ResultSetHandlerFactory<E> factory;
    private volatile ResultSetHandler<E> inner = null;

    public DelegatingResultSetHandler(ResultSetHandlerFactory<E> factory) {
        this.factory = factory;
    }

    @Override
    public E handle(ResultSet resultSet) throws SQLException {
        if (inner == null) inner = factory.newResultSetHandler(resultSet.getMetaData());
        return inner.handle(resultSet);
    }
}
