package result;

/**
 * Class to represent the results of a request to the
 * <code>/load</code> API route
 */
public class LoadResult extends ApiResult {
    private String message;
    private String description;
    private boolean success;
    private static final String MESSAGE = "Successfully added %d users, %d persons, and %d events to the database.";

    /**
     * Creates an ApiResult of a failed request to the <code>/load</code> route.
     * @param error the error message
     * @param description Description of the error message
     */
    public LoadResult(String error, String description) {
        setSuccess(false);
        setMessage(error);
        setDescription(description);
    }

    /**
     * Empty Constructor
     */
    public LoadResult() {}

    /**
     * Creates an ApiResult of a successful request to the <code>/load</code> route.
     * @param numUsersAdded Int number of users added from load request
     * @param numPersonsAdded Int number of persons added from load request
     * @param numEventsAdded Int number of events added from load request
     */
    public LoadResult(int numUsersAdded, int numPersonsAdded, int numEventsAdded) {
        setSuccess(true);
        setMessage(String.format(MESSAGE, numUsersAdded, numPersonsAdded, numEventsAdded));
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