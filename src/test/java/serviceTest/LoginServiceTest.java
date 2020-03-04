package serviceTest;

import dataAccess.*;
import model.AuthToken;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import request.LoginRequest;
import result.ApiResult;
import result.LoginResult;
import service.LoginService;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

public class LoginServiceTest {

    private UserDao userDao = null;
    private AuthTokenDao authTokenDao = null;
    private Database db = null;
    private LoginService loginService = null;
    private Connection connection = null;
    private User user = null;
    private AuthToken authToken = null;

    @BeforeEach
    public void setUp() throws DatabaseException {
        try {
            // Set up member variables
            loginService = new LoginService();
            userDao = new UserDao();
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
            userDao.setConnection(connection);
            authTokenDao.setConnection(connection);
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
            userDao = null;
            user = null;
            authToken = null;
            loginService = null;
            connection = null;
        } catch (DatabaseException e) {
            System.out.println("Problem in FillServiceTest tearDown(): " + e.getMessage());
        }
    }

    /**
     * Tests logging in with a valid userName and passWord
     */
    @Test
    public void loginPass() {
        setUser();
        insertUser();
        insertAuthToken(); // AuthToken associated with the user

        LoginRequest loginRequest = new LoginRequest(user.getUserName(), user.getPassWord());
        loginService = new LoginService();
        LoginResult loginResult = loginService.login(loginRequest);

        String token = getToken();

        assertTrue(loginResult.getSuccess());
        assertEquals(token, loginResult.getToken());
        assertEquals(user.getPersonID(), loginResult.getPersonID());
        assertEquals(user.getUserName(), loginResult.getUserName());
    }

    /**
     * Tests logging in with an incorrect password
     */
    @Test
    public void loginFail() {
        setUser();
        insertUser();
        user.setPassWord("badPassWord");
        insertAuthToken();

        LoginRequest loginRequest = new LoginRequest(user.getUserName(), user.getPassWord());
        loginService = new LoginService();
        LoginResult loginResult = loginService.login(loginRequest);

        assertFalse(loginResult.getSuccess());
        assertEquals(loginResult.getMessage(), ApiResult.REQUEST_PROPERTY_MISSING_OR_INVALID);
    }

    private void setUser() {
        user = new User("userName","passWord",
                "email@email.com","firstName","lastName",
                "m","personID");
    }

    private void insertUser() {
        try {
            userDao.insertUser(user);
            db.commitConnection(true);
        } catch(DatabaseException ex) {
            System.out.println("Error with setUser() in LoginServiceTest class: " + ex.getMessage());
        }
    }

    private void insertAuthToken() {
        try {
            authToken = new AuthToken("token", user.getUserName());
            authTokenDao.insertAuthToken(authToken);
            db.commitConnection(true);
        } catch(DatabaseException ex) {
            System.out.println("Error with insertAuthToken() in LoginServiceTest class: " + ex.getMessage());
        }
    }

    private String getToken() {
        try {
             authToken = authTokenDao.getAuthTokenByUserName(user.getUserName());
             return authToken.getToken();
        } catch(DatabaseException ex) {
            System.out.println("Error with getToken() in LoginServiceTest class: " + ex.getMessage());
        }
        return null;
    }
}
