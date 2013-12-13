/*
 * Source https://github.com/evanx by @evanxsummers
 * 
 */
package vellum.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import vellum.storage.StorageException;
import vellum.storage.StorageExceptionType;

/**
 *
 * @author evan.summers
 */
public class ConnectionEntry {
    Connection connection;
    long created = System.currentTimeMillis();
    long offeredTime;
    long releasedTime;
    int offeredCount;
    int releasedCount;
    boolean ok = false;

    public ConnectionEntry(Connection connection) {
        this.connection = connection;
    }
    
    public Connection getConnection() {
        return connection;
    }

    public void taken() {
        ok = false;
        offeredCount++;
        offeredTime = System.currentTimeMillis();
    }
    
    public void returned() {
        releasedCount++;
        releasedTime = System.currentTimeMillis();
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public boolean isOk() {
        return ok;
    }

    public boolean isClosed() throws SQLException {
        return connection == null || connection.isClosed();
    }

    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }
}
