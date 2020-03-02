package Result;

/**
 * Parent class for all child results to inherit from.
 */
public class ApiResult {
    private String message;
    private boolean success;

    public static final String REQUEST_PROPERTY_MISSING_OR_INVALID = "" +
            "Request property missing or has invalid value";
    public static final String USERNAME_TAKEN = "" +
            "Username already taken by another user";
    public static final String INTERNAL_SERVER_ERROR = "" +
            "Internal server error";
    public static final String INVALID_FILL_PARAM = "" +
            "Invalid username or generations parameter";
    public static final String INVALID_REQUEST_DATA = "" +
            "Invalid request data (missing values, invalid values, etc.)";
    public static final String INVALID_AUTH_TOKEN = "" +
            "Invalid auth token";
    public static final String INVALID_PERSON_ID_PARAM = "" +
            "Invalid personID parameter";
    public static final String REQUESTED_PERSON_NO_RELATION = "" +
            "Requested person does not belong to this user";
    public static final String INVALID_EVENT_ID_PARAM = "" +
            "Invalid eventID parameter";
    public static final String REQUESTED_EVENT_NO_RELATION = "" +
            "Requested event does not belong to this user";

    /**
     * Creates a new API result
     *
     * @param success If the ApiRequest was successful in returning data
     * @param message A description of why the ApiRequest failed
     */
    public ApiResult(boolean success, String message) {
        setSuccessMessage(success);
        setFailMessage(message);
    }

    public String getFailMessage() {
        return message;
    }

    public boolean getSuccessMessage() {
        return success;
    }

    public void setSuccessMessage(boolean success) {
        this.success = success;
    }

    public void setFailMessage(String message) {
        this.message = message;
    }
}
