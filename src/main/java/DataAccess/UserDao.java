package DataAccess;

import Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A class to access User data from the User table in the SQLite Database
 */
public class UserDao {

    private Connection connection;
    private final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS User" +
            "(" +
            "userName TEXT NOT NULL, " +
            "password TEXT NOT NULL, " +
            "email TEXT NOT NULL, " +
            "firstName TEXT NOT NULL, " +
            "lastName TEXT NOT NULL, " +
            "gender TEXT NOT NULL, " +
            "personID TEXT NOT NULL, " +
            "PRIMARY KEY(userName) " +
            ");";
    private final String DROP_SQL = "DROP TABLE IF EXISTS User";
    private final String INSERT_SQL = "INSERT INTO Event " +
            "(userName, password, email, firstName, lastName, " +
            "gender, personID) " +
            "VALUES (?,?,?,?,?,?,?);";
    private final String DELETE_SQL = "DELETE FROM User" +
            "WHERE userID = ?;";
    private final String EMPTY_SQL = "DELETE FROM User";
    private final String UPDATE_SQL = "UPDATE User" +
            "password = ?, email = ?, firstName = ?, lastName = ?, " +
            "gender = ?, personID = ?" +
            "WHERE userName = ?";
    private final String SELECT_SQL = "SELECT userName, password, email, " +
            "firstName, lastName, gender, personID FROM User " +
            "WHERE userName = ?";

    /**
     * Creates a User database access object to access SQLite Database
     */
    public UserDao() {}

    /**
     *  Resets the User Table
     * @throws DatabaseException Error with database operation
     */
    public void resetTable() throws DatabaseException {
        PreparedStatement stmtDrop = null;
        PreparedStatement stmtCreate = null;
        try {
            stmtDrop = connection.prepareStatement(DROP_SQL);
            stmtDrop.executeUpdate();
            stmtDrop.close();

            stmtCreate = connection.prepareStatement(CREATE_SQL);
            stmtCreate.executeUpdate();
            stmtCreate.close();
        }
        catch (SQLException e) {
            throw new DatabaseException("Error resetting User table: " + e);
        }
    }

    /**
     * Creates a User table in the database if it doesn't already exist
     * @throws DatabaseException Error with database operation
     */
    public void createTable() throws DatabaseException {
        try {
            PreparedStatement UserTableStmt = connection.prepareStatement(CREATE_SQL);
            UserTableStmt.executeUpdate();
            UserTableStmt.close();
        } catch(Exception e) {
            throw new DatabaseException("Error creating a User table: " + e);
        }
    }

    /**
     * Inserts a user into the SQLite Database if they do not already exist in it
     * @param user User object to insert
     * @throws DatabaseException Error with database operation
     */
    public void insertUser(User user) throws DatabaseException {
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(INSERT_SQL);
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassWord());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());
            if (stmt.executeUpdate() != 1) {
                throw new DatabaseException("Error with inserting User object into database");
            }
            stmt.close();
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when inserting User object: " + e);
        }
    }

    /**
     * Get's a User from the User table
     * @param userName String identifier of a User object
     * @return A User object
     * @throws DatabaseException Error with database operation
     */
    public User getUserByUserName(String userName) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;
        try {
            stmt = connection.prepareStatement(SELECT_SQL);
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            stmt.close();

            while (rs.next()) {
                String password = rs.getString(1);
                String email = rs.getString(2);
                String firstName = rs.getString(3);
                String lastName = rs.getString(4);
                String gender = rs.getString(5);
                String personID = rs.getString(6);
                user = new User(userName, password, email, firstName, lastName, gender, personID);
            }
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when selecting User object: " + e);
        }
        return user;
    }

    /**
     * Deletes a user from the SQLite Database if they still exist in it
     * @param userID String identifier of a User object
     * @throws DatabaseException Error with database operation
     */
    public void deleteUser(String userID) throws DatabaseException {
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(DELETE_SQL);
            stmt.setString(1, userID);
            if (stmt.executeUpdate() != 1) {
                throw new DatabaseException("Error with deleting User object from database");
            }
            stmt.close();
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when deleting User object: " + e);
        }
    }

    /**
     * Empties all User objects from the User table
     * @throws DatabaseException Error with database operation
     */
    public void empty() throws DatabaseException {
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(EMPTY_SQL);
            if (stmt.executeUpdate() != 1) {
                throw new DatabaseException("Error with emptying User table");
            }
            stmt.close();
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when emptying User table: " + e);
        }
    }

    /**
     * Updates a User object in the database
     * @param user User object to update in the database
     * @throws DatabaseException Error with database operation
     */
    public void updateUserByUserID(User user) throws DatabaseException {
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(INSERT_SQL);
            stmt.setString(1, user.getPassWord());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getGender());
            stmt.setString(6, user.getPersonID());
            stmt.setString(7, user.getUserName());
            if (stmt.executeUpdate() != 1) {
                throw new DatabaseException("Error with updating User object in the database");
            }
            stmt.close();
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when updating User object: " + e);
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
