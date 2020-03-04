package serviceTest;

import dataAccess.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import service.FillService;
import service.LoginService;

import java.sql.Connection;

public class LoginServiceTest {

    private UserDao userDao = null;
    private PersonDao personDao = null;
    private EventDao eventDao = null;
    private Database db = null;
    private LoginService loginService = null;
    private Connection connection = null;

    @BeforeEach
    public void setUp() throws DatabaseException {
        try {
            // Set up member variables
            loginService = new LoginService();
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
            System.out.println("FillServiceTest setUp failed: " + e.getMessage());
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
            loginService = null;
            connection = null;
        } catch (DatabaseException e) {
            System.out.println("Problem in FillServiceTest tearDown(): " + e.getMessage());
        }
    }
}
