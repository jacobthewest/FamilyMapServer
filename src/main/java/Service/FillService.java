package Service;

import Result.FillResult;

/**
 * Implements methods to serve the API route <code>/fill/[username]/{generations}</code>
 */
public class FillService {
    private static final int DEFAULT_GENERATIONS = 4;
    /**
     * Populates the server's database with generated data for the specified user name.
     * The required "username" parameter must be a user already registered with the server. If there is
     * any data in the database already associated with the given user name, it is deleted. The
     * optional "generations" parameter lets the caller specify the number of generations of ancestors
     * to be generated, and must be a non-negative integer (the default is 4, which results in 31 new
     * persons each with associated events).
     * @param userName the userName of User to fill
     * @param generations the number of generations to generate
     * @return The FillResult from attempting to fill the database.
     */
    public static FillResult fill(String userName, int generations) {
        return null;
    }

    /**
     * Populates the server's database with generated data for the specified user name.
     * The required "username" parameter must be a user already registered with the server. If there is
     * any data in the database already associated with the given user name, it is deleted. The
     * optional "generations" parameter lets the caller specify the number of generations of ancestors
     * to be generated, and must be a non-negative integer (the default is 4, which results in 31 new
     * persons each with associated events).
     * @param userName the userName of User to fill
     * @return The FillResult from attempting to fill the database.
     */
    public static FillResult fill(String userName) {
        return null;
    }
}