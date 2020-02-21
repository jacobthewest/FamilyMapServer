package DataAccessTest;

import DataAccess.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    private Database db;

    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();

        setDbToNull();
    }

    @Test
    public void testCreateDatabaseObject() throws Exception {
        createDatabase();
    }

    @Test
    public void testLoadDriver() throws Exception{
        createDatabase();
        loadDriver();
    }

    @Test
    public void testOpenConnection() throws Exception {
        createDatabase();
        loadDriver();
        openConnection();
        AuthTokenDao tempAuthTokenTable = db.getAuthTokenTable();
        EventDao tempEventTable = db.getEventTable();
        PersonDao tempPersonTable = db.getPersonTable();
        UserDao tempUserTable = db.getUserTable();
        assertNotNull(tempAuthTokenTable);
        assertNotNull(tempEventTable);
        assertNotNull(tempPersonTable);
        assertNotNull(tempUserTable);
    }

    @Test
    public void testInitializeTables() throws Exception {
        try {
            createDatabase();
            loadDriver();
            openConnection();
            initializeTables();
            this.db.commitConnection(true);
        } catch(Exception ex) {
            this.db.commitConnection(false);
            throw new Exception("Error initializingTables: " + ex);
        } finally {
            closeConnection();
        }
    }

    public void closeConnection() throws Exception {
        this.db.closeConnection();
    }

    public void initializeTables() throws Exception {
        this.db.initializeTables();
    }

    public void openConnection() throws Exception {
        this.db.openConnection();
    }

    public void createDatabase() throws Exception {
        this.db = new Database();
    }

    public void loadDriver() throws Exception {
        this.db.loadDriver();
    }

    public void setDbToNull() {
        this.db = null;
    }
}
