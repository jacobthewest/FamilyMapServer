package service;

import dataAccess.Database;
import dataAccess.DatabaseException;
import result.ApiResult;
import result.ClearResult;

/**
 * Implements all methods needed to serve the API route <code>/clear</code>
 */
public class ClearService {
    /**
     * Deletes ALL data from the database, including user accounts, auth tokens, and
     * generated person, and event data.
     * @return the result of clearing the request
     */
    public ClearResult clear() {
        try {
            Database db = new Database();
            db.loadDriver();
            db.openConnection();
//            db.initializeTables();
            db.emptyTables();
            db.commitConnection(true);

            // Close db connection and return
            db.closeConnection();
            return new ClearResult();
        } catch(DatabaseException e) {
            return new ClearResult(ApiResult.INTERNAL_SERVER_ERROR,e.getMessage() + " Failure to clear database.");
        }
    }
}
