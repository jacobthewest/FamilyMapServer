package result;

/**
 * Parent class for all error messages.
 */
public class ApiResult {
    public static final String REQUEST_PROPERTY_MISSING_OR_INVALID = "" +
            "Error: Request property missing or has invalid value";
    public static final String USERNAME_TAKEN = "" +
            "Error: Username already taken by another user";
    public static final String INTERNAL_SERVER_ERROR = "" +
            "Error: Internal server error";
    public static final String INVALID_FILL_PARAM = "" +
            "Error: Invalid username or generations parameter";
    public static final String INVALID_REQUEST_DATA = "" +
            "Error: Invalid request data (missing values, invalid values, etc.)";
    public static final String INVALID_AUTH_TOKEN = "" +
            "Error: Invalid auth authToken";
    public static final String INVALID_PERSON_ID_PARAM = "" +
            "Error: Invalid personID parameter";
    public static final String REQUESTED_PERSON_NO_RELATION = "" +
            "Error: Requested person does not belong to this user";
    public static final String INVALID_EVENT_ID_PARAM = "" +
            "Error: Invalid eventID parameter";
    public static final String REQUESTED_EVENT_NO_RELATION = "" +
            "Error: Requested event does not belong to this user";
}
