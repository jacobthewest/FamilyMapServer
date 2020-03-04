package serviceTest;

import dataAccess.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import service.FillService;
import service.LoadService;

import java.sql.Connection;

public class LoadServiceTest {

    private UserDao userDao = null;
    private PersonDao personDao = null;
    private EventDao eventDao = null;
    private Database db = null;
    private LoadService loadService = null;
    private Connection connection = null;

    @BeforeEach
    public void setUp() throws DatabaseException {
        try {
            // Set up member variables
            loadService = new LoadService();
            eventDao = new EventDao();
            userDao = new UserDao();
            personDao = new PersonDao();

            // Set up database and Daos
            db = new Database();
            db.loadDriver();
            db.openConnection();
            db.initializeTables();
            db.emptyTables();
            db.commitConnection(true);
            connection = db.getConnection();

            // Set dao connections
            eventDao.setConnection(connection);
            personDao.setConnection(connection);
            userDao.setConnection(connection);

            // Create User, User as a person object, and three events for that user.
            // Insert all objects into the database


        } catch(DatabaseException e) {
            System.out.println("LoadServiceTest setUp failed: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() {
        try {
            // Close database
            db.initializeTables();
            db.emptyDatabase();
            db.commitConnection(true);
            db.closeConnection();

            // Set everything to null
            db = null;
            eventDao = null;
            personDao = null;
            userDao = null;
            loadService = null;
            connection = null;
        } catch (DatabaseException e) {
            System.out.println("Problem in LoadServiceTest tearDown(): " + e.getMessage());
        }
    }
}
