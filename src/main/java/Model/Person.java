package Model;

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
     * @param personID unique String Id for the person
     * @param associatedUsername unique String userName for the person
     * @param firstName String firstName of the person
     * @param lastName String lastName of the person
     * @param gender String gender of the person. Either 'f' or 'm'
     * @param fatherID String id of the father of the person. May be null.
     * @param motherID String id of the mother of the person. May be null.
     * @param spouseID String id of the spouse of the person. May be null.
     */
    public Person(String personID, String associatedUsername, String firstName, String lastName, String gender,
                  String fatherID, String motherID, String spouseID) {

    }

    /**
     * Creates a person with a personId and an associatedUsername
     *
     * @param personID unique String Id for the person
     * @param associatedUsername unique String userName for the person
     */
    public Person(String personID, String associatedUsername) {

    }

    /**
     * Auto generates a personId and creates a person using the associatedUsername
     * @param associatedUsername unique String userName for the person
     */
    public Person(String associatedUsername) {

    }

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
