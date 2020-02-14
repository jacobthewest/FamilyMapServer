package Result;

/**
 * Class to represent the results of a request to the API routes
 * <code>/fill</code> and <code>/fill/{generations}</code>
 */
public class FillResult extends ApiResult {
    /**
     * The default message to be sent in the response body upon a successful request
     */
    public static final String MESSAGE = "Successfully added X persons and Y events to the database.";

    /**
     * Creates an ApiResult of a failed request to the <code>/fill</code> and
     * <code>/fill/{generations}]</code> routes.
     * @param error the error message
     */
    public FillResult(String error) {
        super(false, error);
    }

    /**
     * Creates an ApiResult of a successful request to the <code>/fill</code> and
     * <code>/fill/{generations}]</code> routes.
     */
    public FillResult() {
        super(true, MESSAGE);
    }
}
