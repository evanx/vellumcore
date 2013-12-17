/*
 * Source https://github.com/evanx by @evanxsummers
 * 
 */
package vellum.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author evan.summers
 */
public class SimpleConnectionPool implements ConnectionPool {

    DataSourceProperties dataSourceProperties;
    Queue<ConnectionEntry> availableQueue = new LinkedList();
    Queue<ConnectionEntry> takenQueue = new LinkedList();
    int poolSize = 0;
    int takenCount = 0;
    int releasedCount = 0;
    int validTimeoutSeconds = 2;

    public SimpleConnectionPool(DataSourceProperties dataSourceInfo) {
        if (dataSourceInfo.getPoolSize() != null) {
            this.poolSize = dataSourceInfo.getPoolSize();
        }
        this.dataSourceProperties = dataSourceInfo;
    }

    @Override
    public synchronized ConnectionEntry takeEntry() throws SQLException {
        ConnectionEntry connectionEntry = availableQueue.poll();
        if (connectionEntry != null) {
            Connection connection = connectionEntry.getConnection();
            if (connection != null) {
                try {
                    if (connection.isClosed()) {
                        close(connectionEntry);
                    } else if (!connection.isValid(validTimeoutSeconds)) {
                        close(connectionEntry);
                    }
                } catch (SQLException e) {
                    close(connectionEntry);
                    throw e;
                }
            }
        }
        if (connectionEntry == null) {
            try {
                Connection connection = DriverManager.getConnection(
                        dataSourceProperties.getUrl(), dataSourceProperties.getUser(), 
                        dataSourceProperties.getPassword());
                connectionEntry = new ConnectionEntry(connection);
            } catch (SQLException e) {
                throw e;
            }
        }
        takenCount++;
        connectionEntry.taken();
        return connectionEntry;
    }

    @Override
    public synchronized void releaseConnection(ConnectionEntry connectionEntry) 
        throws SQLException {
        releasedCount++;
        connectionEntry.returned();
        takenQueue.remove(connectionEntry);
        if (connectionEntry.isOk() && availableQueue.size() < poolSize) {
            if (availableQueue.offer(connectionEntry)) {
                return;
            }
        }
        close(connectionEntry);
    }

    static void close(ConnectionEntry connectionEntry) throws SQLException {
        if (!connectionEntry.isClosed()) {
            connectionEntry.getConnection().close();
        }
    }
}
