package Result;

/**
 * Class to represent the results of a request to the
 * <code>/load</code> API route
 */
public class LoadResult extends ApiResult {
    /**
     * Creates an ApiResult of a failed request to the <code>/load</code> route.
     * @param error the error message
     */
    public LoadResult(String error) {
        super(false, error);
    }

    /**
     * Creates an ApiResult of a successful request to the <code>/load</code> route.
     */
    public LoadResult() {
        super(true, null);
    }
}
