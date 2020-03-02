package model;


import java.util.UUID;

/**
 * An major time in a Person's life. Each Person must have three events in their lifetime (birth, marriage, death),
 * although a person's marriage and death may remain empty.
 */
public class Event {
    private String eventID;
    private String associatedUsername;
    private String personID;
    private double latitude;
    private double longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;

    /**
     * Creates a new Event
     *
     * @param eventID Unique identifier for this event (non-empty string)
     * @param associatedUsername User (Username) to which this person belongs
     * @param personID ID of person to which this event belongs
     * @param latitude Latitude of event’s location
     * @param longitude Longitude of event’s location
     * @param country Country in which event occurred
     * @param city City in which event occurred
     * @param eventType Type of event (birth, baptism, christening, marriage, death, etc.)
     * @param year Year in which event occurred
     */
    public Event(String eventID, String associatedUsername, String personID, double latitude, double longitude,
                 String country, String city, String eventType, int year) {
        setEventID(eventID);
        setAssociatedUsername(associatedUsername);
        setPersonID(personID);
        setLatitude(latitude);
        setLongitude(longitude);
        setCountry(country);
        setCity(city);
        setEventType(eventType);
        setYear(year);
    }

    /**
     * Creates a new event from the provided eventId and associatedUsername
     *
     * @param eventID Unique identifier for this event (non-empty string)
     * @param associatedUsername the userName fo the User this event belongs to
     */
    public Event(String eventID, String associatedUsername) {
        setAssociatedUsername(associatedUsername);
        setEventID(eventID);
    }

    /**
     * Creates a new, random event for the userName
     *
     * @param associatedUsername the userName of the User this event belongs to
     */
    public Event(String associatedUsername) {
        setAssociatedUsername(associatedUsername);
        setEventID(UUID.randomUUID().toString());
    }

    public String getEventID() {
        return eventID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
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

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
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
