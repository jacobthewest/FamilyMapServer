package model;

/**
 * A representation of a User, or of a User's ancestors. A person may be auto generated to be a fake ancestor.
 */
public class Person {
    private String personID;
    private String associatedUsername;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;

    /**
     * Creates a Person
     *
     * @param personID Unique identifier for this person (non-empty string)
     * @param associatedUsername User (Username) to which this person belongs
     * @param firstName Person’s first name (non-empty string)
     * @param lastName Person’s last name (non-empty string)
     * @param gender Person’s gender (string: "f" or "m")
     * @param fatherID Person ID of person’s father (possibly null)
     * @param motherID Person ID of person’s mother (possibly null)
     * @param spouseID Person ID of person’s spouse (possibly null)
     */
    public Person(String personID, String associatedUsername, String firstName, String lastName,
                  String gender, String fatherID, String motherID, String spouseID) {
        setPersonID(personID);
        setAssociatedUsername(associatedUsername);
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setFatherId(fatherID);
        setMotherId(motherID);
        setSpouseId(spouseID);
    }

    /**
     * Creates a person with a personId and an associatedUsername
     *
     * @param personID Unique identifier for this person (non-empty string)
     * @param associatedUsername User (Username) to which this person belongs
     */
    public Person(String personID, String associatedUsername) {
        setPersonID(personID);
        setAssociatedUsername(associatedUsername);
    }

    public String getPersonID() {return personID;}

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getFatherId() {
        return fatherID;
    }

    public String getMotherId() {
        return motherID;
    }

    public String getSpouseId() {
        return spouseID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setFatherId(String fatherId) {
        this.fatherID = fatherId;
    }

    public void setMotherId(String motherId) {
        this.motherID = motherId;
    }

    public void setSpouseId(String spouseId) {
        this.spouseID = spouseId;
    }
}
