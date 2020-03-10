package dataAccess;

import model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A class to access Event data from the Event table in the SQLite Database
 */
public class EventDao {
    private Connection connection;
    private final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS Event" +
            "(" +
            "eventID TEXT NOT NULL, " +
            "associatedUsername TEXT NOT NULL, " +
            "personID TEXT NOT NULL, " +
            "latitude NUMERIC NOT NULL, " +
            "longitude NUMERIC NOT NULL, " +
            "country TEXT NOT NULL, " +
            "city TEXT NOT NULL, " +
            "eventType TEXT NOT NULL, " +
            "year NUMERIC NOT NULL " +
            ")";
    private final String DROP_SQL = "DROP TABLE Event";
    private final String INSERT = "INSERT INTO Event " +
            "(eventID, associatedUsername, personID, latitude, longitude, " +
            "country, city, eventType, year) " +
            "VALUES (?,?,?,?,?,?,?,?,?);";
    private final String DELETE_SQL = "DELETE FROM Event" +
            "WHERE eventID = ?;";
    private final String EMPTY_SQL = "DELETE FROM Event";
    private final String UPDATE_SQL = "UPDATE Event" +
            "SET associatedUsername = ?, personID = ?, latitude = ?, " +
            "longitude = ?, country = ?, city = ?, eventType = ?, year = ?" +
            "WHERE eventID = ?";
    private final String SELECT_SQL = "SELECT * FROM Event " +
            "WHERE eventID = ?";

    /**
     * Creates an Event database access object to access SQL Database
     */
    public EventDao() {}

    /**
     *  Resets the Event Table
     * @throws DatabaseException Error with database operation
     */
    public void resetTable() throws DatabaseException {
        PreparedStatement stmtDrop = null;
        PreparedStatement stmtCreate = null;
        try {
            stmtDrop = connection.prepareStatement(DROP_SQL);
            stmtDrop.executeUpdate();
            stmtDrop.close();

            stmtCreate = connection.prepareStatement(CREATE_SQL);
            stmtCreate.executeUpdate();
            stmtCreate.close();
        }
        catch (SQLException e) {
            throw new DatabaseException("Error resetting Event table: " + e);
        }
    }

