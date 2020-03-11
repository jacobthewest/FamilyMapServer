package service;

import dataAccess.AuthTokenDao;
import dataAccess.Database;
import dataAccess.DatabaseException;
import dataAccess.PersonDao;
import model.AuthToken;
import model.Person;
import result.ApiResult;
import result.EventResult;
import result.PersonResult;

import java.sql.Connection;
import java.util.List;

/**
 * Contains functions used to find a person by personId. Implements the api route
 * <code>/person/[personID]</code> and <code>/person</code>
 */
public class PersonService {
    /**
     * @param personID A String identifier for the Person to get from the database
     * @return The single Person object with the specified ID.
     */
    public PersonResult getPerson(String personID, String authToken) {
        // Error check the parameters
        if(personID == null) {
            return new PersonResult(ApiResult.INVALID_PERSON_ID_PARAM); // ,"personID is null"
        }
        if(authToken == null) {
            return new PersonResult(ApiResult.INVALID_AUTH_TOKEN); //,"authToken is null"
        }

        try {
            // Database setup
            Database db = new Database();
            db.loadDriver();
            db.openConnection();
            db.initializeTables();
            db.commitConnection(true);

            // Create Daos
            PersonDao personDao = new PersonDao();
            AuthTokenDao authTokenDao = new AuthTokenDao();

            personDao.setConnection(db.getConnection());
            authTokenDao.setConnection(db.getConnection());


            AuthToken returnedAuthToken = authTokenDao.getAuthTokenByToken(authToken);
            // Error check authToken
            if(returnedAuthToken == null) {
                db.closeConnection();
                return new PersonResult(ApiResult.INVALID_AUTH_TOKEN); //, "authToken is null"
            }
            if(returnedAuthToken.getToken() == null) {
                db.closeConnection();
                return new PersonResult(ApiResult.INVALID_AUTH_TOKEN); //, "authToken is null"
            }

            // Get the userName from the authToken
            String userNameFromAuthToken = returnedAuthToken.getUserName();
            // Get the associatedUserName from the personID
            Person returnedPerson = personDao.getPersonByPersonID(personID);
            if(returnedPerson == null) {
                db.closeConnection();
                return new PersonResult(ApiResult.INVALID_AUTH_TOKEN); // returnedPerson is null
            }

            String associatedUserNameFromPersonID = returnedPerson.getAssociatedUsername();
            // If they are NOT the same then throw an invalid auth token error
            if(!associatedUserNameFromPersonID.equals(userNameFromAuthToken)) {
                db.closeConnection();
                return new PersonResult(ApiResult.INVALID_AUTH_TOKEN); // Person Wrong User
            }


            // Retrieve Person
            Person retrievedPerson = personDao.getPersonByPersonID(personID);

            // Error check the retrieved Person
            if(retrievedPerson == null) {
                db.closeConnection();
                return new PersonResult(ApiResult.INTERNAL_SERVER_ERROR); //,"Retrieved Person is null and not found in the database"
            }

            String userNameToCheck = retrievedPerson.getAssociatedUsername();
            AuthToken authTokenToCheck = authTokenDao.getAuthTokenByUserName(userNameToCheck);

            if(authTokenToCheck == null) {
                db.closeConnection();
                return new PersonResult(ApiResult.INVALID_AUTH_TOKEN);//",AuthToken provided does not belong to the provided userName."
            }

            boolean match = false;
            match = authTokenAgreesWithSelf(returnedAuthToken);

            if(!match) {
                db.closeConnection();
                return new PersonResult(ApiResult.INVALID_AUTH_TOKEN);//",AuthToken provided does not belong to the provided userName."
            }
            if(!authTokenToCheck.getUserName().equals(retrievedPerson.getAssociatedUsername())) {
                db.closeConnection();
                return new PersonResult(ApiResult.REQUESTED_PERSON_NO_RELATION); //,"userName associated with authToken does not match with the Person's userName"
            }

            // No errors so create successful PersonResult.
            db.closeConnection();
            return new PersonResult(retrievedPerson);

        } catch(DatabaseException e) {
            return new PersonResult(ApiResult.INTERNAL_SERVER_ERROR); //, e.getMessage()
        }
    }

    /**
     * @return Returns All Persons in the database
     */
    public PersonResult getAllPersons(String authToken) {
        // Error check authToken
        if(authToken == null) {return new PersonResult(ApiResult.INVALID_AUTH_TOKEN);} //,"authToken object is null"

        try {
            // Set up Database
            Database db = new Database();
            db.loadDriver();
            db.openConnection();
            db.initializeTables();
            db.commitConnection(true);

            // Create PersonDao
            PersonDao personDao = new PersonDao();
            AuthTokenDao authTokenDao = new AuthTokenDao();
            personDao.setConnection(db.getConnection());
            authTokenDao.setConnection(db.getConnection());

            AuthToken returnedAuthToken = authTokenDao.getAuthTokenByToken(authToken);
            db.closeConnection();

            if(returnedAuthToken == null) {
                return new PersonResult(ApiResult.INVALID_AUTH_TOKEN); //, "authToken is not found in database"
            }

            // Error check the authToken
            if(returnedAuthToken.getToken() == null) {
                return new PersonResult(ApiResult.INVALID_AUTH_TOKEN);} //, "authToken is null"
            if(returnedAuthToken.getUserName() == null) {
                return new PersonResult(ApiResult.INVALID_AUTH_TOKEN);} //,"userName is null"
            // Do the authToken and userName match?
            boolean match = false;
            match = authTokenAgreesWithSelf(returnedAuthToken);
            if(!match) {
                return new PersonResult(ApiResult.INVALID_AUTH_TOKEN); //",userName and authToken don't match each other in the database."
            }

            // Retrieve Person array from PersonDao
            db.loadDriver();
            db.openConnection();
            personDao.setConnection(db.getConnection());
            Person[] personArray = personDao.getAllPersons(returnedAuthToken.getUserName());
            db.closeConnection();

            // Error check Person array from PersonDao
            if(personArray == null) {
                return new PersonResult(ApiResult.INVALID_AUTH_TOKEN); //, "Array of Persons returned from authToken is null."
            }

            // Close db connection and return
            return new PersonResult(personArray);

        } catch(DatabaseException e) {
            return new PersonResult(ApiResult.INTERNAL_SERVER_ERROR); //, e.getMessage()
        }
    }

    private boolean authTokenAgreesWithSelf(AuthToken authToken) {
        try {
            // Set up database
            Database db = new Database();
            db.loadDriver();
            db.openConnection();
            db.initializeTables();
            db.commitConnection(true);
            Connection connection = db.getConnection();

            // Set up AuthTokenDao
            AuthTokenDao authTokenDao = new AuthTokenDao();
            authTokenDao.setConnection(connection);

            // Try to retrieve from database
            List<AuthToken> authTokensFromUsername = authTokenDao.getAllAuthTokensByUsername(authToken.getUserName());
            db.closeConnection();

            if(authTokensFromUsername == null) return false;
            if(authTokensFromUsername.size() == 0) return false;

            boolean tokensMatch = false;
            boolean userNamesMatch = false;

            for(int i = 0; i < authTokensFromUsername.size(); i++) {
                if(authToken.getUserName().equals(authTokensFromUsername.get(i).getUserName())) userNamesMatch = true;
                if(authToken.getToken().equals(authTokensFromUsername.get(i).getToken())) tokensMatch = true;
                if(tokensMatch && userNamesMatch) return true;
            }
            return false;
        } catch(DatabaseException e) {
            System.out.println("Error with authTokenAgreesWithSelf() in PersonService class");
        }
        return false;
    }
}
