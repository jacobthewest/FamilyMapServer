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
     * @param userName String. The userName for the user
     * @param passWord String. A unique passWord for the user.
     */
    public User(String userName, String passWord) {

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
}
