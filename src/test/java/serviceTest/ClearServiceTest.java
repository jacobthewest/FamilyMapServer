package serviceTest;

import dataAccess.*;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.ClearResult;
import service.ClearService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    private Database db = null;
    private EventDao eventDao = null;
    private PersonDao personDao = null;
    private UserDao userDao = null;
    private AuthTokenDao authTokenDao = null;
    private ClearResult clearResult = null;
    private ClearService clearService = null;
    private Connection connection = null;

    @BeforeEach
    public void setUp() {
        try {
            eventDao = new EventDao();
            personDao = new PersonDao();
            userDao = new UserDao();
            authTokenDao = new AuthTokenDao();
            clearService = new ClearService();

            // Set up database
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
            authTokenDao.setConnection(connection);

             //Insert data into tables
            eventDao.insertEvent(new Event("id", "uName", "pID", 3.14,
                    9.35, "USA", "Compton", "Gang Fight", 1900));
            personDao.insertPerson(new Person("pID", "uName", "fName", "lName", "f", "fID",
                    "mID", "sID"));
            userDao.insertUser(new User("uName", "pWord", "email@email.com", "fName", "lName",
                    "m", "pID"));
            authTokenDao.insertAuthToken(new AuthToken("7191nad", "uName"));
            db.commitConnection(true);
        } catch(DatabaseException e) {
            System.out.println("Problem in ClearServiceTest setUp(): " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() throws DatabaseException {
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
            authTokenDao = null;
            clearResult = null;
            clearService = null;
            connection = null;
        } catch(DatabaseException e) {
            System.out.println("Problem in ClearServiceTest tearDown(): " + e.getMessage());
        }
    }

    /**
     * Tests a normal case of clearing tables
     */
    @Test
    public void clearServicePass() {
        boolean codeWorked = false;
        try {
            // Use the service
            clearResult = clearService.clear();

            int eventCount = eventDao.getCountOfAllEvents();
            int personCount = personDao.getCountOfAllPersons();
            int userCount = userDao.getCountOfAllUsers();
            int authCount = authTokenDao.getCountOfAllAuthTokens();

            // One of the tables was not cleared
            if(eventCount != 0 || personCount != 0 || userCount != 0 || authCount != 0) {
                codeWorked = false;
            } else {
                codeWorked = true;
                assertTrue(clearResult.getSuccess());
            }
        } catch(DatabaseException e) {
            codeWorked = false;
        }
        assertTrue(codeWorked);
    }

    /**
     * Tests clearing tables that don't exist in the database.
     */
    @Test
    public void clearServiceFail() {
        try {
            db.emptyDatabase(); // Deletes tables from existence
            db.commitConnection(true);

            clearResult = clearService.clear();
            assertFalse(clearResult.getSuccess());
        } catch(DatabaseException e) {
            assertNotNull(e); // Just asserts that an error was thrown.
        }
    }
}
