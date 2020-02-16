package DataAccess;

import Model.AuthToken;
import Model.User;

/**
 * A class to access AuthToken data from the AuthToken table in the SQLite Database
 */
public class AuthTokenDao {

    private Database database;

    /**
     * Creates an AuthToken database access object to access SQL Database
     * @param db A Database object which enables us to speak with the AuthToken table
     */
    public AuthTokenDao(Database db) {
        setDatabase(db);
    }

    /**
     * Inserts an AuthToken object into the database if it doesn't exist in it
     * @param authToken An AuthToken object to insert
     */
    public void insertAuthToken(AuthToken authToken) {

    }

    /**
     * Deletes an AuthToken object from the database if it exists in it
     * @param token String identifier used to identify the AuthToken to delete
     */
    public void deleteAuthToken(String token) {

    }

    /**
     * Empties all AuthToken objects from the AuthToken table
     */
    public void deleteAll() {

    }

    /**
     * Updates an AuthToken object in the database
     * @param authToken AuthToken object to update
     */
    public void updateAuthToken(AuthToken authToken) {

    }

    /**
     * Returns a User from the database if it exist in it based off the User's AuthToken
     * @param token String identifier unique to the AuthToken object to retrieve
     * @return A User object from the query
     */
    public User getUser(String token) {
        return null;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }
}
