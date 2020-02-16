package DataAccess;

import Model.Person;

/**
 * A class to access Person data from the Person table in the SQLite Database
 */
public class PersonDao {

    private Database database;

    /**
     * Creates a Person database access object to access SQL Database
     * @param db A Database object which enables us to speak with the Person table
     */
    public PersonDao(Database db) {
        setDatabase(db);
    }

    /**
     * Inserts a user into the SQLite Database if they do not already exist in it
     * @param person Person object to insert
     */
    public void insertPerson(Person person) {

    }

    /**
     * Deletes a Person from the SQLite Database if they still exist in it
     * @param personID String identifier of a Person object
     */
    public void deletePerson(String personID) {

    }

    /**
     * Empties all Person objects from the Person table
     */
    public void deleteAllPersons() {

    }

    /**
     * Updates a Person object in the database
     * @param person Person object to update
     */
    public void updatePerson(Person person) {

    }

    /**
     * Returns a Person from the database if they exist in it
     * @param personID String identifier unique to the Person object to retrieve
     * @return A Person object from the query
     */
    public Person getPersonByPersonID(String personID) {
        return null;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }
}
