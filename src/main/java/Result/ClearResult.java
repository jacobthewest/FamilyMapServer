package Result;

/**
 * Class to represent the results of a request to the API route <code>/clear</code>
 */
public class ClearResult extends ApiResult {

    /**
     * Response body message to be displayed upon a successful request
     */
    private static final String MESSAGE = "Clear succeeded.";

    /**
     * Creates an ApiResult of a failed request to the <code>/clear</code> route.
     * @param error the error message
     */
    public ClearResult(String error) {
        super(false, error);
    }

    /**
     * Creates an ApiResult of a successful request to the <code>/clear</code> route.
     */
    public ClearResult() {
        super(true, MESSAGE);
    }
}
