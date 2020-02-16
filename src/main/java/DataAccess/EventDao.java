package DataAccess;

import Model.User;
import Model.Event;
import Model.Person;

/**
 * A class to access Event data from the Event table in the SQLite Database
 */
public class EventDao {
    private Database database;

    /**
     * Creates an Event database access object to access SQL Database
     * @param db A Database object which enables us to speak with the Event table
     */
    public EventDao(Database db) {
        setDatabase(db);
    }

    /**
     * Inserts an Event object into the database if it doesn't exist in it
     * @param event An Event object to insert
     */
    public void insertEvent(Event event) {

    }

    /**
     * Deletes an Event object from the database if it exists in it
     * @param eventID String identifier used to identify the Event to delete
     */
    public void deleteEvent(String eventID) {

    }

    /**
     * Empties all Event objects from the Event table
     */
    public void deleteAllEvents() {

    }

    /**
     * Updates an Event object in the database
     * @param event Event object to update
     */
    public void updateEvent(Event event) {

    }

    /**
     * Returns an Event from the database if it exist in it
     * @param eventID String identifier unique to the Event object to retrieve
     * @return A Event object from the query
     */
    public Event getEventByEventID(String eventID) {
        return null;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }
}
