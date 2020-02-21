package DataAccess;

import Model.User;
import Model.Event;
import Model.Person;

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
            "year NUMERIC, " +
            "PRIMARY KEY(eventID)" +
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
    private final String SELECT_SQL = "SELECT associatedUsername, personID, " +
            "latitude, longitude, country, city, eventType, year FROM Event " +
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
                return false;
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
     * Empties all Event objects from the Event table
     * @throws DatabaseException Error with database operation
     */
    public void empty() throws DatabaseException{
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(EMPTY_SQL);
            if (stmt.executeUpdate() != 1) {
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
            stmt.close();

            while (rs.next()) {
                String associatedUsername = rs.getString(1);
                String personID = rs.getString(2);
                double latitude = rs.getDouble(3);
                double longitude = rs.getDouble(4);
                String country = rs.getString(5);
                String city = rs.getString(6);
                String eventType = rs.getString(7);
                int year = rs.getInt(8);

                event = new Event(eventID, associatedUsername, personID, latitude, longitude,
                        country, city, eventType, year);
            }
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when selecting Event object: " + e);
        }
        return event;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
