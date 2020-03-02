package Service;

import DataAccess.Database;
import DataAccess.DatabaseException;
import Result.ApiResult;
import Result.ClearResult;

/**
 * Implements all methods needed to serve the API route <code>/clear</code>
 */
public class ClearService {
    /**
     * Deletes ALL data from the database, including user accounts, auth tokens, and
     * generated person, and event data.
     * @return the result of clearing the request
     */
    public static ClearResult clear() {
        try {
            Database db = new Database();
            db.loadDriver();
            db.openConnection();
            db.emptyTables();
            db.commitConnection(true);
            db.closeConnection();
        } catch(DatabaseException e) {
            return new ClearResult(ApiResult.INTERNAL_SERVER_ERROR
             + ": " + e.getMessage() + " Failure to clear database.");
        }
        return new ClearResult();
    }
}
