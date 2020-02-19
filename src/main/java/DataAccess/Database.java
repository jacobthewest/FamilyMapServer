package DataAccess;

import java.sql.*;

/**
 * Class used to interact with the SQLite database and provide basic top-level
 * database management
 */
public class Database {

    private AuthTokenDao authTokenTable;
    private EventDao eventTable;
    private PersonDao personTable;
    private UserDao userTable;
    private Connection connection;
    private final String DROP_TABLE = "DROP TABLE IF EXISTS ";

    /**
     * Creates a new database by passing itself into the dao classes,
     * loading the driver to connect to the database, opening the connection,
     * and initializing the tables.
     * @throws DatabaseException if an error occurs
     */
    public Database() throws DatabaseException {
        this.authTokenTable = new AuthTokenDao();
        this.eventTable = new EventDao();
        this.personTable = new PersonDao();
        this.userTable = new UserDao();
    }

    /**
     * Loads the driver needed to speak with the SQLite database
     * @throws DatabaseException if an error occurs
     */
    public void loadDriver() throws DatabaseException{
        try{
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        }
        catch(Exception e) {
            throw new DatabaseException("Error loading the driver: " + e);
        }

    }

    /**
     * Opens a connection to the SQLite database
     * @throws DatabaseException if an error occurs
     */
    public void openConnection() throws DatabaseException {
        String url = "C:/Users/jacob/Documents/Winter_2020/240/FamilyMapServer/src/main/java/DataAccess/FMS_DB.sqlite";
        final String CONNECTION_URL = "jdbc:sqlite:" + url;
        try {
            // Open a database connection
            connection = DriverManager.getConnection(CONNECTION_URL);
            // Start a transaction
            connection.setAutoCommit(false);
            authTokenTable.setConnection(this.connection);
            eventTable.setConnection(this.connection);
            personTable.setConnection(this.connection);
            userTable.setConnection(this.connection);
        }
        catch (Exception e) {
            throw new DatabaseException("Error connecting to our database: " + e);
        }
    }

    /**
     *
     * @param errorFree If all of our operations have been successful so far.
     * @throws DatabaseException An error about what went wrong with the commit.
     */
    public void commitConnection(boolean errorFree) throws DatabaseException {
        try {
            if(errorFree) {
                connection.commit();
            } else {
                connection.rollback();
            }

        } catch(Exception ex) {
            throw new DatabaseException("Error committing to the database: " + ex);
        }
    }

    /**
     * Closes the connection to the SQLite database
     * @throws DatabaseException if an error occurs
     */
    public void closeConnection() throws DatabaseException {
        try {
            connection.close();
        } catch(Exception ex) {
            throw new DatabaseException("Error encountered while closing database");
        }
    }

    /**
     * Creates AuthToken, Event, Person, and User tables if
     * they do not already exist in the SQLite database
     * @throws DatabaseException if an error occurs
     */
    public void initializeTables() throws DatabaseException {
        try {
            this.authTokenTable.createTable();
            this.eventTable.createTable();
            this.personTable.createTable();
            this.userTable.createTable();
        } catch(Exception e) {
            throw new DatabaseException("Error initializing tables: " + e);
        }
    }

    /**
     * Empties AuthToken, Event, Person, and User tables, but doesn't delete them.
     * @throws DatabaseException An error with the database interaction.
     */
    public void emptyTables() throws DatabaseException {
        try {
            this.authTokenTable.empty();
            this.eventTable.empty();
            this.personTable.empty();
            this.userTable.empty();
        } catch(Exception e) {
            throw new DatabaseException("Error emptying tables: " + e);
        }
    }

    /**
     * Drops all tables in the SQLite database
     * @throws DatabaseException An error with the database interaction.
     */
    public void emptyDatabase() throws DatabaseException {
        PreparedStatement dropAuthTokenTable = null;
        PreparedStatement dropEventTable = null;
        PreparedStatement dropPersonTable = null;
        PreparedStatement dropUserTable = null;
        try {
            dropAuthTokenTable = connection.prepareStatement(DROP_TABLE + "AuthToken");
            dropEventTable = connection.prepareStatement(DROP_TABLE + "Event");
            dropPersonTable = connection.prepareStatement(DROP_TABLE + "Person");
            dropUserTable = connection.prepareStatement(DROP_TABLE + "User");

            dropAuthTokenTable.executeUpdate();
            dropEventTable.executeUpdate();
            dropPersonTable.executeUpdate();
            dropUserTable.executeUpdate();

            dropAuthTokenTable.close();
            dropEventTable.close();
            dropPersonTable.close();
            dropUserTable.close();
        }
        catch (SQLException e) {
            throw new DatabaseException("Error dropping database tables: " + e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public AuthTokenDao getAuthTokenTable() {
        return authTokenTable;
    }

    public EventDao getEventTable() {
        return eventTable;
    }

    public PersonDao getPersonTable() {
        return personTable;
    }

    public UserDao getUserTable() {
        return userTable;
    }
}
