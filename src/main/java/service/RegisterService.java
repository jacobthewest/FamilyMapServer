package service;

import dataAccess.AuthTokenDao;
import dataAccess.Database;
import dataAccess.DatabaseException;
import dataAccess.UserDao;
import model.AuthToken;
import model.User;
import request.LoginRequest;
import result.ApiResult;
import result.RegisterResult;
import request.RegisterRequest;

import java.util.UUID;

/**
 * Contains functions used to register a new user. Implements the api route <code>/user/register</code>
 */
public class RegisterService {
    /**
     * Creates a new user account, generates 4 generations of ancestor data for the new
     * user, logs the user in, and returns an auth authToken.
     *
     * @param request A RegisterService object containing data needed to serve the API call
     * @return The result of registering the user
     */
     public RegisterResult register(RegisterRequest request) {
         // Error check the parameter
         if(request == null) {
             return new RegisterResult(ApiResult.REQUEST_PROPERTY_MISSING_OR_INVALID, "RegisterRequest is null");
         }
         if(request.getUser() == null) {
             return new RegisterResult(ApiResult.REQUEST_PROPERTY_MISSING_OR_INVALID,
                     "User in RegisterRequest is null");
         }

         try {
             // Set up database
             Database db = new Database();
             db.loadDriver();
             db.openConnection();
             db.initializeTables();
             db.commitConnection(true);

             // Create Daos
             UserDao userDao = new UserDao();
             AuthTokenDao authTokenDao = new AuthTokenDao();

             userDao.setConnection(db.getConnection());
             authTokenDao.setConnection(db.getConnection());

             // Retrieve data
             User userFromRequest = request.getUser();
             User retrievedUser = userDao.getUserByUserName(userFromRequest.getUserName());

             if(retrievedUser == null) {
                 // A user by that userName DOES NOT exist in database and may be added.

                 //---------- The following will now take place ----------//

                 // 1) Create a new user account
                 if(userFromRequest.getPersonID() == null) {
                     userFromRequest.setPersonID(UUID.randomUUID().toString());
                 }
                    userDao.insertUser(userFromRequest);
                    db.commitConnection(true);
                    db.closeConnection();

                 // 2) Generates 4 generations of ancestor data for the new user
                    FillService fillService = new FillService();
                    fillService.fill(userFromRequest.getUserName());

                 // 3) Log the user in
                    db.loadDriver();
                    db.openConnection();
                    authTokenDao.setConnection(db.getConnection());

                    AuthToken authToken = new AuthToken(userFromRequest.getUserName());
                    authTokenDao.insertAuthToken(authToken);
                    db.commitConnection(true);
                    db.closeConnection();

                    LoginRequest loginRequest = new LoginRequest(userFromRequest.getUserName(), userFromRequest.getPassword());
                    LoginService loginService = new LoginService();
                    loginService.login(loginRequest);

                 // 4) Close db and return an auth authToken.
                    db.closeConnection();
                    return new RegisterResult(authToken.getToken(), authToken.getUserName(), userFromRequest.getPersonID());
             } else {
                 // That userName has been taken. User may not be added by that userName.
                 db.closeConnection();
                 return new RegisterResult(ApiResult.USERNAME_TAKEN,
                         "userName: " + userFromRequest.getUserName() + " is already taken.");
             }
         } catch(DatabaseException e) {
             return new RegisterResult(ApiResult.INTERNAL_SERVER_ERROR, e.getMessage());
         }
     }
}
