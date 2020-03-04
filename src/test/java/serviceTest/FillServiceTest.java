package serviceTest;

import dataAccess.*;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.ApiResult;
import result.FillResult;
import service.FillService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class FillServiceTest {
    private UserDao userDao = null;
    private PersonDao personDao = null;
    private EventDao eventDao = null;
    private Database db = null;
    private FillService fillService = null;
    private Connection connection = null;
    private int DEFAULT_GENERATIONS = 4;
    private final String VALID_USERNAME = "userName";
    private final String VALID_PERSON_ID = "personID";
    private final int GENERATIONS_FOR_TEST = 2;

    @BeforeEach
    public void setUp() throws DatabaseException {
        try {
            // Set up member variables
            fillService = new FillService();
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
            createAndInsertStarterObjects();

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
            fillService = null;
            connection = null;
        } catch(DatabaseException e) {
            System.out.println("Problem in FillServiceTest tearDown(): " + e.getMessage());
        }
    }

    /**
     * Tests successfully filling two generations with a valid userName and generation number
     */
    @Test
    public void twoParamFillPass() {
        // Valid userName: "userName"  // Valid because of the inserts we did earlier.
        // Generations we will use to test: 2, to keep it simple
        FillResult fillResult = fillService.fill(VALID_USERNAME, GENERATIONS_FOR_TEST);

        double numPeopleSupposedToBeAdded = Math.pow(2, (GENERATIONS_FOR_TEST + 1)) - 1; // Algorithm for calculating number of nodes in ancestry tree
        int numPeopleAdded = 0;
        try {
            numPeopleAdded = personDao.getCountOfAllPersons();
        } catch(DatabaseException ex) {
            System.out.println("Problem with personDao.getCountOfAllPersons() in twoParamFillPas(): " + ex.getMessage());
        }

        assertTrue(fillResult.getSuccess());
        assertEquals(numPeopleSupposedToBeAdded, numPeopleAdded);
    }

    /**
     * Tests failing the fillService with an invalid (negative) number of generations
     */
    @Test
    public void twoParamFillFail() {
        FillResult fillResult = fillService.fill(VALID_USERNAME, -3);

        assertEquals(ApiResult.INVALID_FILL_PARAM, fillResult.getMessage());
        assertFalse(fillResult.getSuccess());
    }

    /**
     * Tests successfully filling the default 4 generations with a valid userName
     */
    @Test
    public void oneParamFillPass() {
        // Valid userName: "userName"  // Valid because of the inserts we did earlier.
        FillResult fillResult = fillService.fill(VALID_USERNAME);

        // Correct number of people to be added with 4 generations = 31 people.
        int numPeopleSupposedToBeAdded = 31;
        int numPeopleAdded = 0;

        try {
            numPeopleAdded = personDao.getCountOfAllPersons();
        } catch(DatabaseException ex) {
            System.out.println("Problem with personDao.getCountOfAllPersons() in oneParamFillPass(): " + ex.getMessage());
        }

        assertTrue(fillResult.getSuccess());
        assertEquals(numPeopleSupposedToBeAdded, numPeopleAdded);
    }

    /**
     * Tests failing the fillService with a userName that doesn't exist in the database
     */
    @Test
    public void oneParamFillFail() {
        FillResult fillResult = fillService.fill("badUserName");

        assertEquals(ApiResult.INVALID_FILL_PARAM, fillResult.getMessage());
        assertFalse(fillResult.getSuccess());
    }

    private void createAndInsertStarterObjects() {
        User user = new User(VALID_USERNAME, "password", "email@email.com",
                "firstName", "lastName","m", VALID_PERSON_ID);
        Person person = new Person(VALID_PERSON_ID, VALID_USERNAME, "firstName",
                "lastName", "m", "fatherID", "motherID","spouseID");
        Event event1 = new Event("eventID1", VALID_USERNAME, VALID_PERSON_ID, 3.14, 8.234,
                "USA", "Boise", "Wedding", 2020);
        Event event2 = new Event("eventID2", VALID_USERNAME, VALID_PERSON_ID, 3.1212, 3.122,
                "USA", "Salt Lake CIty", "Party", 1829);
        Event event3 = new Event("eventID3", VALID_USERNAME, VALID_PERSON_ID, 4.2342, 81.2324, "USA",
                "Compton", "Gang Fight", 2010);

        try {
            userDao.insertUser(user);
            personDao.insertPerson(person);
            eventDao.insertEvent(event1);
            eventDao.insertEvent(event2);
            eventDao.insertEvent(event3);
            db.commitConnection(true);
        } catch(DatabaseException e) {
            System.out.println("Problem with inserting in createAndInsertStarterObjects(): " + e.getMessage());
        }
    }

}
