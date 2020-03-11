package dataAccess;

import model.Person;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class to access Person data from the Person table in the SQLite Database
 */
public class PersonDao {

    private Connection connection;
    private final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS Person " +
            "(" +
            "personID TEXT NOT NULL, " +
            "associatedUsername TEXT NOT NULL, " +
            "firstName TEXT NOT NULL, " +
            "lastName TEXT NOT NULL, " +
            "gender TEXT NOT NULL, " +
            "fatherID TEXT, " +
            "motherID TEXT, " +
            "spouseID TEXT, " +
            "PRIMARY KEY(personID)" +
            ");";
    private final String DROP_SQL = "DROP TABLE Person";
    private final String INSERT_SQL = "INSERT INTO Person " +
            "(personID, associatedUsername, firstName, lastName, gender, " +
            "fatherID, motherID, spouseID) " +
            "VALUES (?,?,?,?,?,?,?,?);";
    private final String DELETE_SQL = "DELETE FROM Person " +
            "WHERE personID = ?;";
    private final String EMPTY_SQL = "DELETE FROM Person";
    private final String UPDATE_SQL = "UPDATE Person" +
            "SET associatedUsername = ?, firstName = ?, lastName = ?, " +
            "gender = ?, fatherID = ?, motherID = ?, spouseID = ?" +
            "WHERE personID = ?";
    private final String SELECT_SQL = "SELECT personID, associatedUsername, firstName, " +
            "lastName, gender, fatherID, motherID, spouseID FROM Person " +
            "WHERE personID = ?";

    /**
     * Creates a Person database access object to access SQL Database
     */
    public PersonDao() {}

    /**
     *  Resets the Persons Table
     * @throws DatabaseException Error with database operation
     */
    public void resetTable() throws DatabaseException {
        PreparedStatement stmtDrop = null;
        PreparedStatement stmtCreate = null;
        try {
            stmtDrop = connection.prepareStatement(DROP_SQL);
            stmtDrop.executeUpdate();
            stmtDrop.close();

            stmtCreate = connection.prepareStatement(CREATE_SQL);
            stmtCreate.executeUpdate();
            stmtCreate.close();
        }
        catch (SQLException e) {
            throw new DatabaseException("Error resetting Person table: " + e);
        }
    }

    /**
     * Creates a Person table in the database if it doesn't already exist
     * @throws DatabaseException Error with database operation
     */
    public void createTable() throws DatabaseException {
        try {
            PreparedStatement UserTableStmt = connection.prepareStatement(CREATE_SQL);
            UserTableStmt.executeUpdate();
            UserTableStmt.close();
        } catch(Exception e) {
            throw new DatabaseException("Error creating a Person table: " + e);
        }
    }

