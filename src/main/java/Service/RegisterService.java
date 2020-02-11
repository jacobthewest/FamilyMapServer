package Service;

import Result.RegisterResult;
import Request.RegisterRequest;
/**
 * Contains functions used to register a new user. Implements the api route <code>/user/register</code>
 */
public class RegisterService {
        /**
         * Creates a new user account, generates 4 generations of ancestor data for the new
         * user, logs the user in, and returns an auth token.
         *
         * @param request A RegisterService object containing data needed to serve the API call
         * @return the result of registering the user
         */
        public static RegisterResult register(RegisterRequest request) {
                return null;
        }
}
