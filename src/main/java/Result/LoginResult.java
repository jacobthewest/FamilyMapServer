package Result;

/**
 * Class to represent the results of a request to the
 * <code>/load</code> API route
 */
public class LoginResult extends ApiResult {
    private String authToken;
    private String userName;
    private String personID;

    /**
     * Creates an ApiResult of a failed request to the <code>/user/login</code> route.
     * @param error The error message for the failed login attempt
     */
    public LoginResult(String error) {
        super(false, error);
    }

    /**
     * Creates an ApiResult of a failed request to the <code>/user/login</code> route.
     */
    public LoginResult() {
        super(true, null);
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUserName() {
        return userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
