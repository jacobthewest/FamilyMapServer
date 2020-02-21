package DataAccessTest;

import DataAccess.Database;
import DataAccess.DatabaseException;
import DataAccess.PersonDao;
import Model.Person;
import Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDaoTest {
    private Database db;
    private PersonDao personDao;
    private Person genericPerson;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        db.loadDriver();
        db.openConnection();
        db.initializeTables();
        db.commitConnection(true);

        personDao = new PersonDao();
        Connection tempConnection = db.getConnection();
        personDao.setConnection(tempConnection);
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.emptyDatabase();
        db.commitConnection(true);
        db.closeConnection();

        this.genericPerson = null;
        this.db = null;
        this.personDao = null;
    }

    /**
     * Inserts a non existent Person with correct data
     * @throws Exception Problem with the code used to test the insertion
     */
    @Test
    public void insertPass() throws Exception {
        Person returnedPerson = null;
        try {
            setGenericPerson();
            personDao.insertPerson(genericPerson);
            db.commitConnection(true);
            returnedPerson = personDao.getPersonByPersonID("personID");
        } catch (DatabaseException ex) {
            db.commitConnection(false);
        }

        assertEquals(returnedPerson.getPersonID(), "personID");
        assertEquals(returnedPerson.getAssociatedUsername(), "associatedUsername");
        assertEquals(returnedPerson.getFirstName(), "firstName");
        assertEquals(returnedPerson.getLastName(), "lastName");
        assertEquals(returnedPerson.getGender(), "m");
        assertEquals(returnedPerson.getFatherId(), "fatherID");
        assertEquals(returnedPerson.getMotherId(), "motherID");
        assertEquals(returnedPerson.getSpouseId(), "spouseID");
    }

    /**
     * Inserts a non existent Person, then tries to insert a duplicate Person
     * @throws Exception Problem with the code used to test the insertion
     */
    @Test
    public void insertFail() throws Exception {
        Person duplicatePerson = null;
        try {
            setGenericPerson();
            personDao.insertPerson(genericPerson);
            duplicatePerson = genericPerson;
            personDao.insertPerson(duplicatePerson);
            db.commitConnection(true);
        } catch (DatabaseException ex) {
            db.commitConnection(false);
        }
    }

    /**
     * Tests returning a Person that exists in the database
     * @throws Exception Problem with the code used to test the retrieval
     */
    @Test
    public void retrievePass() throws Exception {
        Person returnedPerson = null;
        try {
            setGenericPerson();
            insertGenericPerson();
            returnedPerson = personDao.getPersonByPersonID("personID");
        } catch (DatabaseException ex) {
            db.commitConnection(false);
        }

        assertEquals(genericPerson.getPersonID(), returnedPerson.getPersonID());
        assertEquals(genericPerson.getAssociatedUsername(), returnedPerson.getAssociatedUsername());
        assertEquals(genericPerson.getFirstName(), returnedPerson.getFirstName());
        assertEquals(genericPerson.getLastName(), returnedPerson.getLastName());
        assertEquals(genericPerson.getGender(), returnedPerson.getGender());
        assertEquals(genericPerson.getFatherId(), returnedPerson.getFatherId());
        assertEquals(genericPerson.getMotherId(), returnedPerson.getMotherId());
        assertEquals(genericPerson.getSpouseId(), returnedPerson.getSpouseId());
    }

    /**
     * Tries to return a user that does not exist in the database
     * @throws Exception Problem with the code used to test the retrieval
     */
    @Test
    public void retrieveFail() throws Exception {
        Person returnedPerson = null;
        try {
            setGenericPerson();
            insertGenericPerson();
            Person nonexistentPerson = new Person("heyDude","fakeUserName", "fakeF",
                    "fakeL", "f", "pops", "motherDearest", "wifey");
            returnedPerson = personDao.getPersonByPersonID("heyDude");
        } catch (DatabaseException ex) {
            db.commitConnection(false);
        }
        assertEquals(returnedPerson, null);
    }

    /**
     * Tries to clear all data from the User table
     * @throws Exception Problem with the code used to test the retrieval
     */
    @Test
    public void clearPass() throws Exception {
        try {
            setGenericPerson();
            insertGenericPerson();
            personDao.empty();
            db.commitConnection(true);
        } catch (DatabaseException ex) {
            db.commitConnection(false);
        }
        int newCount = personDao.getCountOfAllPersons();
        assertEquals(newCount, 0);
    }


    public void setGenericPerson() {
        this.genericPerson = new Person("personID","associatedUsername", "firstName",
                "lastName", "m", "fatherID", "motherID", "spouseID");
    }

    public void insertGenericPerson() throws Exception {
        setGenericPerson();
        personDao.insertPerson(genericPerson);
        db.commitConnection(true);
    }
}
