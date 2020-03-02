package service;

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


        return null;
    }
}
