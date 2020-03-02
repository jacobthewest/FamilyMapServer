package Service;

import DataAccess.AuthTokenDao;
import DataAccess.Database;
import DataAccess.DatabaseException;
import DataAccess.EventDao;
import Model.AuthToken;
import Model.Event;
import Result.ApiResult;
import Result.ClearResult;
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
     * @param authToken The authToken needed to obtain the event
     * @return The eventResult containing an error message or an Event object
     */
    public static EventResult getEvent(String eventID, AuthToken authToken) {
        if (eventID == null) {
            return new EventResult(ApiResult.INVALID_EVENT_ID_PARAM + ": eventID is null");
        }
        try {
            // Set up DB
            Database db = new Database();
            db.loadDriver();
            db.openConnection();

            if(authToken.getUserName() == null) {
                return new EventResult(ApiResult.INVALID_AUTH_TOKEN + " AuthToken userName is null");
            }

            // Set up Daos and get Event
            EventDao eventDao = new EventDao();
            Event returnedEvent = eventDao.getEventByEventID(eventID);

            // userName in AuthToken MUST match associatedUsername in Event
            if(returnedEvent.getAssociatedUsername() != authToken.getUserName()) {
                return new EventResult(ApiResult.REQUESTED_EVENT_NO_RELATION + "AuthToken userName: " +
                        authToken.getUserName() + " Event associatedUsername: " + returnedEvent.getAssociatedUsername());
            }
            db.closeConnection();
            return new EventResult(returnedEvent);
        } catch(DatabaseException e) {
            return new EventResult(ApiResult.INTERNAL_SERVER_ERROR
                    + ": " + e.getMessage() + " Failure to retrieve Event by EventID.");
        }
    }

    /**
     * Implements the <code>/event</code> API route.
     *
     * @param authToken AuthToken needed for the request
     *
     * @return Returns ALL events for ALL family members of the current user. The current
     * user is determined from the provided auth token
     */
    public static EventResult getAllEvents(AuthToken authToken) {
        try {
            // Set up DB
            Database db = new Database();
            db.loadDriver();
            db.openConnection();

            if(authToken.getUserName() == null) {
                return new EventResult(ApiResult.INVALID_AUTH_TOKEN + " AuthToken userName is null");
            }

            // Set up Daos and get Event
            EventDao eventDao = new EventDao();

            // Create eventDao thing where you get all Events
            Event[] allEvents = eventDao.getAllEvents();

            db.closeConnection();
            return new EventResult(allEvents);
        } catch(DatabaseException e) {
            return new EventResult(ApiResult.INTERNAL_SERVER_ERROR
                    + ": " + e.getMessage() + " Failure to retrieve Event by EventID.");
        }
    }
}
