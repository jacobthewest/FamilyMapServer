package serviceTest;

import dataAccess.*;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.ApiResult;
import result.RegisterResult;
import service.RegisterService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterServiceTest {

    private UserDao userDao = null;
    private Database db = null;
    private RegisterService registerService = null;
    private Connection connection = null;
    private User newUser = null;
    private User userWithExistingUserName = null;

    @BeforeEach
    public void setUp() throws DatabaseException {
        try {
            // Set up member variables
            registerService = new RegisterService();
            userDao = new UserDao();

            // Set up database and Daos
            db = new Database();
            db.loadDriver();
            db.openConnection();
            db.initializeTables();
            db.emptyTables();
            db.commitConnection(true);
            connection = db.getConnection();

            // Set dao connections
            userDao.setConnection(connection);
        } catch(DatabaseException e) {
            System.out.println("RegisterServiceTest setUp failed: " + e.getMessage());
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
            userDao = null;
            registerService = null;
            connection = null;
        } catch (DatabaseException e) {
            System.out.println("Problem in RegisterServiceTest tearDown(): " + e.getMessage());
        }
    }

    /**
     * Tests successfully registering a userName that does not yet exist
     */
    @Test
    public void registerPass() {
        newUser = new User("newUser","passWord","email@email.com",
                "firstName","lastName","m","personID");

        RegisterRequest registerRequest = new RegisterRequest(newUser);
        RegisterResult registerResult = registerService.register(registerRequest);

        assertTrue(registerResult.getSuccess());
        assertNotNull(registerResult.getToken());
        assertEquals(registerResult.getUserName(), newUser.getUserName());
        assertEquals(registerResult.getPersonID(), newUser.getPersonID());
    }

    /**
     * Tests failing to register by registering a userName that is already taken
     */
    @Test
    public void registerFail() {
        newUser = new User("newUser","passWord","email@email.com",
                "firstName","lastName","m","personID");
        userWithExistingUserName = new User("newUser","newPassWord","newEmail@email.com",
                "newFirstName","newLastName","f","newPersonID");

        insertNewUser();

        RegisterRequest registerRequest = new RegisterRequest(newUser);
        RegisterResult registerResult = registerService.register(registerRequest);

        assertFalse(registerResult.getSuccess());
        assertEquals(registerResult.getMessage(), ApiResult.USERNAME_TAKEN);
    }

    private void insertNewUser() {
        try {
            userDao.insertUser(newUser);
            db.commitConnection(true);
        } catch (DatabaseException e) {
            System.out.println("Error in insertNewUser() in RegisterServiceTest class: " + e.getMessage());
        }
    }
}
