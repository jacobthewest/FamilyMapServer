package result;

/**
 * Class to represent the results of a request to the API route <code>/clear</code>
 */
public class ClearResult {
    private String message;
    private String description;
    private boolean success;
    private static final String MESSAGE = "Clear succeeded.";

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

    /**
     * Creates an ApiResult of a failed request to the <code>/clear</code> route.
     * @param error The error message
     * @param description Description of the error message
     */
    public ClearResult(String error, String description) {
        setSuccess(false);
        setMessage(error);
        setDescription(description);
    }

    /**
     * Creates an ApiResult of a successful request to the <code>/clear</code> route.
     */
    public ClearResult() {
        setSuccess(true);
        setMessage(MESSAGE);
        setDescription(null);
    }

    public boolean getSuccess() {return this.success;}
}