    /**
     * Gets a count of all Persons in the Person table
     * @return The number of Users in the Person table
     */
    public int getCountOfAllPersons() throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;
        try {
            stmt = connection.prepareStatement("SELECT * FROM Person");
            stmt.executeQuery();
            rs = stmt.executeQuery();
            while(rs.next()) {
                count++;
            }
            stmt.close();
            rs.close();
            return count;
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when getting count from Person table: " + e);
        }
    }

    /**
     * Gets a count of all Persons in the Person table
     * @return The number of Users in the Person table
     */
    public int getCountOfAllPersons(String associatedUsername) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;
        try {
            stmt = connection.prepareStatement("SELECT * FROM Person WHERE associatedUsername = ?");
            stmt.setString(1, associatedUsername);
            stmt.executeQuery();
            rs = stmt.executeQuery();
            while(rs.next()) {
                count++;
            }
            stmt.close();
            rs.close();
            return count;
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when getting count from Person table: " + e);
        }
    }


    /**
     * Inserts a user into the SQLite Database if they do not already exist in it
     * @return If the user was successfully inserted
     * @param person Person object to insert
     * @throws DatabaseException Error with database operation
     */
    public boolean insertPerson(Person person) throws DatabaseException {
        PreparedStatement stmt = null;
        try {

            Person tempPerson = getPersonByPersonID(person.getPersonID());
            if(tempPerson != null) {
                throw new DatabaseException("User already exist with personID: " + person.getPersonID());
            }

            if(!person.getGender().equals("m") && !person.getGender().equals("f")) {
                throw new DatabaseException("Gender provided other than \"m\" or \"f\"");
            }

            stmt = connection.prepareStatement(INSERT_SQL);
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherId());
            stmt.setString(7, person.getMotherId());
            stmt.setString(8, person.getSpouseId());
            if (stmt.executeUpdate() != 1) {
                throw new DatabaseException("Error with inserting Person object into database");
            }
            stmt.close();
            return true;
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when inserting Person object: " + e);
        }
    }

    /**
     * Deletes a Person from the SQLite Database if they still exist in it
     * @param personID String identifier of a Person object
     * @throws DatabaseException Error with database operation
     */
    public void deletePerson(String personID) throws DatabaseException {
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement("SELECT * FROM PERSON WHERE personID = ?");
            stmt.setString(1, personID);
            ResultSet rs = stmt.executeQuery();
            Person isExisting = null;
            while(rs.next()) {
                isExisting = new Person(rs.getString(1), rs.getString(2));
            }

            if(isExisting == null) return;


            stmt = connection.prepareStatement(DELETE_SQL);
            stmt.setString(1, personID);
            if (stmt.executeUpdate() != 1) {
                throw new DatabaseException("Error with deleting person object from database");
            }
            stmt.close();
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when deleting person object: " + e);
        }
    }

    /**
     * Empties all Person objects from the Person table
     * @throws DatabaseException Error with database operation
     */
    public void empty() throws DatabaseException, SQLException {
        PreparedStatement stmt = null;
        try {
            int numPersons = getCountOfAllPersons();
            if(numPersons == 0) {
                return;
            }

            stmt = connection.prepareStatement(EMPTY_SQL);
            if (stmt.executeUpdate() < 1) {
                throw new DatabaseException("Error with emptying Person table");
            }
            stmt.close();
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when emptying Person table: " + e);
        }
    }

    /**
     * Updates a Person object in the database
     * @param person Person object to update
     * @throws DatabaseException Error with database operation
     */
    public void updatePerson(Person person) throws DatabaseException {
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(INSERT_SQL);
            stmt.setString(1, person.getAssociatedUsername());
            stmt.setString(2, person.getFirstName());
            stmt.setString(3, person.getLastName());
            stmt.setString(4, person.getGender());
            stmt.setString(5, person.getFatherId());
            stmt.setString(6, person.getMotherId());
            stmt.setString(7, person.getSpouseId());
            stmt.setString(8, person.getPersonID());
            if (stmt.executeUpdate() != 1) {
                throw new DatabaseException("Error with updating Person object in the database");
            }
            stmt.close();
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when updating Person object: " + e);
        }
    }

    /**
     * Returns a Person from the database if they exist in it
     * @param personID String identifier unique to the Person object to retrieve
     * @return A Person object from the query
     * @throws DatabaseException Error with database operation
     */
    public Person getPersonByPersonID(String personID) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Person person = null;
        try {
            stmt = connection.prepareStatement(SELECT_SQL);
            stmt.setString(1, personID);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String associatedUsername = rs.getString(2);
                String firstName = rs.getString(3);
                String lastName = rs.getString(4);
                String gender = rs.getString(5);
                String fatherID = rs.getString(6);
                String motherID = rs.getString(7);
                String spouseID = rs.getString(8);
                person = new Person(personID, associatedUsername, firstName, lastName, gender,
                        fatherID, motherID, spouseID);
            }
            rs.close();
            stmt.close();
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when selecting Person object: " + e);
        }
        return person;
    }

    /**
     * Returns all immediate family members of the Person with the associatedUsername
     *
     * @param associatedUsername the associatedUsername of the Person for immediate
     *                           family member retrieval
     * @return Array of Person objects representing the Person, and if not null, the Person's
     *                           father, mother, and spouse. Array order is Person, Father,
     *                           Mother, Spouse
     * @throws DatabaseException Error performing the operation
     */
    public Person[] getAllPersons(String associatedUsername) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int numberOfPersons = getCountOfAllPersons(associatedUsername);
        List<Person> list = new ArrayList<Person>();

        try {
            stmt = connection.prepareStatement("SELECT * FROM PERSON WHERE " +
                    "associatedUsername = ?");
            stmt.setString(1, associatedUsername);
            rs = stmt.executeQuery();

            while(rs.next()) {
                Person tempPerson = new Person(rs.getString(1), rs.getString(2));
                tempPerson.setFirstName(rs.getString(3));
                tempPerson.setLastName(rs.getString(4));
                tempPerson.setGender(rs.getString(5));
                tempPerson.setFatherId(rs.getString(6));
                tempPerson.setMotherId(rs.getString(7));
                tempPerson.setSpouseId(rs.getString(8));

                list.add(tempPerson);
            }
            if(rs != null) rs.close();
            if(stmt != null) stmt.close();
        } catch(Exception e) {
            return null;
        }
        Person[] data = new Person[list.size()];
        list.toArray(data);
        return data;
    }


    public Person getPersonByAssociatedUsername(String associatedUsername) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Person person = null;
        try {
            stmt = connection.prepareStatement("SELECT personID, associatedUsername, firstName, " +
                    "lastName, gender, fatherID, motherID, spouseID FROM Person " +
                    "WHERE associatedUsername = ?");
            stmt.setString(1, associatedUsername);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String personID = rs.getString(1);
                String firstName = rs.getString(3);
                String lastName = rs.getString(4);
                String gender = rs.getString(5);
                String fatherID = rs.getString(6);
                String motherID = rs.getString(7);
                String spouseID = rs.getString(8);
                person = new Person(personID, associatedUsername, firstName, lastName, gender,
                        fatherID, motherID, spouseID);
            }
            if(rs != null) rs.close();
            if(stmt != null) stmt.close();
        }
        catch (Exception e) {
            throw new DatabaseException("SQLException when selecting Person object: " + e);
        }
        return person;
    }

    /**
     * Checks to see if a Person table exists and can be accessed
     * @return If the table is able to be accessed
     * @throws DatabaseException An error performing the operation
     */
    public boolean canAccessTable() throws DatabaseException {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Person");
            stmt.executeQuery();
            stmt.close();
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    /**
     * Creates a person object from a ResultSet that came from a query
     * @param rs A ResultSet that came from a query
     * @return A person object created from the ResultSet
     * @throws DatabaseException An Error with the operation.
     */
    public Person getSelf(ResultSet rs) throws DatabaseException {
        try {
            String personID = rs.getString(1);
            String associatedUsername = rs.getString(2);
            String firstName = rs.getString(3);
            String lastName = rs.getString(4);
            String gender = rs.getString(5);
            String fatherID = rs.getString(6);
            String motherID = rs.getString(7);
            String spouseID = rs.getString(8);

            Person self = new Person(personID, associatedUsername, firstName, lastName, gender,
                    fatherID, motherID, spouseID);
            return self;
        } catch(Exception e) {
            return null;
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
