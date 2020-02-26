package DataAccessTest;

import DataAccess.AuthTokenDao;
import DataAccess.Database;
import DataAccess.DatabaseException;
import Model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthTokenDaoTest {
    private Database db;
    private AuthTokenDao authTokenDao;
    private AuthToken authToken;

    @BeforeEach
    public void setUp() throws DatabaseException {
        db = new Database();
        db.loadDriver();
        db.openConnection();
        db.initializeTables();
        db.commitConnection(true);

        authTokenDao = new AuthTokenDao();
        Connection tempConnection = db.getConnection();
        authTokenDao.setConnection(tempConnection);
    }

    @AfterEach
    public void tearDown() throws DatabaseException {
        try {
            db.emptyDatabase();
            db.commitConnection(true);
            db.closeConnection();

            this.authToken = null;
            this.db = null;
            this.authTokenDao = null;
        } catch(Exception ex) {
            throw new DatabaseException("Jacob, error: " + ex);
        }

    }

    /**
     * Inserts a non existent AuthToken with correct data
     * @throws DatabaseException Problem with the code used to test the insertion
     */
    @Test
    public void insertPass() throws DatabaseException {
        AuthToken returnedAuthToken = null;
        authTokenDao.empty();
        db.commitConnection(true);
        try {
            setGenericAuthToken();
            insertGenericAuthToken();
            returnedAuthToken = authTokenDao.getAuthTokenByUserName("userName");
        } catch (DatabaseException ex) {
            db.commitConnection(false);
        }

        assertEquals(returnedAuthToken.getToken(), "token");
        assertEquals(returnedAuthToken.getUserName(), "userName");
    }

    /**
     * Inserts a non existent AuthToken, then tries to insert a duplicate AuthToken
     * @throws DatabaseException Problem with the code used to test the insertion
     */
    @Test
    public void insertDuplicate() throws DatabaseException {
        AuthToken duplicateAuthToken = null;
        try {
            setGenericAuthToken();
            insertGenericAuthToken();
            duplicateAuthToken = authToken;
            authTokenDao.insertAuthToken(duplicateAuthToken);
            db.commitConnection(true);
        } catch (DatabaseException ex) {
            db.commitConnection(false);
        }
    }

    /**
     * Tests inserting a AuthToken with a token that has already been used
     * @throws DatabaseException Error encountered while performing the operation
     */
    @Test
    public void insertAuthTokenByUsedToken() throws DatabaseException {
        boolean myCodeHandledIt;
        try {
            setGenericAuthToken();
            insertGenericAuthToken();
            AuthToken badDataAuthToken = new AuthToken("token", "badUserName");
            authTokenDao.insertAuthToken(badDataAuthToken);
            myCodeHandledIt = false;
            db.commitConnection(true);
        } catch(DatabaseException e) {
            db.commitConnection(false);
            myCodeHandledIt = true;
        }
        assertTrue(myCodeHandledIt);
    }


    /**
     * Tests returning an AuthToken that exists in the database
     * @throws DatabaseException Problem with the code used to test the retrieval
     */
    @Test
    public void retrievePass() throws DatabaseException {
        AuthToken returnedAuthToken = null;
        try {
            setGenericAuthToken();
            insertGenericAuthToken();
            returnedAuthToken = authTokenDao.getAuthTokenByUserName("userName");
        } catch (DatabaseException ex) {
            db.commitConnection(false);
        }

        assertEquals(authToken.getToken(), returnedAuthToken.getToken());
        assertEquals(authToken.getUserName(), returnedAuthToken.getUserName());
    }

    /**
     * Tries to return an AuthToken that does not exist in the database
     * @throws DatabaseException Problem with the code used to test the retrieval
     */
    @Test
    public void retrieveFail() throws DatabaseException {
        AuthToken returnedAuthToken = null;
        try {
            setGenericAuthToken();
            insertGenericAuthToken();
            returnedAuthToken = authTokenDao.getAuthTokenByUserName("abc");
        } catch (DatabaseException ex) {
            db.commitConnection(false);
        }
        assertEquals(returnedAuthToken, null);
    }

    /**
     * Tries to clear all data from the AuthToken table
     * @throws DatabaseException Problem with the code used to test the retrieval
     */
    @Test
    public void clearPass() throws DatabaseException {
        try {
            setGenericAuthToken();
            insertGenericAuthToken();
            authTokenDao.empty();
            db.commitConnection(true);
        } catch (DatabaseException ex) {
            db.commitConnection(false);
        }
        assertEquals(authTokenDao.getCountOfAllAuthTokens(), 0);
    }


    public void setGenericAuthToken() {
        this.authToken = new AuthToken("token", "userName");
    }

    public void insertGenericAuthToken() throws DatabaseException {
        setGenericAuthToken();
        authTokenDao.insertAuthToken(this.authToken);
        db.commitConnection(true);
    }
}
