package service;

import dataAccess.AuthTokenDao;
import dataAccess.Database;
import dataAccess.DatabaseException;
import dataAccess.PersonDao;
import model.AuthToken;
import model.Person;
import result.ApiResult;
import result.PersonResult;

import java.sql.Connection;

/**
 * Contains functions used to find a person by personId. Implements the api route
 * <code>/person/[personID]</code> and <code>/person</code>
 */
public class PersonService {
    /**
     * @param personID A String identifier for the Person to get from the database
     * @return The single Person object with the specified ID.
     */
    public PersonResult getPerson(String personID, AuthToken authToken) {
        // Error check the parameters
        if(personID == null) {
            return new PersonResult(ApiResult.INVALID_PERSON_ID_PARAM,"personID is null");
        }
        if(authToken.getToken() == null) {
            return new PersonResult(ApiResult.INVALID_AUTH_TOKEN, "authToken is null");
        }

        try {
            // Database setup
            Database db = new Database();
            db.loadDriver();
            db.openConnection();

            // Create Daos
            PersonDao personDao = new PersonDao();
            AuthTokenDao authTokenDao = new AuthTokenDao();

            personDao.setConnection(db.getConnection());
            authTokenDao.setConnection(db.getConnection());

            // Retrieve Person
            Person retrievedPerson = personDao.getPersonByPersonID(personID);

            // Error check the retrieved Person
            if(retrievedPerson == null) {
                db.closeConnection();
                return new PersonResult(ApiResult.INTERNAL_SERVER_ERROR,
                        "Retrieved Person is null and not found in the database");
            }

            String userNameToCheck = retrievedPerson.getAssociatedUsername();
            AuthToken authTokenToCheck = authTokenDao.getAuthTokenByUserName(userNameToCheck);
            if(!authTokenToCheck.getToken().equals(authToken.getToken())) {
                db.closeConnection();
                return new PersonResult(ApiResult.INVALID_AUTH_TOKEN,
                        "AuthToken provided does not belong to the provided userName.");
            }
            if(!authTokenToCheck.getUserName().equals(retrievedPerson.getAssociatedUsername())) {
                db.closeConnection();
                return new PersonResult(ApiResult.REQUESTED_PERSON_NO_RELATION,
                        "userName associated with authToken does not match with the Person's userName");
            }

            // No errors so create successful PersonResult.
            db.closeConnection();
            return new PersonResult(retrievedPerson);

        } catch(DatabaseException e) {
            return new PersonResult(ApiResult.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * @return Returns All Persons in the database
     */
    public PersonResult getAllPersons(AuthToken authToken) {
        // Error check authToken
        if(authToken == null) {return new PersonResult(ApiResult.INVALID_AUTH_TOKEN,
                "authToken object is null");}
        if(authToken.getToken() == null) {return new PersonResult(ApiResult.INVALID_AUTH_TOKEN,
                "token is null");}
        if(authToken.getUserName() == null) {return new PersonResult(ApiResult.INVALID_AUTH_TOKEN,
                "userName is null");}
        // Do the authToken and userName match?
        boolean match = false;
        match = authTokenAgreesWithSelf(authToken);
        if(!match) {
            return new PersonResult(ApiResult.INVALID_AUTH_TOKEN,
                    "userName and token don't match each other in the database.");
        }

        try {
            // Set up Database
            Database db = new Database();
            db.loadDriver();
            db.openConnection();

            // Create PersonDao
            PersonDao personDao = new PersonDao();
            personDao.setConnection(db.getConnection());

            // Retrieve Person array from PersonDao
            Person[] personArray = personDao.getAllPersons(authToken.getUserName());

            // Error check Person array from PersonDao
            if(personArray == null) {
                return new PersonResult(ApiResult.INVALID_AUTH_TOKEN, "Array of Persons returned from authToken is null.");
            }

            // Close db connection and return
            db.closeConnection();
            return new PersonResult(personArray);

        } catch(DatabaseException e) {
            return new PersonResult(ApiResult.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private boolean authTokenAgreesWithSelf(AuthToken authToken) {
        try {
            // Set up database
            Database db = new Database();
            db.loadDriver();
            db.openConnection();
            Connection connection = db.getConnection();

            // Set up AuthTokenDao
            AuthTokenDao authTokenDao = new AuthTokenDao();
            authTokenDao.setConnection(connection);

            // Try to retrieve from database
            AuthToken authTokenFromDB = authTokenDao.getAuthTokenByUserName(authToken.getUserName());
            db.closeConnection();

            if(authTokenFromDB == null) return false;

            boolean tokensMatch = false;
            boolean userNamesMatch = false;

            if(authToken.getToken().equals(authTokenFromDB.getToken())) tokensMatch = true;
            if(authToken.getUserName().equals(authTokenFromDB.getUserName())) userNamesMatch = true;

            if(tokensMatch && userNamesMatch) return true;
        } catch(DatabaseException e) {
            System.out.println("Error with authTokenAgreesWithSelf() in PersonService class");
        }
        return false;
    }
}
