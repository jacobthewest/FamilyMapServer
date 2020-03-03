package result;

/**
 * Class to represent the results of a request to the API routes
 * <code>/fill</code> and <code>/fill/{generations}</code>
 */
public class FillResult extends ApiResult {
    /**
     * The default message to be sent in the response body upon a successful request
     */
    public static final String MESSAGE = "Successfully added %d persons and %d events to the database.";

    /**
     * Creates an ApiResult of a failed request to the <code>/fill</code> and
     * <code>/fill/{generations}]</code> routes.
     * @param error the error message
     * @param description Description of the error message
     */
    public FillResult(String error, String description) {
        super(false, error, description);
    }

    /**
     * Creates an ApiResult of a successful request to the <code>/fill</code> and
     * <code>/fill/{generations}]</code> routes.
     * @param numPersonsAdded Int number of persons added from Fill Service
     * @param numEventsAdded Int number of persons added from Fill Service
     */
    public FillResult(int numPersonsAdded, int numEventsAdded) {
        super(true, String.format(MESSAGE, numPersonsAdded, numEventsAdded), null);
    }
}