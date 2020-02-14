package Request;

import Model.User;

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

    /**
     * Creates a LoginRequest for the <code>/user/register</code> route
     *
     * @param user A User object to register with the database
     */
    public RegisterRequest(User user) {
        super();
        setUser(user);
    }

    public void setUser(User user) {
        this.userName = user.getFirstName();
        this.password = user.getPassWord();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.gender = user.getGender();
    }

    public User getUser() {
        User returnUser = new User(this.userName, this.password);
        returnUser.setEmail(this.email);
        returnUser.setFirstName(this.firstName);
        returnUser.setFirstName(this.lastName);
        returnUser.setFirstName(this.gender);

        return returnUser;
    }
}
