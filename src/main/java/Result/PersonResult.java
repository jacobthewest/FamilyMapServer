package Result;

/**
 * Class to represent the results of a request to the API route <code>/person/[personID]</code>
 */
public class PersonResult extends ApiResult {
    /**
     * Creates an ApiResult of a failed request to the <code>/person/[personID]</code> route.
     * @param error the error message
     */
    public PersonResult(String error) {
        super(false, error);
    }

    /**
     * Creates an ApiResult of a successful request to the <code>/person/[personID]</code> route.
     */
    public PersonResult() {
        super(true, null);
    }
}
