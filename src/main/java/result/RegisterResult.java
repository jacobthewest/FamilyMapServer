package result;

/**
 * Class to represent the results of a request to the API route <code>/user/register</code>
 */
public class RegisterResult extends ApiResult {
    private String authToken;
    private String userName;
    private String personID;
    private String message;
    private String description;
    private boolean success;

    /**
     * Creates an ApiResult of a failed request to the <code>/user/register</code> route.
     * @param error the error message
     * @param description Description of the error message
     */
    public RegisterResult(String error, String description) {
        setSuccess(false);
        setMessage(error);
        setDescription(description);
    }

    /**
     * Empty Constructor
     */
    public RegisterResult() {}

    /**
     * Creates an ApiResult of a successful request to the <code>/user/register</code> route.
     * @param authToken String authToken from Register Request
     * @param userName String userName from Register Request
     * @param personID String personID from Register Request
     */
    public RegisterResult(String authToken, String userName, String personID) {
        setSuccess(true);
        setMessage(null);
        setDescription(null);
        setToken(authToken);
        setUserName(userName);
        setPersonID(personID);
    }

    public String getToken() {
        return authToken;
    }

    public String getUserName() {
        return userName;
    }

    public String getPersonID() {
        return personID;
    }

    public boolean getSuccess() {return this.success;}

    public void setToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}