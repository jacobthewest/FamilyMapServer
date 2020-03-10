package result;

/**
 * Class to represent the results of a request to the API routes
 * <code>/fill</code> and <code>/fill/{generations}</code>
 */
public class FillResult extends ApiResult {

    private String message;
    private String description;
    private boolean success;
    public static final String MESSAGE = "Successfully added %d persons and %d events to the database.";

    /**
     * Creates an ApiResult of a failed request to the <code>/fill</code> and
     * <code>/fill/{generations}]</code> routes.
     * @param error the error message
     * @param description Description of the error message
     */
    public FillResult(String error, String description) {
        setSuccess(false);
        setMessage(error);
        setDescription(description);
    }

    /**
     * Empty Constructor
     */
    public FillResult() {}

    /**
     * Creates an ApiResult of a successful request to the <code>/fill</code> and
     * <code>/fill/{generations}]</code> routes.
     * @param numPersonsAdded Int number of persons added from Fill Service
     * @param numEventsAdded Int number of persons added from Fill Service
     */
    public FillResult(int numPersonsAdded, int numEventsAdded) {
        setSuccess(true);
        setMessage(String.format(MESSAGE, numPersonsAdded, numEventsAdded));
        setDescription(null);
    }

    public boolean getSuccess() {return this.success;}

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