    /**
     * Gets a count of all Users in the User table
     * @return The number of Users in the User table
     */
    public int getCountOfAllEvents() throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;
        try {
            stmt = connection.prepareStatement("SELECT * FROM Event");
            stmt.executeQuery();
            rs = stmt.executeQuery();
            while(rs.next()) {
                count++;
            }
            stmt.close();
            rs.close();
            return count;
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when getting count of Event table: " + e);
        }
    }

    /**
     * Gets a count of all Users in the User table
     * @return The number of Users in the User table
     */
    public int getCountOfAllEvents(String associatedUsername) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;
        try {
            stmt = connection.prepareStatement("SELECT * FROM Event WHERE associatedUsername = ?");
            stmt.setString(1, associatedUsername);
            stmt.executeQuery();
            rs = stmt.executeQuery();
            while(rs.next()) {
                count++;
            }
            stmt.close();
            rs.close();
            return count;
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when getting count of Event table: " + e);
        }
    }

    /**
     * Creates an Event table in the database if it doesn't already exist
     * @throws DatabaseException Error with database operation
     */
    public void createTable() throws DatabaseException {
        try {
            PreparedStatement UserTableStmt = connection.prepareStatement(CREATE_SQL);
            UserTableStmt.executeUpdate();
            UserTableStmt.close();
        } catch(Exception e) {
            throw new DatabaseException("Error creating an Event table: " + e);
        }

    }

    /**
     * Inserts an Event object into the database if it doesn't exist in it
     * @return If the user was successfully inserted
     * @param event An Event object to insert
     * @throws DatabaseException Error with database operation
     */
    public boolean insertEvent(Event event) throws DatabaseException{
        PreparedStatement stmt = null;
        try {
            Event tempEvent = getEventByEventID(event.getEventID());
            if(tempEvent != null) {
                boolean problem = areIdenticalEvents(tempEvent, event);
                if(problem) {
                    throw new DatabaseException("Event already exists with eventID: " + event.getEventID());
                }
            }

            stmt = connection.prepareStatement(INSERT);
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getAssociatedUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setDouble(4, event.getLatitude());
            stmt.setDouble(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());
            if (stmt.executeUpdate() != 1) {
                throw new DatabaseException("Error with inserting Event object into database");
            }
            stmt.close();
            return true;
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when inserting Event object: " + e);
        }
    }

    /**
     * Checks to see if two events are identical. Insertion is NOT allowed if
     * the events have identical eventID's and personID's.
     * @param tempEvent An event already in the database
     * @param event An event to be inserted into the database
     * @return
     */
    public boolean areIdenticalEvents(Event tempEvent, Event event) {
        boolean equalEventIds = false;
        boolean equalPersonIDs = false;

        if(tempEvent.getEventID().equals(event.getEventID())) {
            equalEventIds = true;
        }

        if(tempEvent.getPersonID().equals(event.getPersonID())) {
            equalPersonIDs = true;
        }

        if(equalEventIds && equalPersonIDs) {return true;}
        return false;
    }

    /**
     * Deletes an Event object from the database if it exists in it
     * @param eventID String identifier used to identify the Event to delete
     * @throws DatabaseException Error with database operation
     */
    public void deleteEvent(String eventID) throws DatabaseException{
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(DELETE_SQL);
            stmt.setString(1, eventID);
            if (stmt.executeUpdate() != 1) {
                throw new DatabaseException("Error with deleting Event object from database");
            }
            stmt.close();
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when deleting Event object from database: " + e);
        }
    }

    /**
     *
     * @param associatedUsername String parameter for our SQL Select statement
     * @return The number of events tied to the associatedUsername
     * @throws DatabaseException An error performing the operation
     */
    public int getEventCountByAssociatedUsername(String associatedUsername) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            stmt = connection.prepareStatement("SELECT * FROM Event " +
                    "WHERE associatedUsername = ?");
            stmt.setString(1, associatedUsername);
            rs = stmt.executeQuery();

            while(rs.next()) {
                String one = rs.getString(1);
                String two = rs.getString(2);
                String three = rs.getString(3);
                Double four = rs.getDouble(4);
                Double five = rs.getDouble(5);
                String six = rs.getString(6);
                String seven = rs.getString(7);
                String eight = rs.getString(8);
                int nine = rs.getInt(9);
                count++;
            }
            if(rs != null) rs.close();
            if(stmt != null) stmt.close();
            return count;
        } catch (Exception e) {
            throw new DatabaseException("SQLException when selecting all Events: " + e);
        }
    }

    /**
     * Deletes all Event objects from the database associated with the associatedUsername
     * @param associatedUsername String identifier used to identify the Event to delete
     * @throws DatabaseException Error with database operation
     */
    public void deleteAllEventsForAssociatedUsername(String associatedUsername) throws DatabaseException{
        PreparedStatement stmt = null;

        try {
            stmt = connection.prepareStatement("DELETE FROM Event " +
                    "WHERE associatedUsername = ?");
            stmt.setString(1, associatedUsername);
            if (stmt.executeUpdate() < 1) {
                if(getEventCountByAssociatedUsername(associatedUsername) > 0) {
                    throw new DatabaseException("Error with deleting Event object from database");
                }
            }
            stmt.close();
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when deleting Event objects from database: " + e);
        }
    }

    /**
     * Empties all Event objects from the Event table
     * @throws DatabaseException Error with database operation
     */
    public void empty() throws DatabaseException{
        PreparedStatement stmt = null;
        try {
            int numEvents = getCountOfAllEvents();
            if(numEvents == 0) {
                return;
            }

            stmt = connection.prepareStatement(EMPTY_SQL);
            if (stmt.executeUpdate() < 1) {
                throw new DatabaseException("Error with emptying Event table");
            }
            stmt.close();
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when emptying Event table: " + e);
        }
    }

    /**
     * Updates an Event object in the database
     * @param event Event object to update
     * @throws DatabaseException Error with database operation
     */
    public void updateEvent(Event event) throws DatabaseException{
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(UPDATE_SQL);
            stmt.setString(1, event.getAssociatedUsername());
            stmt.setString(2, event.getPersonID());
            stmt.setDouble(3, event.getLatitude());
            stmt.setDouble(4, event.getLongitude());
            stmt.setString(5, event.getCountry());
            stmt.setString(6, event.getCity());
            stmt.setString(7, event.getEventType());
            stmt.setInt(8, event.getYear());
            stmt.setString(9, event.getEventID());
            if (stmt.executeUpdate() != 1) {
                throw new DatabaseException("Error with updating Event object in the database");
            }
            stmt.close();
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when updating Event object: " + e);
        }
    }

    /**
     * Gets all of the Event objects we have in our database
     * @return An Array of all Events
     * @throws DatabaseException An Error performing the operation.
     */
    public Event[] getAllEvents(String associatedUsername) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int numberOfEvents = getCountOfAllEvents(associatedUsername);
        Event[] data = new Event[numberOfEvents];

        try {
            stmt = connection.prepareStatement("SELECT * FROM Event WHERE associatedUsername = ?");
            stmt.setString(1, associatedUsername);
            rs = stmt.executeQuery();
            int arrayCounter = 0;

            while(rs.next()) {
                Event tempEvent = new Event(rs.getString(1), rs.getString(2));
                tempEvent.setPersonID(rs.getString(3));
                tempEvent.setLatitude(rs.getDouble(4));
                tempEvent.setLongitude(rs.getDouble(5));
                tempEvent.setCountry(rs.getString(6));
                tempEvent.setCity(rs.getString(7));
                tempEvent.setEventType(rs.getString(8));
                tempEvent.setYear(rs.getInt(9));

                data[arrayCounter] = tempEvent;
                arrayCounter++;
            }
            if(rs != null) rs.close();
            if(stmt != null) stmt.close();
        } catch (Exception e) {
            throw new DatabaseException("SQLException when selecting all Events: " + e);
        }
        return data;
    }

    /**
     * Returns an Event from the database if it exist in it
     * @param eventID String identifier unique to the Event object to retrieve
     * @return A Event object from the query
     * @throws DatabaseException Error with database operation
     */
    public Event getEventByEventID(String eventID) throws DatabaseException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Event event = null;
        try {
            stmt = connection.prepareStatement(SELECT_SQL);
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String associatedUsername = rs.getString(2);
                String personID = rs.getString(3);
                double latitude = rs.getDouble(4);
                double longitude = rs.getDouble(5);
                String country = rs.getString(6);
                String city = rs.getString(7);
                String eventType = rs.getString(8);
                int year = rs.getInt(9);

                event = new Event(eventID, associatedUsername, personID, latitude, longitude,
                        country, city, eventType, year);
            }
            if(rs != null) rs.close();
            if(stmt != null) stmt.close();
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when selecting Event object: " + e);
        }
        return event;
    }

    /**
     * Checks to see if an Event table exists and can be accessed
     * @return If the table is able to be accessed
     * @throws DatabaseException An error performing the operation
     */
    public boolean canAccessTable() throws DatabaseException {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Event");
            stmt.executeQuery();
            stmt.close();
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
