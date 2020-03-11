package dataAccess;

import model.AuthToken;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A class to access AuthToken data from the AuthToken table in the SQLite Database
 */
public class AuthTokenDao {

    private Connection connection;
    private final String DROP_SQL = "DROP TABLE AuthToken";
    private final String EMPTY_SQL = "DELETE FROM AuthToken";
    private final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS AuthToken " +
            "(" +
            "authToken TEXT NOT NULL, " +
            "userName TEXT NOT NULL," +
            "PRIMARY KEY(authToken)" +
            ");";
    private final String INSERT = "INSERT INTO AuthToken " +
            "(authToken, userName) " +
            "VALUES (?,?);";
    private final String DELETE_SQL = "DELETE FROM AuthToken" +
            "WHERE authToken = ?;";
    private final String SELECT_BY_TOKEN = "SELECT userName FROM AuthToken WHERE authToken = ?";
    private final String SELECT_BY_USER_NAME = "SELECT authToken FROM AuthToken WHERE userName = ?";

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
     * @param authToken String identifier used to identify the AuthToken to delete
     * @throws DatabaseException Error with database operation
     */
    public void deleteAuthToken(String authToken) throws DatabaseException {
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(DELETE_SQL);
            stmt.setString(1, authToken);
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
            int numAuthTokens = getCountOfAllAuthTokens();
            if(numAuthTokens == 0) {
                return;
            }

            stmt = connection.prepareStatement(EMPTY_SQL);
            if (stmt.executeUpdate() < 1) {
                throw new DatabaseException("Error with emptying AuthToken table");
            }
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
     * Gets a count of all AuthTokens in the AuthToken table
     * @return The number of AuthTokens in the AuthToken table
     */
    public int getCountOfAllAuthTokens() throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;
        try {
            stmt = connection.prepareStatement("SELECT * FROM AuthToken");
            stmt.executeQuery();
            rs = stmt.executeQuery();
            while(rs.next()) {
                count++;
            }
            stmt.close();
            rs.close();
            return count;
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when getting count of AuthToken table: " + e);
        }
    }

    /**
     * Returns a User from the database if it exist in it based off the User's AuthToken
     * @param authToken String identifier unique to the AuthToken object to retrieve
     * @return A User object from the query
     * @throws DatabaseException Error with database operation
     */
    public User getUser(String authToken) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;
        try {
            stmt = connection.prepareStatement(SELECT_BY_TOKEN);
            stmt.setString(1, authToken);
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
        AuthToken returnedAuthToken = null;
        try {
            stmt = connection.prepareStatement(SELECT_BY_USER_NAME);
            stmt.setString(1, userName);
            rs = stmt.executeQuery();

            String authToken = null;
            while (rs.next()) {
                authToken = rs.getString(1);
                returnedAuthToken = new AuthToken(authToken, userName);
            }
            if(rs != null) rs.close();
            if(stmt != null) stmt.close();
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when selecting AuthToken object: " + e);
        }
        return returnedAuthToken;
    }

    public List<AuthToken> getAllAuthTokensByUsername(String userName) throws DatabaseException{
        PreparedStatement stmt = null;
        ResultSet rs = null;
        AuthToken returnedAuthToken = null;
        List<AuthToken> list = new ArrayList<AuthToken>();

        try {
            stmt = connection.prepareStatement(SELECT_BY_USER_NAME);
            stmt.setString(1, userName);
            rs = stmt.executeQuery();

            String authToken = null;
            while (rs.next()) {
                authToken = rs.getString(1);
                returnedAuthToken = new AuthToken(authToken, userName);
                list.add(returnedAuthToken);
            }
            if(rs != null) rs.close();
            if(stmt != null) stmt.close();
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when selecting AuthToken object: " + e);
        }
        return list;
    }

    /**
     * Returns a User from the database if it exist in it based off the User's userName
     * @param authToken String identifier unique to the AuthToken object to retrieve
     * @return A User object from the query
     * @throws DatabaseException Error with database operation
     */
    public AuthToken getAuthTokenByToken(String authToken) throws DatabaseException{
        PreparedStatement stmt = null;
        ResultSet rs = null;
        AuthToken returnedAuthToken = null;
        try {
            stmt = connection.prepareStatement(SELECT_BY_TOKEN);
            stmt.setString(1, authToken);
            rs = stmt.executeQuery();

            String userName = null;
            while (rs.next()) {
                userName = rs.getString(1);
                returnedAuthToken = new AuthToken(authToken, userName);
            }
            if(rs != null) rs.close();
            if(stmt != null) stmt.close();
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when selecting AuthToken object: " + e);
        }
        return returnedAuthToken;
    }

    /**
     * Checks to see if an AuthToken table exists and can be accessed
     * @return If the table is able to be accessed
     * @throws DatabaseException An error performing the operation
     */
    public boolean canAccessTable() throws DatabaseException {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AuthToken");
            stmt.executeQuery();
            stmt.close();
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
