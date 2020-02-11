package Model;

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
     * @param token the token to authenticate the user
     * @param userName the username of the user who holds the token
     */
    public AuthToken(String token, String userName) {
        this.token = token;
        this.userName = userName;
    }

    /**
     * Creates a new AuthToken
     *
     * @param userName the userName of the person who is getting the
     *                 auto-generated token
     */
    public AuthToken(String userName){

    }

    public String getUserName() {
        return userName;
    }

    public String getToken() {
        return token;
    }
}
