package DataAccessTest;

import DataAccess.Database;
import DataAccess.DatabaseException;
import DataAccess.PersonDao;
import Model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDaoTest {
    private Database db;
    private PersonDao personDao;
    private Person genericPerson;
    private Person father;
    private Person mother;
    private Person spouse;

    @BeforeEach
    public void setUp() throws DatabaseException {
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
    public void tearDown() throws DatabaseException {
        db.emptyDatabase();
        db.commitConnection(true);
        db.closeConnection();

        this.genericPerson = null;
        this.father = null;
        this.mother = null;
        this.spouse = null;
        this.db = null;
        this.personDao = null;
    }

    /**
     * Inserts a non existent Person with correct data
     * @throws DatabaseException Problem with the code used to test the insertion
     */
    @Test
    public void insertPass() throws DatabaseException {
        Person returnedPerson = null;
        try {
            personDao.empty();
            db.commitConnection(true);
            setGenericPerson();
            insertGenericPerson();
            db.commitConnection(true);
            returnedPerson = personDao.getPersonByPersonID("personID");
        } catch (Exception ex) {
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
     * @throws DatabaseException Problem with the code used to test the insertion
     */
    @Test
    public void insertDuplicate() throws DatabaseException {
        Person duplicatePerson = null;
        try {
            setGenericPerson();
            insertGenericPerson();
            duplicatePerson = genericPerson;
            personDao.insertPerson(duplicatePerson);
            db.commitConnection(true);
        } catch (DatabaseException ex) {
            db.commitConnection(false);
        }
    }

    /**
     * Tests inserting a user with a personID already in the Database
     * @throws DatabaseException Error encountered while performing the operation
     */
    @Test
    public void insertPersonByUsedPersonID() throws DatabaseException {
        boolean myCodeHandledIt;
        try {
            setGenericPerson();
            insertGenericPerson();
            Person badDataPerson = new Person("personID", "nameUser", "Michael",
                    "chick", "Vick", "Loves", "Dog", "Fighting");
            personDao.insertPerson(badDataPerson);
            myCodeHandledIt = false;
            db.commitConnection(true);
        } catch(Exception e) {
            db.commitConnection(false);
            myCodeHandledIt = true;
        }
        assertTrue(myCodeHandledIt);
    }

    /**
     * Tests returning a Person that exists in the database
     * @throws DatabaseException Problem with the code used to test the retrieval
     */
    @Test
    public void retrievePass() throws DatabaseException {
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
     * @throws DatabaseException Problem with the code used to test the retrieval
     */
    @Test
    public void retrieveFail() throws DatabaseException {
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
     * @throws DatabaseException Problem with the code used to test the retrieval
     */
    @Test
    public void clearPass() throws DatabaseException {
        try {
            insertGenericPerson();
            personDao.empty();
            db.commitConnection(true);
        } catch (Exception ex) {
            db.commitConnection(false);
        }
        int newCount = personDao.getCountOfAllPersons();
        assertEquals(newCount, 0);
    }

    /**
     * Passing test for getting Person and Person's family members
     * @throws DatabaseException
     */
    @Test
    public void getAllPersonsPass() throws DatabaseException {
        boolean codeWorked;
        try {
            insertGenericPerson();
            insertFather();
            insertMother();
            insertSpouse();

            Person[] array = this.personDao.getAllPersons(this.genericPerson.getAssociatedUsername());
            assertPerson(this.genericPerson, array[0]); // Person
            assertPerson(this.father, array[1]); // Father
            assertPerson(this.mother, array[2]); // Mother
            assertPerson(this.spouse, array[3]); // Spouse

            codeWorked = true;
        } catch(Exception e) {
            codeWorked = false;
        }
        assertTrue(codeWorked);
    }

    /**
     * Failing test for getting Person and Person's family members
     * @throws DatabaseException
     */
    @Test
    public void getAllPersonsFail() throws DatabaseException {
        boolean codeWorked;
        try {
            insertGenericPerson();
            insertBadDad();
            insertMother();
            insertSpouse();

            Person[] array = this.personDao.getAllPersons(this.genericPerson.getAssociatedUsername());
            assertPerson(this.genericPerson, array[0]); // Person
            assertPerson(this.father, array[1]); // Father
            assertPerson(this.mother, array[2]); // Mother
            assertPerson(this.spouse, array[3]); // Spouse

            codeWorked = false;
        } catch(Exception e) {
            codeWorked = true;
        }
        assertTrue(codeWorked);
    }


    public void setGenericPerson() throws DatabaseException {
        this.genericPerson = new Person("personID","associatedUsername", "firstName",
                "lastName", "m", "fatherID", "motherID", "spouseID");
    }

    public void insertFather() throws DatabaseException {
        this.father = new Person("fatherID","fatherUsername", "daddy",
                "pops", "m", null, null, null);
        personDao.insertPerson(father);
        db.commitConnection(true);
    }

    public void insertBadDad() throws DatabaseException {
        this.father = new Person("nonExistentFatherPersonID","fatherUsername", "daddy",
                "pops", "m", null, null, null);
        personDao.insertPerson(father);
        db.commitConnection(true);
    }

    public void insertMother() throws DatabaseException {
        this.mother = new Person("motherID","motherUsername", "mommy",
                "motherDearest", "f", null, null, null);
        personDao.insertPerson(mother);
        db.commitConnection(true);
    }

    public void insertSpouse() throws DatabaseException {
        this.spouse = new Person("spouseID","spouseUsername", "hottie",
                "wifey", "f", null, null, null);
        personDao.insertPerson(spouse);
        db.commitConnection(true);
    }

    public void insertGenericPerson() throws DatabaseException {
        setGenericPerson();
        personDao.insertPerson(genericPerson);
        db.commitConnection(true);
    }

    public void assertPerson(Person initialPerson, Person person) {
        assertEquals(initialPerson.getPersonID(), person.getPersonID());
        assertEquals(initialPerson.getAssociatedUsername(), person.getAssociatedUsername());
        assertEquals(initialPerson.getFirstName(), person.getFirstName());
        assertEquals(initialPerson.getLastName(), person.getLastName());
        assertEquals(initialPerson.getGender(), person.getGender());
        assertEquals(initialPerson.getFatherId(), person.getFatherId());
        assertEquals(initialPerson.getMotherId(), person.getMotherId());
        assertEquals(initialPerson.getSpouseId(), person.getSpouseId());
    }
}
