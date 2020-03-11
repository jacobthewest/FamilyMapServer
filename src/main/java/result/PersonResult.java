package result;

import model.Person;

/**
 * Class to represent the results of a request to the API route <code>/person/[personID]</code>
 */
public class PersonResult extends ApiResult {
    private String personID;
    private String associatedUsername;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;
    private String message;
    private boolean success;
    private Person[] data;

    /**
     * Creates an ApiResult of a failed request to the <code>/person/[personID]</code> and
     * <code>/person</code> routes.
     * @param error the error message
     */
    public PersonResult(String error) {
        setSuccess(false);
        setMessage(error);
    }

    /**
     * Empty Constructor
     */
    public PersonResult() {}

    /**
     * Creates an ApiResult of a successful request to the <code>/person</code> route.
     */
    public PersonResult(Person[] data) {
        setSuccess(true);
        setMessage(null);
        setData(data);
    }

    /**
     * Creates an ApiResult of a successful request to the <code>/person</code> route.
     */
    public PersonResult(Person person) {
        setSuccess(true);
        setMessage(null);
        setMessage(null);
        setAssociatedUsername(person.getAssociatedUsername());
        setPersonID(person.getPersonID());
        setFirstName(person.getFirstName());
        setLastName(person.getLastName());
        setGender(person.getGender());
        setFatherID(person.getFatherId());
        setMotherID(person.getMotherId());
        setSpouseID(person.getSpouseId());
    }

    public Person getPerson() {
        return new Person(personID, associatedUsername, firstName, lastName,
                gender, fatherID, motherID, spouseID);
    }

    public Person[] getData() {
        return data;
    }

    public void setData(Person[] data) {
        this.data = data;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    public boolean getSuccess() {return this.success;}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}