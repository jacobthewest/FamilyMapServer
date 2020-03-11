package service;

import dataAccess.*;
import model.Event;
import model.Person;
import model.User;
import result.ApiResult;
import result.LoadResult;
import request.LoadRequest;

/**
 * Implements methods to serve the API route <code>/load</code>
 */
public class LoadService {
    /**
     * Clears all data from the database (just like the /clear API), and then loads the
     * posted user, person, and event data into the database.
     * @param request An instance of LoadRequest containing a User array,
     *                a Person array, and an Events array
     * @return The result of loading.
     */
    public LoadResult load(LoadRequest request) {
        if(request.getUsers() == null) {
            return new LoadResult(ApiResult.INVALID_REQUEST_DATA, "Users array is null");
        }
        if(request.getPersons() == null) {
            return new LoadResult(ApiResult.INVALID_REQUEST_DATA, "Persons array is null");
        }
        if(request.getEvents() == null) {
            return new LoadResult(ApiResult.INVALID_REQUEST_DATA, "Events array is null");
        }

        try {
            // Open the Database and establish a connection
            Database db = new Database();
            db.loadDriver();
            db.openConnection();
            db.initializeTables();
            db.emptyTables();
            db.commitConnection(true);

            // Retrieve data from request
            Event[] events = request.getEvents();
            Person[] persons = request.getPersons();
            User[] users = request.getUsers();

            // Open access to the Database tables
            EventDao eventDao = new EventDao();
            PersonDao personDao = new PersonDao();
            UserDao userDao = new UserDao();

            eventDao.setConnection(db.getConnection());
            personDao.setConnection(db.getConnection());
            userDao.setConnection(db.getConnection());

            // Make sure the clear worked
            if(eventDao.getCountOfAllEvents() != 0) {
                db.closeConnection();
                return new LoadResult(ApiResult.INTERNAL_SERVER_ERROR,"Event table is not empty");}
            if(personDao.getCountOfAllPersons() != 0) {
                db.closeConnection();
                return new LoadResult(ApiResult.INTERNAL_SERVER_ERROR,"Person table is not empty");}
            if(userDao.getCountOfAllUsers() != 0) {
                db.closeConnection();
                return new LoadResult(ApiResult.INTERNAL_SERVER_ERROR,"User table is not empty");}

            // Then insert all of the data
            for(Event singleEvent: events) {eventDao.insertEvent(singleEvent);}
            for(Person singlePerson: persons) {personDao.insertPerson(singlePerson);}
            for(User singleUser: users) {userDao.insertUser(singleUser);}

            // Commit the inserted objects to the database
            db.commitConnection(true);

            // Get the number of added objects
            int numEventsAdded = eventDao.getCountOfAllEvents();
            int numPersonsAdded = personDao.getCountOfAllPersons();
            int numUsersAdded = userDao.getCountOfAllUsers();

            // Close db connection and return
            db.closeConnection();
            return new LoadResult(numUsersAdded, numPersonsAdded, numEventsAdded);
        } catch(DatabaseException e) {
            return new LoadResult(ApiResult.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
