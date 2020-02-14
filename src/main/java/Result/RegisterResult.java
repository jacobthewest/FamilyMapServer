package Result;

/**
 * Class to represent the results of a request to the API route <code>/user/register</code>
 */
public class RegisterResult extends ApiResult {
    private String authToken;
    private String userName;
    private String personID;

    /**
     * Creates an ApiResult of a failed request to the <code>/user/register</code> route.
     * @param error the error message
     */
    public RegisterResult(String error) {
        super(false, error);
    }

    /**
     * Creates an ApiResult of a failed request to the <code>/user/register</code> route.
     */
    public RegisterResult() {
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
