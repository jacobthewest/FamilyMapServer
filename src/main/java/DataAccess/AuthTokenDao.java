package DataAccess;

import Model.AuthToken;
import Model.Event;
import Model.User;

import java.sql.*;

/**
 * A class to access AuthToken data from the AuthToken table in the SQLite Database
 */
public class AuthTokenDao {

    private Connection connection;
    private final String DROP_SQL = "DROP TABLE AuthToken";
    private final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS AuthToken " +
            "(" +
            "token TEXT NOT NULL, " +
            "userName TEXT NOT NULL" +
            ");";
    private final String INSERT = "INSERT INTO AuthToken " +
            "(token, userName) " +
            "VALUES (?,?);";
    private final String DELETE_SQL = "DELETE FROM AuthToken" +
            "WHERE token = ?;";
    private final String SELECT_BY_TOKEN = "SELECT userName FROM AuthToken WHERE token = ?";
    private final String SELECT_BY_USER_NAME = "SELECT token FROM AuthToken WHERE userName = ?";

    /**
     * Creates an AuthToken database access object to access SQL Database
     */
    public AuthTokenDao() {}

    /**
     * Resets the AuthToken Table
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
            throw new DatabaseException("Error resetting AuthToken table: " + e);
        }
    }

    /**
     * Creates an AuthToken table in the database if it doesn't already exist
     * @throws DatabaseException if an error occurs
     */
    public void createTable() throws DatabaseException {
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(CREATE_SQL);
            stmt.executeUpdate();
            stmt.close();
        }
        catch (SQLException e) {
            throw new DatabaseException("Error creating AuthToken table: " + e);
        }
    }

    /**
     * Inserts an AuthToken object into the database if it doesn't exist in it
     * @return If the user was successfully inserted
     * @param authToken An AuthToken object to insert
     * @throws DatabaseException Error with database operation
     */
    public boolean insertAuthToken(AuthToken authToken) throws DatabaseException {
        PreparedStatement stmt = null;
        try {

            AuthToken tempToken = getAuthTokenByUserName(authToken.getUserName());
            if(tempToken != null) {
                return false;
            }

            stmt = connection.prepareStatement(INSERT);
            stmt.setString(1, authToken.getToken());
            stmt.setString(2, authToken.getUserName());

            if (stmt.executeUpdate() != 1) {
                throw new DatabaseException("Error with inserting AuthToken into database");
            }
            stmt.close();
            return true;
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when inserting AuthToken: " + e);
        }
    }

    /**
     * Deletes an AuthToken object from the database if it exists in it
     * @param token String identifier used to identify the AuthToken to delete
     * @throws DatabaseException Error with database operation
     */
    public void deleteAuthToken(String token) throws DatabaseException {
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(DELETE_SQL);
            stmt.setString(1, token);
            if (stmt.executeUpdate() != 1) {
                throw new DatabaseException("Error with deleting AuthToken object into database");
            }
            stmt.close();
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException deleting AuthToken object from database: " + e);
        }
    }

    /**
     * Empties all AuthToken objects from the AuthToken table
     * @throws DatabaseException Error with database operation
     */
    public void empty() throws DatabaseException {
        PreparedStatement stmt = null;
        try {
            String EMPTY_SQL = "DELETE FROM AuthToken";
            stmt = connection.prepareStatement(EMPTY_SQL);
            stmt.close();
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when emptying AuthToken table: " + e);
        }
    }

    /**
     * Updates an AuthToken object in the database
     * @param authToken AuthToken object to update
     * @throws DatabaseException Error with database operation
     */
    public void updateAuthToken(AuthToken authToken) throws DatabaseException {

    }

    /**
     * Returns a User from the database if it exist in it based off the User's AuthToken
     * @param token String identifier unique to the AuthToken object to retrieve
     * @return A User object from the query
     * @throws DatabaseException Error with database operation
     */
    public User getUser(String token) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;
        try {
            stmt = connection.prepareStatement(SELECT_BY_TOKEN);
            stmt.setString(1, token);
            rs = stmt.executeQuery();
            stmt.close();

            String userName = null;
            while (rs.next()) {
                userName = rs.getString(1);
            }

            UserDao userDao = new UserDao();
            userDao.setConnection(this.connection);
            user = userDao.getUserByUserName(userName);
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when selecting User object from AuthTokenDao class: " + e);
        }
        return user;
    }

    /**
     * Returns a User from the database if it exist in it based off the User's userName
     * @param userName String identifier unique to the AuthToken object to retrieve
     * @return A User object from the query
     * @throws DatabaseException Error with database operation
     */
    public AuthToken getAuthTokenByUserName(String userName) throws DatabaseException{
        PreparedStatement stmt = null;
        ResultSet rs = null;
        AuthToken authToken = null;
        try {
            stmt = connection.prepareStatement(SELECT_BY_USER_NAME);
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            stmt.close();

            String token = null;
            while (rs.next()) {
                token = rs.getString(1);
            }

            authToken = new AuthToken(token, userName);
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when selecting AuthToken object: " + e);
        }
        return authToken;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
