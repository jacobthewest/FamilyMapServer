package service;

import dataAccess.AuthTokenDao;
import dataAccess.Database;
import dataAccess.DatabaseException;
import dataAccess.UserDao;
import model.AuthToken;
import model.User;
import result.ApiResult;
import result.LoginResult;
import request.LoginRequest;

/**
 * A class used to log a user into the server and serve the API route <code>user/login</code>.
 */
public class LoginService {
    /**
     * Logs in the user and returns an auth token.
     *
     * @param request An instance of LoginRequest with userName and password to login
     * @return An AuthToken needed to maintain the User's login status
     */
    public static LoginResult login(LoginRequest request) {
        // Error check the loginRequest
        if(request.getPassword() == null) {
            return new LoginResult(ApiResult.REQUEST_PROPERTY_MISSING_OR_INVALID,
                    "LoginRequest password is null");
        }
        if(request.getUserName() == null) {
            return new LoginResult(ApiResult.REQUEST_PROPERTY_MISSING_OR_INVALID,
                    "LoginRequest userName is null");
        }

        String userName = request.getUserName();
        String password = request.getPassword();

        try {
            // Set up the database side of things
            Database db = new Database();
            db.loadDriver();
            db.openConnection();

            // Create and open User a UserDao
            UserDao userDao = new UserDao();
            userDao.setConnection(db.getConnection());

            // Get specified User from the database
            User userToVerify = userDao.getUserByUserName(userName);
            if(userToVerify == null) {
                db.closeConnection();
                return new LoginResult(ApiResult.INTERNAL_SERVER_ERROR,
                        "User to login was not found in the database.");
            }

            // Verify if the User's password from the database matches the password from the request
            if(userToVerify.getPassWord() != password) {
                db.closeConnection();
                return new LoginResult(ApiResult.INTERNAL_SERVER_ERROR,
                        "Provided password does not match password found in database.");
            } else {
                // The passwords match! Now we need to retrieve the user's authToken
                AuthTokenDao authTokenDao = new AuthTokenDao();
                AuthToken authToken = authTokenDao.getAuthTokenByUserName(userName);

                // Create successful login
                // Close db connection and return
                db.closeConnection();
                return new LoginResult(authToken.getToken(), userName, userToVerify.getPersonID());
            }
        } catch(DatabaseException e) {
            return new LoginResult(ApiResult.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
