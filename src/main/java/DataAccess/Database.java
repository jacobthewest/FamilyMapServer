package DataAccess;


/**
 * Class used to interact with the SQLite database and provide basic top-level
 * database management
 */
public class Database {

    private AuthTokenDao authTokenTable;
    private EventDao eventTable;
    private PersonDao personTable;
    private UserDao userTable;

    /**
     * Creates a new database by passing itself into the dao classes,
     * loading the driver to connect to the database, opening the connection,
     * and initializing the tables.
     */
    public Database() {

    }

    /**
     * Loads the driver needed to speak with the SQLite database
     */
    public void loadDriver() {

    }

    /**
     * Opens a connection to the SQLite database
     */
    public void openConnection() {

    }

    /**
     * Closes the connection to the SQLite database
     */
    public void closeConnection() {

    }

    /**
     * Creates AuthToken, Event, Person, and User tables if
     * they do not already exist in the SQLite database
     */
    public void initializeTables() {

    }

    /**
     * Deletes all tables in the SQLite database
     */
    public void emptyDatabase() {

    }
}
