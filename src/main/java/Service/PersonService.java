package Service;

import Model.Person;
import Request.PersonRequest;
import Result.PersonResult;

/**
 * Contains functions used to find a person by personId. Implements the api route
 * <code>/person/[personID]</code> and <code>/person</code>
 */
public class PersonService {
    /**
     * @param request An instance of PersonRequest with the needed data for the function.
     * @return the single Person object with the specified ID.
     */
    public static Person getPerson(PersonRequest request){
        return null;
    }

    /**
     * Get's all of the family members of the current user.
     *
     * @param request An instance of PersonRequest with the needed data for the function.
     * @return Returns ALL family members of the current user. The current user is
     * determined from the provided auth token.
     */
    public static PersonResult getFamily(PersonRequest request) {
        return null;
    }
}
