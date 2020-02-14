package Service;

import Result.LoadResult;
import Request.LoadRequest;

/**
 * Implements methods to serve the API route <code>/load</code>
 */
public class LoadService {
    /**
     * Clears all data from the database (just like the /clear API), and then loads the
     * posted user, person, and event data into the database.
     * @param request An instance of LoadRequest containing a User array,
     *                a Person array, and an Events array
     * @return The result of loading.
     */
    public static LoadResult load(LoadRequest request) {
        return null;
    }
}
