package DataAccess;

import Model.User;

/**
 * A class to access User data from the User table in the SQLite Database
 */
public class UserDao {

    private Database database;

    /**
     * Creates a User database access object to access SQLite Database
     * @param db A Database object which enables us to speak with the User table
     */
    public UserDao(Database db) {
        setDatabase(db);
    }

    /**
     * Inserts a user into the SQLite Database if they do not already exist in it
     * @param user User object to insert
     */
    public void insertUser(User user) {

    }

    /**
     * Deletes a user from the SQLite Database if they still exist in it
     * @param userID String identifier of a User object
     */
    public void deleteUser(String userID) {

    }

    /**
     * Empties all User objects from the User table
     */
    public void deleteAllUsers() {

    }

    /**
     * Updates a User object in the database
     * @param user User object to update in the database
     */
    public void updateUserByUserID(User user) {

    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }
}
