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
            db.emptyTables();
            db.commitConnection(true);
        } catch(DatabaseException e) {
            return new ClearResult();
        }
        return new ClearResult();
    }
}
