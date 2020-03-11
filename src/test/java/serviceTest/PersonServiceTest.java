package serviceTest;

import dataAccess.*;
import model.AuthToken;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.PersonResult;
import service.PersonService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonServiceTest {

    private PersonDao personDao = null;
    private AuthTokenDao authTokenDao = null;
    private Database db = null;
    private PersonService personService = null;
    private Connection connection = null;
    private Person person = null;
    private AuthToken authToken = null;
    private Person[] personList = null;

    @BeforeEach
    public void setUp() throws DatabaseException {
        try {
            // Set up member variables
            personService = new PersonService();
            authTokenDao = new AuthTokenDao();
            personDao = new PersonDao();

            // Set up database and Daos
            db = new Database();
            db.loadDriver();
            db.openConnection();
            db.initializeTables();
            db.emptyTables();
            db.commitConnection(true);
            connection = db.getConnection();

            // Set dao connections
            authTokenDao.setConnection(connection);
            personDao.setConnection(connection);
        } catch(DatabaseException e) {
            System.out.println("PersonServiceTest setUp failed: " + e.getMessage());
        }
    }

    /**
     * Tests retrieving a Person already in the database with a correct authToken
     */
    @Test
    public void getSinglePersonPass() {
        setPerson();
        insertPeople();
        setAuthToken();
        insertAuthToken();

        try {
            PersonResult personResult = personService.getPerson(person.getPersonID(), authToken.getToken());
            assertTrue(personResult.getSuccess());
            Person tempPerson = personResult.getPerson();
            assertPersonsEqual(tempPerson, person, false);
            db.closeConnection();
        } catch(Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    /**
     * Tests failure to retrieve a Person already in the database with a personID that
     * isn't tied to an existing Person in the database
     */
    @Test
    public void getSinglePersonFail() {
        setPerson();
        insertPeople();
        setAuthToken();
        insertAuthToken();

        try {
            PersonResult personResult = personService.getPerson("badPersonID", authToken.getToken());
            assertFalse(personResult.getSuccess());
        } catch(Exception e) {
            e.printStackTrace();
        }
        try {
            db.closeConnection();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests retrieving all family Persons of the userName in the authToken
     */
    @Test
    public void getAllPersonsPass() {
        setPerson();
        insertPeople();
        setAuthToken();
        insertAuthToken();

        PersonResult personResult = personService.getAllPersons(authToken.getToken());
        Person[] personArray = personResult.getData();
        int numPeopleBack = personArray.length;
        assertEquals(numPeopleBack, personList.length);
        assertTrue(personResult.getSuccess());
        try {
            db.closeConnection();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests failure to retrieve all Persons by providing an invalid authToken
     */
    @Test
    public void getAllPersonsFail() {
        setPerson();
        insertPeople();
        setAuthToken();
        insertAuthToken();

        AuthToken badAuthToken = new AuthToken(";lkajl;kajdff", "useName");
        boolean codeWorked = false;

        try {
            PersonResult personResult = personService.getAllPersons(badAuthToken.getToken());
            Person[] personArray = personResult.getData();
            if(personResult.getMessage().toLowerCase().contains("error")) codeWorked = true;
            db.closeConnection();
        } catch(Exception e) {
            codeWorked = true;
        }

        assertTrue(codeWorked);
    }

    @AfterEach
    public void tearDown() {
        try {
            // Close database
            db.loadDriver();
            db.openConnection();
            db.initializeTables();
            db.emptyDatabase();
            db.commitConnection(true);
            db.closeConnection();

            // Set everything to null
            db = null;
            personDao = null;
            person = null;
            personList = null;
            authTokenDao = null;
            authToken = null;
            personService = null;
            connection = null;
        } catch (DatabaseException e) {
            System.out.println("Problem in PersonServiceTest tearDown(): " + e.getMessage());
        }
    }

    /**f
     * Sets the person member variable
     */
    private void setPerson() {
        person = new Person("p1", "a1", "f1", "l1",
                "m", "p5", "p4", "p2");
    }

    /**
     * Inserts a family of Persons into the database
     */
    private void insertPeople() {
        try {
            Person spouse = new Person("p2", "a1", "f2", "l2",
                    "f", "f2", "m2", "p1");
            Person random = new Person("p3", "a1", "f3", "l3",
                    "m", "f3", "m3", "s3");
            Person mother = new Person("p4", "a1", "f4", "l4",
                            "f", "f4", "m4", "s4");
            Person father = new Person("p5", "a1", "f5", "l5",
                    "m", "f5", "m5", "s5");

            personDao.insertPerson(person);
            personDao.insertPerson(spouse);
            personDao.insertPerson(random);
            personDao.insertPerson(mother);
            personDao.insertPerson(father);

            db.commitConnection(true);

            personList = new Person[5];
            personList[0] = person;
            personList[1] = father;
            personList[2] = mother;
            personList[3] = spouse;
            personList[4] = random;

        } catch(DatabaseException ex) {
            System.out.println("Problem in people() in PersonServiceTest class: " + ex.getMessage());
        }
    }

    /**
     * Sets the authToken member variable
     */
    private void setAuthToken() {
        authToken = new AuthToken("authToken", person.getAssociatedUsername());
    }

    /**
     * Inserts the authToken member variable into the database
     */
    private void insertAuthToken() {
        try {
            authTokenDao.insertAuthToken(authToken);
            db.commitConnection(true);
        } catch(DatabaseException ex) {
            System.out.println("Problem in insertAuthToken() in PersonServiceTest class: " + ex.getMessage());
        }
    }

    /**
     * Gets an authToken from the database using the person member variable
     * @return An AuthToken from the database
     */
    private AuthToken getAuthTokenFromDatabase() {
        try {
            AuthToken authTokenFromDB = authTokenDao.getAuthTokenByUserName(person.getAssociatedUsername());
            return authTokenFromDB;
        } catch(DatabaseException ex) {
            System.out.println("Error with getToken() in LoginServiceTest class: " + ex.getMessage());
        }
        return null;
    }

    /**
     * Compares two Person objects to be equal
     * @param one First Person
     * @param two Second Person
     * @param personTwoIsNull Boolean value saying if the Second Person object is null.
     *                        It may be null depending on the function call.
     */
    private void assertPersonsEqual(Person one, Person two, boolean personTwoIsNull) {

        if(personTwoIsNull) {
            assertEquals(one.getPersonID(), null);
            assertEquals(one.getAssociatedUsername(), null);
            assertEquals(one.getFirstName(), null);
            assertEquals(one.getLastName(), null);
            assertEquals(one.getGender(), null);
            assertEquals(one.getFatherId(), null);
            assertEquals(one.getMotherId(), null);
            assertEquals(one.getSpouseId(), null);
        } else {
            assertEquals(one.getPersonID(), two.getPersonID());
            assertEquals(one.getAssociatedUsername(), two.getAssociatedUsername());
            assertEquals(one.getFirstName(), two.getFirstName());
            assertEquals(one.getLastName(), two.getLastName());
            assertEquals(one.getGender(), two.getGender());
            assertEquals(one.getFatherId(), two.getFatherId());
            assertEquals(one.getMotherId(), two.getMotherId());
            assertEquals(one.getSpouseId(), two.getSpouseId());
        }
    }
}
