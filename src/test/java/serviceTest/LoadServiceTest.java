package serviceTest;

import dataAccess.*;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoadRequest;
import result.ApiResult;
import result.LoadResult;
import service.LoadService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class LoadServiceTest {

    private UserDao userDao = null;
    private PersonDao personDao = null;
    private EventDao eventDao = null;
    private Database db = null;
    private LoadService loadService = null;
    private Connection connection = null;
    private User[] userArray = null;
    private Person[] personArray = null;
    private Event[] eventArray = null;

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
        } catch(DatabaseException e) {
            System.out.println("LoadServiceTest setUp failed: " + e.getMessage());
        }
    }

    /**
     * Tests the load service with valid parameters
     */
    @Test
    public void loadServicePass() {
        setuserArray();
        setpersonArray();
        seteventArray();

        String expectedString = "Successfully added %d users, %d persons, and %d events to the database.";
        expectedString = String.format(expectedString, userArray.length, personArray.length, eventArray.length);

        LoadRequest loadRequest = new LoadRequest(userArray,personArray,eventArray);
        LoadResult loadResult = loadService.load(loadRequest);

        assertTrue(loadResult.getSuccess());
        assertEquals(expectedString, loadResult.getMessage());
    }

    /**
     * Tests the load service with the Event[] param being null
     */
    @Test
    public void loadServiceFail() {
        setuserArray();
        setpersonArray();

        LoadRequest loadRequest = new LoadRequest(userArray, personArray, null);
        LoadResult loadResult = loadService.load(loadRequest);

        assertFalse(loadResult.getSuccess());
        assertEquals(ApiResult.INVALID_REQUEST_DATA, loadResult.getMessage());
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
            userArray = null;
            personArray = null;
            eventArray = null;
        } catch (DatabaseException e) {
            System.out.println("Problem in LoadServiceTest tearDown(): " + e.getMessage());
        }
    }

    public void setuserArray() {
        User one = new User("u1","p1", "e1", "f1", "l1","m", "p1");
        User two = new User("u2","p2", "e2", "f2", "l2","f", "p2");
        User three = new User("u3","p3", "e3", "f3", "l3","m", "p3");

        userArray = new User[3];

        userArray[0] = one;
        userArray[1] = two;
        userArray[2] = three;
    }

    public void setpersonArray() {
        Person one = new Person("p1", "a1", "f1", "l1",
                "m", "f1", "m1", "s1");
        Person two = new Person("p2", "a2", "f2", "l2",
                "f", "f2", "m2", "s2");
        Person three = new Person("p3", "a3", "f3", "l3",
                "m", "f3", "m3", "s3");
        Person four = new Person("p4", "a4", "f4", "l4",
                "f", "f4", "m4", "s4");

        personArray = new Person[4];

        personArray[0] = one;
        personArray[1] = two;
        personArray[2] = three;
        personArray[3] = four;
    }

    public void seteventArray() {
        Event one = new Event("e1","a1","p1",56.7123,
                120.31,"USA","Orem","Bar Mitzvah",2017);
        Event two = new Event("2","a2","p2",3.191,
                9.323,"USA","Pleasant Grove","Exorcism",2014);

        eventArray = new Event[2];

        eventArray[0] = one;
        eventArray[1] = two;
    }
}
