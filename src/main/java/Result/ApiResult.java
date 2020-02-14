package Result;

/**
 * Parent class for all child results to inherit from.
 */
public class ApiResult {
    private String message;
    private boolean success;

    /**
     * Creates a new API result
     *
     * @param success If the ApiRequest was successful in returning data
     * @param message A description of why the ApiRequest failed
     */
    public ApiResult(boolean success, String message) {
        setSuccess(success);
        setMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
