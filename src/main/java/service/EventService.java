package service;

import dataAccess.AuthTokenDao;
import dataAccess.Database;
import dataAccess.DatabaseException;
import dataAccess.EventDao;
import model.AuthToken;
import model.Event;
import result.ApiResult;
import result.EventResult;

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
    public EventResult getEvent(String eventID, String authToken) {
        if (eventID == null) {
            return new EventResult(ApiResult.INVALID_EVENT_ID_PARAM,"eventID is null");
        }
        if(authToken == null) {
            return new EventResult(ApiResult.INVALID_AUTH_TOKEN, "Token is null");
        }
        try {
            // Set up DB
            Database db = new Database();
            db.loadDriver();
            db.openConnection();
            db.initializeTables();
            db.commitConnection(true);

            // Set up Daos and get Event
            EventDao eventDao = new EventDao();
            AuthTokenDao authTokenDao = new AuthTokenDao();
            eventDao.setConnection(db.getConnection());
            authTokenDao.setConnection(db.getConnection());

            Event returnedEvent = eventDao.getEventByEventID(eventID);
            if(returnedEvent == null) {
                db.closeConnection();
                return new EventResult(ApiResult.INVALID_EVENT_ID_PARAM, "No event was returned with the eventID: " + eventID);
            }

            AuthToken returnedAuthToken = authTokenDao.getAuthTokenByToken(authToken);
            if(returnedAuthToken == null) {
                db.closeConnection();
                return new EventResult(ApiResult.INVALID_AUTH_TOKEN, "AuthToken is null and not found in database.");
            }
            if(returnedAuthToken.getUserName() == null) {
                db.closeConnection();
                return new EventResult(ApiResult.INVALID_AUTH_TOKEN, "AuthToken userName is null");
            }

            // userName in AuthToken MUST match associatedUsername in Event
            if(!returnedEvent.getAssociatedUsername().equals(returnedAuthToken.getUserName())) {
                db.closeConnection();
                return new EventResult(ApiResult.REQUESTED_EVENT_NO_RELATION, "AuthToken userName: " +
                        returnedAuthToken.getUserName() + " Event associatedUsername: " + returnedEvent.getAssociatedUsername());
            }

            // Close db connection and return
            db.closeConnection();
            return new EventResult(returnedEvent);
        } catch(DatabaseException e) {
            return new EventResult(ApiResult.INTERNAL_SERVER_ERROR, e.getMessage() + ". Failure to retrieve Event by EventID.");
        }
    }

    /**
     * Implements the <code>/event</code> API route.
     *
     * @param authToken AuthToken needed for the request
     *
     * @return Returns ALL events for ALL family members of the current user. The current
     * user is determined from the provided auth authToken
     */
    public EventResult getAllEvents(String authToken) {
        try {
            // Set up DB
            Database db = new Database();
            db.loadDriver();
            db.openConnection();
            db.initializeTables();
            db.commitConnection(true);

            if(authToken == null) {
                db.closeConnection();
                return new EventResult(ApiResult.INVALID_AUTH_TOKEN, "Token is null");
            }

            // Set up Daos and get Event
            EventDao eventDao = new EventDao();
            AuthTokenDao authTokenDao = new AuthTokenDao();

            eventDao.setConnection(db.getConnection());
            authTokenDao.setConnection(db.getConnection());

            AuthToken returnedAuthToken = authTokenDao.getAuthTokenByToken(authToken);
            if(returnedAuthToken == null) {
                db.closeConnection();
                return new EventResult(ApiResult.INVALID_AUTH_TOKEN, "AuthToken is null and not found in database.");
            }
            if(returnedAuthToken.getUserName() == null) {
                db.closeConnection();
                return new EventResult(ApiResult.INVALID_AUTH_TOKEN, "AuthToken userName is null");
            }

            // Make sure that the authToken belongs to the provided userName
            AuthToken authTokenForCheck = authTokenDao.getAuthTokenByUserName(returnedAuthToken.getUserName());

            if(authTokenForCheck == null) {
                db.closeConnection();
                return new EventResult(ApiResult.INVALID_AUTH_TOKEN,
                        "authToken of provided authToken does not exist in database.");
            }

            String tokenFromResponse = authTokenForCheck.getToken();
            String tokenFromParameter = returnedAuthToken.getToken();

            if(!tokenFromParameter.equals(tokenFromResponse)) {
                db.closeConnection();
                return new EventResult(ApiResult.INVALID_AUTH_TOKEN,
                        "authToken of provided authToken does not match authToken with provided userName");
            }

            // Create eventDao thing where you get all Events
            Event[] allEvents = eventDao.getAllEvents(returnedAuthToken.getUserName());

            // Close db connection and return
            db.closeConnection();
            return new EventResult(allEvents);
        } catch(DatabaseException e) {
            return new EventResult(ApiResult.INTERNAL_SERVER_ERROR,
                    e.getMessage() + " Failure to retrieve Event by EventID.");
        }
    }
}
