package Model;


/**
 * A User must exist to interact with the app. Represents a living person.
 */
public class User {
    private String userName;
    private String passWord;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String personID;

    /**
     * Creates a user
     *
     * @param userName Unique user name (non-empty string)
     * @param passWord User’s password (non-empty string)
     * @param email User’s email address (non-empty string)
     * @param firstName User’s first name (non-empty string)
     * @param lastName User’s last name (non-empty string)
     * @param gender User’s gender (string: "f" or "m")
     * @param personID Unique Person ID assigned to this user’s generated
     *                 Person object - see Family History Information section
     *                 for details (non-empty string)
     */
    public User(String userName, String passWord, String email, String firstName,
                String lastName, String gender, String personID) {
        setUserName(userName);
        setPassWord(passWord);
        setEmail(email);
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setPersonID(personID);
    }

    /**
     * Creates a user
     *
     * @param userName  Unique user name (non-empty string)
     * @param passWord  User’s password (non-empty string)
     */
    public User(String userName, String passWord) {
        setUserName(userName);
        setPassWord(passWord);
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getEmail() {
        return email;
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

    public String getPersonID() {
        return personID;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
