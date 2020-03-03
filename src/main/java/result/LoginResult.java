package result;

/**
 * Class to represent the results of a request to the
 * <code>/load</code> API route
 */
public class LoginResult extends ApiResult {
    private String token;
    private String userName;
    private String personID;

    /**
     * Creates an ApiResult of a failed request to the <code>/user/login</code> route.
     * @param error The error message for the failed login attempt
     * @param description Description of the error message
     */
    public LoginResult(String error, String description) {super(false, error, description);
    }

    /**
     * Creates an ApiResult of a successful request to the <code>/user/login</code> route.
     * @param token String token from LoginRequest
     * @param userName String userName from LoginRequest
     * @param personID String personID from LoginRequest
     */
    public LoginResult(String token, String userName, String personID) {
        super(true, null, null);
        setToken(token);
        setUserName(userName);
        setPersonID(personID);
    }

    public String getToken() {
        return token;
    }

    public String getUserName() {
        return userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}