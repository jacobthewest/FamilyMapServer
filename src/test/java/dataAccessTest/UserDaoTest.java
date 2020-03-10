package dataAccessTest;

import dataAccess.Database;
import dataAccess.DatabaseException;
import dataAccess.UserDao;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.sql.Connection;


public class UserDaoTest {
    private Database db;
    private UserDao userDao;
    private User genericUser;

    @BeforeEach
    public void setUp() throws DatabaseException {
        db = new Database();
        db.loadDriver();
        db.openConnection();
        db.initializeTables();
        db.commitConnection(true);

        userDao = new UserDao();
        Connection tempConnection = db.getConnection();
        userDao.setConnection(tempConnection);
    }

    @AfterEach
    public void tearDown() throws DatabaseException {
        db.emptyDatabase();
        db.commitConnection(true);
        db.closeConnection();

        this.genericUser = null;
        this.db = null;
        this.userDao = null;
    }

    /**
     * Inserts a non existent user with correct data
     * @throws DatabaseException Problem with the code used to test the insertion
     */
    @Test
    public void insertPass() throws DatabaseException {
        User returnedUser = null;
        userDao.empty();
        db.commitConnection(true);
        try {
            setGenericUser();
            userDao.insertUser(this.genericUser);
            db.commitConnection(true);
            returnedUser = userDao.getUserByUserName("userName");
        } catch (DatabaseException ex) {
            db.commitConnection(false);
        }

        assertEquals(returnedUser.getUserName(), "userName");
        assertEquals(returnedUser.getPassword(), "password");
        assertEquals(returnedUser.getEmail(), "email@gmail.com");
        assertEquals(returnedUser.getFirstName(), "firstName");
        assertEquals(returnedUser.getLastName(), "lastName");
        assertEquals(returnedUser.getGender(), "m");
        assertEquals(returnedUser.getPersonID(), "personID");
    }

    /**
     * Inserts a non existent user, then tries to insert a duplicate user
     * @throws DatabaseException Problem with the code used to test the insertion
     */
    @Test
    public void insertDuplicate() throws DatabaseException {
        User duplicateUser = null;
        try {
            setGenericUser();
            userDao.insertUser(genericUser);
            duplicateUser = genericUser;
            userDao.insertUser(duplicateUser);
            db.commitConnection(true);
        } catch (DatabaseException ex) {
            db.commitConnection(false);
        }
    }

    /**
     * Tests inserting a user with a gender other than "m" or "f"
     * @throws DatabaseException Error encountered while performing the operation
     */
    @Test
    public void insertUserWithNonOptionGender() throws DatabaseException {
        boolean myCodeHandledIt;
        try {
            User badDataUser = new User("badUserName", "pass@word1", "your_mom@gmail.com",
                    "Rick", "James", "dude", "abc123");
            userDao.insertUser(badDataUser);
            myCodeHandledIt = false;
            db.commitConnection(true);
        } catch(Exception e) {
            db.commitConnection(false);
            myCodeHandledIt = true;
        }
        assertTrue(myCodeHandledIt);
    }

    /**
     * Tests inserting a user with a userName that has already been used
     * @throws DatabaseException Error encountered while performing the operation
     */
    @Test
    public void insertUserByUsedUserName() throws DatabaseException {
        boolean myCodeHandledIt;
        try {
            setGenericUser();
            insertGenericUser();
            User badDataUser = new User("badUserName", "pass@word1", "your_mom@gmail.com",
                    "Rick", "James", "dude", "abc123");
            userDao.insertUser(badDataUser);
            myCodeHandledIt = false;
            db.commitConnection(true);
        } catch(Exception e) {
            db.commitConnection(false);
            myCodeHandledIt = true;
        }
        assertTrue(myCodeHandledIt);
    }


    /**
     * Tests returning a user that exists in the database
     * @throws DatabaseException Problem with the code used to test the retrieval
     */
    @Test
    public void retrievePass() throws DatabaseException {
        User returnedUser = null;
        try {
            setGenericUser();
            insertGenericUser();
            returnedUser = userDao.getUserByUserName("userName");
        } catch (DatabaseException ex) {
            db.commitConnection(false);
        }

        assertEquals(genericUser.getUserName(), returnedUser.getUserName());
        assertEquals(genericUser.getPassword(), returnedUser.getPassword());
        assertEquals(genericUser.getEmail(), returnedUser.getEmail());
        assertEquals(genericUser.getFirstName(), returnedUser.getFirstName());
        assertEquals(genericUser.getLastName(), returnedUser.getLastName());
        assertEquals(genericUser.getGender(), returnedUser.getGender());
        assertEquals(genericUser.getPersonID(), returnedUser.getPersonID());
    }

    /**
     * Tries to return a user that does not exist in the database
     * @throws DatabaseException Problem with the code used to test the retrieval
     */
    @Test
    public void retrieveFail() throws DatabaseException {
        User returnedUser = null;
        try {
            setGenericUser();
            insertGenericUser();
//            User nonexistentUser = new User("abc", "def", "ghi@email.com",
//                    "Chuck", "Norris", "m", "chuckID");
            returnedUser = userDao.getUserByUserName("abc");
        } catch (DatabaseException ex) {
            db.commitConnection(false);
        }
        assertEquals(returnedUser, null);
    }

    /**
     * Tries to clear all data from the User table
     * @throws DatabaseException Problem with the code used to test the retrieval
     */
    @Test
    public void clearPass() throws DatabaseException {
        try {
            setGenericUser();
            insertGenericUser();
            userDao.empty();
            db.commitConnection(true);
        } catch (DatabaseException ex) {
            db.commitConnection(false);
        }
        assertEquals(userDao.getCountOfAllUsers(), 0);
    }



    public void setGenericUser() {
        this.genericUser = new User("userName","password", "email@gmail.com",
                "firstName", "lastName", "m", "personID");
    }

    public void insertGenericUser() throws DatabaseException {
        setGenericUser();
        userDao.insertUser(genericUser);
        db.commitConnection(true);
    }
}

