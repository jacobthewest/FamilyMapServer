package DataAccessTest;

import DataAccess.*;
import Model.AuthToken;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;
import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    private Database db;
    private Connection connection;
    private UserDao userTable;
    private PersonDao personTable;
    private EventDao eventTable;
    private AuthTokenDao authTokenTable;

    @BeforeEach
    public void setUp() throws Exception{
        db = new Database();
        userTable = new UserDao();
        personTable = new PersonDao();
        eventTable = new EventDao();
        authTokenTable = new AuthTokenDao();
    }

    @AfterEach
    public void tearDown() throws Exception {
        db = null;
        connection = null;
        userTable = null;
        personTable = null;
        eventTable = null;
        authTokenTable = null;
    }

    /**
     * Tests the function that loads the database driver
     * @throws Exception Error with performing the operation
     */
    @Test
    public void testLoadDriver() throws Exception{
        boolean myCodeWorked;
        try {
            db.loadDriver();
            myCodeWorked = true;
        } catch(Exception e) {
            myCodeWorked = false;
        }
        assertTrue(myCodeWorked);
    }

    /**
     * Tests opening a connection for the database
     * @throws Exception Error with performing the operation
     */
    @Test
    public void testOpenConnection() throws Exception {
        boolean myCodeWorked;
        try {
            db.loadDriver();
            db.openConnection();
            myCodeWorked = true;
        } catch(Exception e) {
            myCodeWorked = false;
        }
        assertTrue(myCodeWorked);
        db.closeConnection();
    }

    /**
     * Tests setting the connection for the tables
     * @throws Exception Error with performing the operation
     */
    @Test
    public void testSettingTableConnections() throws Exception {
        boolean myCodeWorked = true;
        try {
            db.loadDriver();
            db.openConnection();
            connection = db.getConnection();

            userTable.setConnection(connection);
            personTable.setConnection(connection);
            eventTable.setConnection(connection);
            authTokenTable.setConnection(connection);
        } catch(Exception e) {
            myCodeWorked = false;
        }

        assertTrue(myCodeWorked);
        assertEquals(connection, userTable.getConnection());
        assertEquals(connection, personTable.getConnection());
        assertEquals(connection, eventTable.getConnection());
        assertEquals(connection, authTokenTable.getConnection());

        db.closeConnection();
    }

    /**
     * Tests setting up the tables correctly
     * @throws Exception Error with performing the operation
     */
    @Test
    public void testInitializeTablesPass() throws Exception {
        boolean myCodeWorked;
        try {
            db.loadDriver();
            db.openConnection();
            connection = db.getConnection();
            db.initializeTables();
            setTableConnections();
            db.commitConnection(true);

            PreparedStatement stmt1 =  connection.prepareStatement("SELECT * FROM Event");
            PreparedStatement stmt2 =  connection.prepareStatement("SELECT * FROM Person");
            PreparedStatement stmt3 =  connection.prepareStatement("SELECT * FROM User");
            PreparedStatement stmt4 =  connection.prepareStatement("SELECT * FROM AuthToken");

            stmt1.executeQuery();
            stmt2.executeQuery();
            stmt3.executeQuery();
            stmt4.executeQuery();

            stmt1.close();
            stmt2.close();
            stmt3.close();
            stmt4.close();

            myCodeWorked = true;
            db.closeConnection();
        } catch(Exception ex) {
            myCodeWorked = false;
        }
        assertTrue(myCodeWorked);
    }

    /**
     * Tests dropping all of the tables from the database
     * @throws Exception Error while performing the operation
     */
    @Test
    public void testEmptyDatabase() throws Exception {
        PreparedStatement stmt = null;
        try {
            emptyTableDatabaseTestSetUp();
            db.emptyDatabase();
            db.commitConnection(true);
            db.closeConnection();
        } catch(Exception e) {
            db.commitConnection(false);
        }

        assertFalse(authTokenTable.canAccessTable());
        assertFalse(userTable.canAccessTable());
        assertFalse(eventTable.canAccessTable());
        assertFalse(personTable.canAccessTable());
    }

    public void emptyTableDatabaseTestSetUp() throws Exception {
        db.loadDriver();
        db.openConnection();
        connection = db.getConnection();
        db.initializeTables();
        db.commitConnection(true);
        setTableConnections();
    }

    public void setTableConnections() {
        userTable.setConnection(connection);
        eventTable.setConnection(connection);
        authTokenTable.setConnection(connection);
        personTable.setConnection(connection);
    }
}
