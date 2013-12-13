/*
 * Source https://github.com/evanx by @evanxsummers
 * 
 */
package vellum.connection;

import java.sql.SQLException;
import vellum.storage.StorageException;

/**
 *
 * @author evan.summers
 */
public interface ConnectionPool {
    public ConnectionEntry takeEntry() throws SQLException, StorageException;
    public void releaseConnection(ConnectionEntry connectionEntry) throws SQLException, 
            StorageException;
    
}
