package model;

import java.util.UUID;

/**
 * An AuthToken authorizes a User upon their login request. An AuthToken is required to interact with the app.
 * One user may have multiple AuthTokens.
 */
public class AuthToken {
    private String token;
    private String userName;

    /**
     * Creates a new AuthToken.
     *
     * @param token The token to authenticate the user
     * @param userName The username of the user who holds the token
     */
    public AuthToken(String token, String userName) {
        this.token = token;
        this.userName = userName;
    }

    /**
     * Generates a unique token and makes a new AuthToken
     *
     * @param userName The userName of the person who is getting the
     *                 auto-generated token
     */
    public AuthToken(String userName){
        setUserName(userName);
        setToken(UUID.randomUUID().toString());
    }

    public String getUserName() {
        return userName;
    }

    public String getToken() {
        return token;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
