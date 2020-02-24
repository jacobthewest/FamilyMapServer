package DataAccessTest;

import DataAccess.Database;
import DataAccess.DatabaseException;
import DataAccess.EventDao;
import Model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EventDaoTest {
    private Database db;
    private EventDao eventDao;
    private Event eventObject;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        db.loadDriver();
        db.openConnection();
        db.initializeTables();
        db.commitConnection(true);

        eventDao = new EventDao();
        Connection tempConnection = db.getConnection();
        eventDao.setConnection(tempConnection);
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.emptyDatabase();
        db.commitConnection(true);
        db.closeConnection();

        this.eventObject = null;
        this.db = null;
        this.eventDao = null;
    }

    /**
     * Inserts a non existent event with correct data
     * @throws Exception Problem with the code used to test the insertion
     */
    @Test
    public void insertPass() throws Exception {
        Event returnedEvent = null;
        eventDao.empty();
        db.commitConnection(true);
        try {
            setEventObject();
            insertEventObject();
            db.commitConnection(true);
            returnedEvent = eventDao.getEventByEventID("eventID");
        } catch (DatabaseException ex) {
            db.commitConnection(false);
        }

        assertEquals(returnedEvent.getEventID(), "eventID");
        assertEquals(returnedEvent.getAssociatedUsername(), "associatedUsername");
        assertEquals(returnedEvent.getPersonID(), "personID");
        assertEquals(returnedEvent.getLatitude(), 23.351);
        assertEquals(returnedEvent.getLongitude(), 12.214);
        assertEquals(returnedEvent.getCountry(), "country");
        assertEquals(returnedEvent.getCity(), "city");
        assertEquals(returnedEvent.getEventType(), "eventType");
        assertEquals(returnedEvent.getYear(), 2019);

    }

    /**
     * Tests inserting an event an eventID that already exists. Our table enforces unique eventID's
     * so this should not be possible.
     * @throws Exception Error encountered while performing the operation
     */
    @Test
    public void insertNewEventWithPreviouslyUsedEventID() throws Exception {
        boolean myCodeWorked;
        try {
            setEventObject();
            insertEventObject();
            String duplicateEventID = "eventID";
            Event badEvent = new Event(duplicateEventID, "userNameBlah", "personIDBlah",
                    123.456, 789.10,"USA", "Boise", "rave", 1492);
            eventDao.insertEvent(badEvent);
            myCodeWorked = false;
            db.commitConnection(true);
        } catch(Exception e) {
            db.commitConnection(false);
            myCodeWorked = true;
        }
        assertTrue(myCodeWorked);
    }

    /**
     * Tests returning an Event that exists in the database
     * @throws Exception Problem with the code used to test the retrieval
     */
    @Test
    public void retrievePass() throws Exception {
        Event returnedEvent = null;
        try {
            setEventObject();
            insertEventObject();
            returnedEvent = eventDao.getEventByEventID("eventID");
        } catch (DatabaseException ex) {
            db.commitConnection(false);
        }

        assertEquals(eventObject.getEventID(), returnedEvent.getEventID());
        assertEquals(eventObject.getAssociatedUsername(), returnedEvent.getAssociatedUsername());
        assertEquals(eventObject.getPersonID(), returnedEvent.getPersonID());
        assertEquals(eventObject.getLatitude(), returnedEvent.getLatitude());
        assertEquals(eventObject.getLongitude(), returnedEvent.getLongitude());
        assertEquals(eventObject.getCountry(), returnedEvent.getCountry());
        assertEquals(eventObject.getCity(), returnedEvent.getCity());
        assertEquals(eventObject.getEventType(), returnedEvent.getEventType());
        assertEquals(eventObject.getYear(), returnedEvent.getYear());
    }

    /**
     * Tries to return an Event that does not exist in the database
     * @throws Exception Problem with the code used to test the retrieval
     */
    @Test
    public void retrieveFail() throws Exception {
        Event returnedEvent = null;
        try {
            setEventObject();
            insertEventObject();
//            Event nonExistentEvent = new Event("abc", "def", "pID",
//            879.23, 1.013,"United Arab Emirates", "Dubai",
//                    "Disco Bash", 2713);
            returnedEvent = eventDao.getEventByEventID("abc");
        } catch (DatabaseException ex) {
            db.commitConnection(false);
        }
        assertEquals(returnedEvent, null);
    }


    /**
     * Tries to clear all data from the Event table
     * @throws Exception Problem with the code used to test the retrieval
     */
    @Test
    public void clearPass() throws Exception {
        try {
            setEventObject();
            insertEventObject();
            eventDao.empty();
            db.commitConnection(true);
        } catch (DatabaseException ex) {
            db.commitConnection(false);
        }
        assertEquals(eventDao.getCountOfAllEvents(), 0);
    }

    public void setEventObject() {
        this.eventObject = new Event("eventID", "associatedUsername", "personID", 23.351,
                12.214,"country", "city", "eventType", 2019);
    }

    public void insertEventObject() throws Exception {
        setEventObject();
        eventDao.insertEvent(this.eventObject);
        db.commitConnection(true);
    }

}
