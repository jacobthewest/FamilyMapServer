package request;

/**
 * A request to the API route <code>/user/login</code>
 */
public class LoginRequest {
    private String userName;
    private String password;

    /**
     * Creates a LoginRequest for the <code>/user/login</code> route
     *
     * @param userName The userName of the User we want to login
     * @param password The password of the User we want to login
     */
    public LoginRequest(String userName, String password) {
        setUserName(userName);
        setPassword(password);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
