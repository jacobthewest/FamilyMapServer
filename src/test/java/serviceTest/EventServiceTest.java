package serviceTest;

import dataAccess.AuthTokenDao;
import dataAccess.Database;
import dataAccess.DatabaseException;
import dataAccess.EventDao;
import model.AuthToken;
import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.EventResult;
import service.EventService;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

public class EventServiceTest {
    private EventService eventService = null;
    private EventResult eventResult = null;
    private EventDao eventDao = null;
    private AuthTokenDao authTokenDao = null;
    private Database db = null;
    private Event event = null;
    private Event event2 = null;
    private Event event3 = null;
    private AuthToken authToken = null;
    private Connection connection = null;

    @BeforeEach
    public void setUp() {
        try {
            // Set up member variables
            eventService = new EventService();
            eventDao = new EventDao();
            authTokenDao = new AuthTokenDao();

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
            authTokenDao.setConnection(connection);

            // Create valid authToken and event objects
            event = new Event("eventID", "userName", "personID", 3.14, 8.234,
                    "USA", "Boise","Wedding", 2020);
            event2 = new Event("eventID2", "userName", "personID", 3.1212, 3.122,
                    "USA", "Salt Lake CIty", "Party", 1829);
            event3 = new Event("eventID3", "userName", "personID", 4.2342, 81.2324, "USA",
                    "Compton", "Gang Fight", 2010);
            authToken = new AuthToken("123", "userName");

            // Insert into database and commit them.
            eventDao.insertEvent(event);
            authTokenDao.insertAuthToken(authToken);

            // Commit
            db.commitConnection(true);

        } catch(DatabaseException e) {
            System.out.println("EventServiceTest setUp failed: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() {
        try {
            // Clear tables and database, commit, and close the connection
            db.initializeTables();
            db.emptyDatabase();
            db.commitConnection(true);
            db.closeConnection();

            // Make everything null
            eventService = null;
            eventResult = null;
            eventDao = null;
            authTokenDao = null;
            db = null;
            event = null;
            event2 = null;
            event3 = null;
            authToken = null;
            connection = null;

        } catch(DatabaseException e) {
            System.out.println("EventServiceTest tearDown failed: " + e.getMessage());
        }
    }

    /**
     * Tests retrieving a single event with correct parameters
     */
    @Test
    public void getSingleEventPass() {
        eventResult = eventService.getEvent(event.getEventID(), authToken);
        Event eventFromEventResult = eventResult.getEvent();

        assertNotNull(eventResult);
        assertTrue(eventResult.getSuccess());
        assertEvents(eventFromEventResult, event);
    }

    /**
     * Tests retrieving all events with correct parameters
     */
    @Test
    public void getAllEventsPass() {
        eventResult = eventService.getAllEvents(authToken);
        Event[] data = eventResult.getData();

        assertNotNull(eventResult);
        assertTrue(eventResult.getSuccess());

        for(int i = 0; i < data.length; i++) {
            if(i == 0) {
                assertEvents(data[i], event);
            } else if (i == 1) {
                assertEvents(data[i], event2);
            } else {
                assertEvents(data[i], event3);
            }
        }
    }

    /**
     * Tests retrieving a single event with an invalid authToken
     */
    @Test
    public void getSingleEventFail() {
        boolean codeWorked = false;

        authToken.setUserName("blahblah"); // A bad userName that won't be in the database
        eventResult = eventService.getEvent(event.getEventID(), authToken);
        Event eventFromEventResult = eventResult.getEvent();

        assertNotNull(eventFromEventResult);
        assertFalse(eventResult.getSuccess());
    }

    /**
     * Tests retrieving all events with an invalid authToken
     */
    @Test
    public void getAllEventFail() {
        boolean codeWorked = false;

        authToken.setUserName("blahblah"); // A bad userName that won't be in the database
        eventResult = eventService.getAllEvents(authToken);
        Event[] data = eventResult.getData();

        assertNull(data);
        assertFalse(eventResult.getSuccess());
    }

    public void assertEvents(Event one, Event two) {
        assertEquals(one.getAssociatedUsername(), two.getAssociatedUsername());
        assertEquals(one.getEventID(), two.getEventID());
        assertEquals(one.getPersonID(), two.getPersonID());
        assertEquals(one.getLatitude(), two.getLatitude());
        assertEquals(one.getLongitude(), two.getLongitude());
        assertEquals(one.getCountry(), two.getCountry());
        assertEquals(one.getCity(), two.getCity());
        assertEquals(one.getEventType(), two.getEventType());
        assertEquals(one.getYear(), two.getYear());
    }
}
