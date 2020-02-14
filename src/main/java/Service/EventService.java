package Service;

import Model.Event;
import Result.EventResult;

/**
 * Implements methods needed to serve the API route <code>/event/[eventID]</code>
 * and <code>/event</code>
 */
public class EventService {
    /**
     * Implements the <code>/event/[eventID]</code>
     *
     * @param eventID The identifier of the event we will retrieve from
     *                the database
     * @return The single Event object with the eventId to query the database
     */
    public static Event getEvent(String eventID) {
        return null;
    }

    /**
     * Implements the <code>/event</code> API route.
     *
     * @return Returns ALL events for ALL family members of the current user. The current
     * user is determined from the provided auth token
     */
    public static EventResult getAllEvents() {
        return null;
    }
}
