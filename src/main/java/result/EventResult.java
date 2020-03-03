package result;

import model.Event;

/**
 * Class to represent the results of a request to the API routes
 * <code>/event</code> and <code>/event/[eventID]</code>
 */
public class EventResult extends ApiResult {
    private Event[] data;
    private String associatedUsername;
    private String eventID;
    private String personID;
    private double latitude;
    private double longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;

    /**
     * Creates an ApiResult of a failed request to the <code>/event</code> and
     * <code>/event/[eventID]</code> routes.
     * @param error The error message
     * @param description Description of the error message
     */
    public EventResult(String error, String description) {
        super(false, error, description);
    }

    /**
     * Creates an ApiResult for a successful request to
     * the <code>/event</code> route
     * @param data An array of Event objects
     */
    public EventResult(Event[] data) {
        super(true, null, null);
        setData(data);
    }

    /**
     * Creates an ApiResult for a successful request to
     * the <code>/event/[eventID]</code> route
     * @param event An event object to be displayed in the response body
     */
    public EventResult(Event event) {
        super(true, null, null);
        setEvent(event);
    }

    public Event[] getData() {
        return data;
    }

    /**
     * @return An event object to be used in the response body
     */
    public Event getEvent() {
        Event returnEvent = new Event(getEventID(), getAssociatedUsername());
        returnEvent.setPersonID(getPersonID());
        returnEvent.setLatitude(getLatitude());
        returnEvent.setLongitude(getLongitude());
        returnEvent.setCountry(getCountry());
        returnEvent.setCity(getCity());
        returnEvent.setEventType(getEventType());
        returnEvent.setYear(getYear());

        return returnEvent;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getEventID() {
        return eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    public int getYear() {
        return year;
    }


    public void setData(Event[] data) {
        this.data = data;
    }

    /**
     * Instantiates the member variables to be used for the response body
     * when the eventResult is returned to the EventRequest
     * @param event The event object to set
     */
    public void setEvent(Event event) {
        setAssociatedUsername(event.getAssociatedUsername());
        setPersonID(event.getPersonID());
        setEventID(event.getEventID());
        setLatitude(event.getLatitude());
        setLongitude(event.getLongitude());
        setCountry(event.getCountry());
        setCity(event.getCity());
        setEventType(event.getEventType());
        setYear(event.getYear());
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setYear(int year) {
        this.year = year;
    }
}