package Request;

/**
 * A request to the API route <code>/event</code> and <code>event/[eventID]</code>
 */
public class EventRequest extends ApiRequest {
    private String eventId;

    /**
     * Creates an EventRequest
     *
     * @param eventId the id of the event to get from the database
     * @param authToken the authToken to validate the request.
     */
    public EventRequest(String eventId, String authToken) {
        super(authToken);
        setEventId(eventId);
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }
}
