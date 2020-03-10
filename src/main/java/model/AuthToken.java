package model;

import java.util.UUID;

/**
 * An AuthToken authorizes a User upon their login request. An AuthToken is required to interact with the app.
 * One user may have multiple AuthTokens.
 */
public class AuthToken {
    private String authToken;
    private String userName;

    /**
     * Creates a new AuthToken.
     *
     * @param authToken The authToken to authenticate the user
     * @param userName The username of the user who holds the authToken
     */
    public AuthToken(String authToken, String userName) {
        this.authToken = authToken;
        this.userName = userName;
    }

    /**
     * Generates a unique authToken and makes a new AuthToken
     *
     * @param userName The userName of the person who is getting the
     *                 auto-generated authToken
     */
    public AuthToken(String userName){
        setUserName(userName);
        setToken(UUID.randomUUID().toString());
    }

    public String getUserName() {
        return userName;
    }

    public String getToken() {
        return authToken;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setToken(String authToken) {
        this.authToken = authToken;
    }
}
