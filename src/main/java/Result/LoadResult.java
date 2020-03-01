package Result;

/**
 * Class to represent the results of a request to the
 * <code>/load</code> API route
 */
public class LoadResult extends ApiResult {
    private static final String MESSAGE = "Successfully added %d users, %d persons, and %d events to the database.";
    /**
     * Creates an ApiResult of a failed request to the <code>/load</code> route.
     * @param error the error message
     */
    public LoadResult(String error) {
        super(false, error);
    }

    /**
     * Creates an ApiResult of a successful request to the <code>/load</code> route.
     * @param numUsersAdded Int number of users added from load request
     * @param numPersonsAdded Int number of persons added from load request
     * @param numEventsAdded Int number of events added from load request
     */
    public LoadResult(int numUsersAdded, int numPersonsAdded, int numEventsAdded) {
        super(true, String.format(MESSAGE, numUsersAdded, numPersonsAdded, numEventsAdded));
    }
}