package request;

import model.User;
import model.Person;
import model.Event;

/**
 * A request to the API route <code>/load</code>
 */
public class LoadRequest {
    private User[] users;
    private Person[] persons;
    private Event[] events;

    /**
     * Creates a loadRequest for the <code>/load</code> route
     *
     * @param users An array of users to be created
     * @param persons An array of persons for the family history
     *                of the users in the users array
     * @param events An array of events for the family history
     *               of the users in the users array
     */
    public LoadRequest(User[] users, Person[] persons, Event[] events) {
        setUsers(users);
        setPersons(persons);
        setEvents(events);
    }

    public User[] getUsers() {
        return users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
