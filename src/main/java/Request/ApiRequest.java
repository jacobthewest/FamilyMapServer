package Request;

/**
 * Parent class for all child request to inherit from.
 */
public class ApiRequest  {
    private String authToken;

    /**
     * Provides an API request with an AuthToken
     * @param authToken An auth token to approve the API request
     */
    public ApiRequest(String authToken) {
        setAuthToken(authToken);
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
