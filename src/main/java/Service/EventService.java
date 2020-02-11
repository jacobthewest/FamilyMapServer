package Service;

import Model.Event;
import Request.EventRequest;
import Result.EventResult;

/**
 * Implements methods needed to serve the API route <code>/event/[eventID]</code>
 * and <code>/event</code>
 */
public class EventService {
    /**
     * Implements the <code>/event/[eventID]</code>
     *
     * @param request An instance of EventRequest with the needed data for the function.
     * @return the single Event object with the specified ID.
     */
    public static Event getEvent(EventRequest request) {
        return null;
    }

    /**
     * Implements the <code>/event</code> API route.
     *
     * @param request An instance of EventRequest with the needed data for the function.
     * @return Returns ALL events for ALL family members of the current user. The current
     * user is determined from the provided auth token
     */
    public static EventResult getAllEvents(EventRequest request) {
        return null;
    }
}
