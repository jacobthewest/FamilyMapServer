package result;

/**
 * Class to represent the results of a request to the API route <code>/user/register</code>
 */
public class RegisterResult extends ApiResult {
    private String token;
    private String userName;
    private String personID;

    /**
     * Creates an ApiResult of a failed request to the <code>/user/register</code> route.
     * @param error the error message
     * @param description Description of the error message
     */
    public RegisterResult(String error, String description) {
        super(false, error, description);
    }

    /**
     * Creates an ApiResult of a successful request to the <code>/user/register</code> route.
     * @param token String token from Register Request
     * @param userName String userName from Register Request
     * @param personID String personID from Register Request
     */
    public RegisterResult(String token, String userName, String personID) {
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