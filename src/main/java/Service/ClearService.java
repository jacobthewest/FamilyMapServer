package Service;

import Request.ClearRequest;
import Result.ClearResult;

/**
 * Implements all methods needed to serve the API route <code>/clear</code>
 */
public class ClearService {
    /**
     * Deletes ALL data from the database, including user accounts, auth tokens, and
     * generated person and event data.
     * @param request An instance of ClearRequest with all of the data needed function
     * @return the result of clearing the request
     */
    public static ClearResult clear(ClearRequest request) {
        return null;
    }
}
