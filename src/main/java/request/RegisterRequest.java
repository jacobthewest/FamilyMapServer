package request;

import model.User;

/**
 * A request to the API route <code>/user/register</code>
 */
public class RegisterRequest {
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String personID;

    /**
     * Creates a LoginRequest for the <code>/user/register</code> route
     *
     * @param user A User object to register with the database
     */
    public RegisterRequest(User user) {
        setUser(user);
    }

    public void setUser(User user) {
        this.userName = user.getUserName();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.gender = user.getGender();
        this.personID = user.getPersonID();
    }

    public User getUser() {
        User returnUser = new User(this.userName, this.password);
        returnUser.setEmail(this.email);
        returnUser.setFirstName(this.firstName);
        returnUser.setLastName(this.lastName);
        returnUser.setGender(this.gender);
        returnUser.setPersonID(this.personID);

        return returnUser;
    }
}
