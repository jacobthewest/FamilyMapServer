package Model;


/**
 * An major time in a Person's life. Each Person must have three events in their lifetime (birth, marriage, death),
 * although a person's marriage and death may remain empty.
 */
public class Event {
    private String eventID;
    private String associatedUsername;
    private double latitude;
    private double longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;

    /**
     * Creates a new Event
     *
     * @param eventID the unique identifier for this event occurrence
     * @param associatedUsername the userName of the User this event belongs to
     * @param latitude the latitude of the event location
     * @param longitude the longitude of the event location
     * @param country the country of the event location
     * @param city the city of the event location
     * @param eventType the kind of event that this event is
     * @param year the year the event occurred
     */
    public Event(String eventID, String associatedUsername, double latitude, double longitude,
                 String country, String city, String eventType, int year) {

    }

    /**
     * Creates a new event from the provided eventId and associatedUsername
     *
     * @param eventID the kind of event this event is
     * @param associatedUsername the userName fo the User this event belongs to
     */
    public Event(String eventID, String associatedUsername) {

    }

    /**
     * Creates a new, random event for the userName
     *
     * @param associatedUsername the userName fo the User this event belongs to
     */
    public Event(String associatedUsername) {

    }

    public String getAssociatedUsername() {
        return associatedUsername;
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

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
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
