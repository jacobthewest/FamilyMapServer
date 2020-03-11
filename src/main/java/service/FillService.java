package service;

import dataAccess.*;
import model.Event;
import model.Person;
import model.User;
import result.ApiResult;
import result.FillResult;

import java.util.*;

/**
 * Implements methods to serve the API route <code>/fill/[username]/{generations}</code>
 */
public class FillService {
    private final int DEFAULT_GENERATIONS = 4;
    private List<String> MALE_NAMES = new ArrayList<String>();
    private List<String> FEMALE_NAMES = new ArrayList<String>();
    private List<String> LAST_NAMES = new ArrayList<String>();
    private List<String> CITIES = new ArrayList<String>();
    private List<String> COUNTRIES = new ArrayList<String>();
    private List<Double> LATITUDES = new ArrayList<Double>();
    private List<Double> LONGITUDES = new ArrayList<Double>();
    private List<Event> generatedEvents = new ArrayList<Event>();
    private List<Person> generatedPersons = new ArrayList<Person>();
    private final int MIN_PARENT_CHILD_AGE_DIFFERENCE = 13;
    private final int MAX_BIRTHING_AGE = 50;
    private final int MAX_DEATH_AGE = 120;
    private final int ADULT_AGE = 18;
    private static final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    private Random random = new Random();
    private PersonDao personDao = null;
    private UserDao userDao = null;
    private EventDao eventDao = null;

    /**
     * Populates the server's database with generated data for the specified user name.
     * The required "username" parameter must be a user already registered with the server. If there is
     * any data in the database already associated with the given user name, it is deleted. The
     * optional "generations" parameter lets the caller specify the number of generations of ancestors
     * to be generated, and must be a non-negative integer (the default is 4, which results in 31 new
     * persons each with associated events).
     * @param userName The userName of User to fill.
     * @param generations The number of generations to generate.
     * @return The FillResult from attempting to fill the database.
     */
    public FillResult fill(String userName, int generations) {
        setUpDataForFill();
        try {
            Database db = new Database();
            setUpDatabase(db);
            createDaosAndSetConnections(db);

            FillResult paramCheck = errorCheckParameters(userName, generations, db);
            if(paramCheck != null) return paramCheck;

            // The required "username" parameter must be a user already registered with the server
            Person preDeletePersonCopy = personDao.getPersonByAssociatedUsername(userName);
            User u = userDao.getUserByUserName(userName);

            if(preDeletePersonCopy == null && u ==  null) {
                db.closeConnection();
                return new FillResult(ApiResult.INVALID_FILL_PARAM, "userName does not exist within the server");
            }

            // In case the user exists in the database as a user and not yet as a person
            if(preDeletePersonCopy == null) {
                preDeletePersonCopy = new Person(u.getPersonID(), u.getUserName(), u.getFirstName(), u.getLastName(), u.getGender(),
                        null, null, null);
            }

            // If there is any data in the database already associated with the given user name, it is deleted.
            deletePreExistingUserData(userName, preDeletePersonCopy, db);

            // Create the root person
            Person rootPerson = createRootPerson(preDeletePersonCopy);

            setRootPersonAndAddToGeneratedPersons(rootPerson, userName, preDeletePersonCopy);

            // Make the user between 18 and 120 years old
            int birthYear = CURRENT_YEAR - random.nextInt((MAX_DEATH_AGE  + 1) - ADULT_AGE);

            generateFillPersonBirthAndAddToGeneratedEvents(birthYear, rootPerson);

            // Let the recursion begin. After this function this.generatedEvents and this.generatedPersons
            // will be completely full.
            generateAncestry(rootPerson, birthYear, generations);

            // Insert created Events and Persons into database
            insertEventsAndPersonsIntoDatabase();

            // Close db connection, commit, and return a successful FillResult
            return finishFill(db);
        } catch(DatabaseException e) {
            return new FillResult(ApiResult.INTERNAL_SERVER_ERROR, "Error filling with userName: '" +
                    userName + "' and '" + generations + "' generations. Error: " + e.getMessage());
        }
    }

    /**
     * Populates the server's database with generated data for the specified user name.
     * The required "username" parameter must be a user already registered with the server. If there is
     * any data in the database already associated with the given user name, it is deleted. The
     * optional "generations" parameter lets the caller specify the number of generations of ancestors
     * to be generated, and must be a non-negative integer (the default is 4, which results in 31 new
     * persons each with associated events).
     * @param userName the userName of User to fill
     * @return The FillResult from attempting to fill the database.
     */
    public FillResult fill(String userName) {
        final int DEFAULT_GENERATIONS = 4;
        return fill(userName, DEFAULT_GENERATIONS);
    }

    /**
     * Initializes the MALE_NAMES array list.
     */
    private void setMaleNames() {
        String[] firstNamesDudes = new String[]{"James", "John", "Michael", "Robert",
                "David", "Richard", "Joseph", "Charles", "Paul", "Daniel", "Mark", "George", "Steven", "Edward",
                "Brian", "Donald", "Ronald", "Jose", "Gary", "Timothy", "Joshua", "Raymond", "Andrew", "Jacob", "Henry", "Carl",
                "Ryan", "Roger", "Harold", "Samuel", "Benjamin", "Adam", "Harry", "Jeremy", "Aaron"};
        for(String singleName:firstNamesDudes) {
            this.MALE_NAMES.add(singleName);
        }
    }

    /**
     * Initializes the FEMALE_NAMES array list.
     */
    private void setFemaleNames() {
        String[] firstNamesChicks = new String[]{"Mary", "Patricia", "Linda", "Barbara", "Jennifer",
                "Elizabeth", "Maria", "Holly", "Shannon", "Trudy", "Nancy", "Lisa", "Susan", "Karen", "Natalie",
                "Katherine", "Ruth", "Donna", "Carol", "Virginia", "Rebecca", "Sarah", "Rachel", "Allison", "Katelyn",
                "Karla", "Aubrey", "Samantha", "Alexandra", "Hannah", "Melissa", "Amy", "Martha", "Debra", "Janet", "Julie"};
        for(String singleName:firstNamesChicks) {
            this.FEMALE_NAMES.add(singleName);
        }
    }

    /**
     * Initializes the LAST_NAMES array list.
     */
    private void setLastNames() {
        String[] lastNames = new String[]{"Smith", "Johnson", "Williams", "Jones", "Brown",
                "Davis", "Miller", "Wilson", "Moore", "Taylor", "Anderson", "Jackson", "White", "Harris", "Martin", "Pugmire",
                "Yorgason", "Briggs", "Vanderwerken", "VanCott", "Davidson", "West", "Jacklyn", "Hernandez", "Scott", "Hill",
                "Baker", "Wright", "King", "Walker", "Allen", "Lee", "Robinson", "Clark", "Martinez", "Garcia", "Philips", "Turner",
                "Parker", "Evans", "Collins", "Edwards", "Carter", "Mitchell", "James", "Roberts", "Cook", "Reed", "Stewards",
                "Winfrey", "Murphey", "Bell", "Morgan", "Cook"};
        for(String singleName: lastNames) {
            this.LAST_NAMES.add(singleName);
        }
    }

    /**
     * Initializes the CITIES array list. Index positions match exactly with the other
     * location array lists to make event creation easier.
     */
    private void setCities() {
        String[] cities = new String[]{"Boise", "Salt Lake City", "Ogden", "Orem", "Provo", "Saint George",
                "Twin Falls", "Rigby", "Jerome", "Fillmore", "Beaver", "Logan", "Brigham City", "Burley", "Rupert", "Rexburg", "Santaquin",
                "Tooele", "Sandy", "Riverton", "Nephi", "Delta", "Emery", "Price", "Spanish Fork", "West Valley City", "Moab"};
        for(String singleCity: cities) {
            this.CITIES.add(singleCity);
        }
    }

    /**
     * Initializes the COUNTRIES array list. Currently we only care about USA cities so her we are.
     */
    private void setCountries() {
        String[] countries = new String[]{"USA"};
        for(String singleCountry: countries) {
            this.COUNTRIES.add(singleCountry);
        }
    }

    /**
     * Initializes the LATITUDES array list. Index positions match exactly with the other
     * location array lists to make event creation easier.
     */
    private void setLatitudes() {
        double[] latitudes = new double[]{43.615791, 40.760780, 41.222759, 40.297119, 40.233845,
                37.108284, 42.563181, 43.672219, 42.724137, 38.96675, 38.279582, 41.735211, 41.510372, 42.538116,
                42.616386, 43.826071, 39.97558, 40.534265, 40.569705, 40.518701, 39.71002, 39.352381, 38.923149,
                39.599506, 40.115007, 40.696682, 38.571738};
        for(double singleLat: latitudes) {
            this.LATITUDES.add(singleLat);
        }
    }

    /**
     * Initializes the LONGITUDES array list. Index positions match exactly with the other
     * location array lists to make event creation easier.
     */
    private void setLongitudes() {
        double[] longitudes = new double[]{-116.201576, -111.891045, -111.970421, -111.695007, -111.658531,
                -113.583277, -114.460278, -111.914829, -114.518435, -112.323686, -112.641291, -111.834857, -112.015716, -113.793261,
                -113.67082, -111.789532,  -111.785249, -112.298452, -111.897282, -111.931665, -111.828546, -112.57362, -111.250477,
                -110.811215, -111.654713, -111.959172, -109.550796};
        for(double singleLong: longitudes) {
            this.LONGITUDES.add(singleLong);
        }
    }

    /**
     * Initializes all of the class member variables that we need.
     * I keep this out of the main functions to keep things more clear.
     */
    private void setUpDataForFill() {
        setMaleNames();
        setFemaleNames();
        setLastNames();
        setCities();
        setCountries();
        setLatitudes();
        setLongitudes();
    }

    /**
     * Creates an object that represents a parent
     * @param personID Identifier of the person to create
     * @param gender Gender of the person
     * @param lastName lastName of the person. Is NULL if male
     * @return A Person object representing a child's parent
     */
    private Person createPersonObject(String personID, String gender, String firstName, String lastName, boolean isRootPerson) {
        // Randomly make their username, first name, and mom's last name
        String firstNameCopy = firstName;
        String lastNameCopy = lastName;

        Person person = new Person(personID, UUID.randomUUID().toString(), firstNameCopy, lastNameCopy,
                gender, UUID.randomUUID().toString(), UUID.randomUUID().toString(), null);
        return person;
    }

    /**
     * Creates an object that represents a parent
     * @param personID Identifier of the person to create
     * @param gender Gender of the person
     * @param lastName lastName of the person. Is NULL if male
     * @return A Person object representing a child's parent
     */
    private Person createPersonObject(String personID, String gender, String lastName, boolean isRootPerson) {
        // Randomly make their username, first name, and mom's last name
        String firstName;
        String lastNameCopy = lastName;
        if(gender.equals("f")) {
            firstName = getRandomFemaleName();
            lastNameCopy = getRandomLastName();
        } else {
            if(isRootPerson) {
                lastNameCopy = getRandomLastName();
            }
            firstName = getRandomMaleName();
        }
        Person person = new Person(personID, UUID.randomUUID().toString(), firstName, lastNameCopy,
                gender, UUID.randomUUID().toString(), UUID.randomUUID().toString(), null);
        return person;
    }

    /**
     * Recursively generates person and event data for the requested number of generations
     * and adds the generated objects to the generatedPersons and generatedEvent array lists
     * @param child Object representing a child
     * @param childBirthYear Year the child was born
     * @param generations The number of generations to generate
     */
    private void generateAncestry(Person child, int childBirthYear, int generations) {
        if(generations == 0) {
            return;
        }

        // Generate Mom and Dad
        // Get their personID's from child
        Person mom = createPersonObject(child.getMotherId(), "f", null, false);
        Person dad = createPersonObject(child.getFatherId(), "m", child.getLastName(), false);

        // Set the parent's associatedUserName
        mom.setAssociatedUsername(child.getAssociatedUsername());
        dad.setAssociatedUsername(child.getAssociatedUsername());

        if(generations == 1) {
            mom.setMotherId(null);
            mom.setFatherId(null);
            dad.setMotherId(null);
            dad.setFatherId(null);
        }

        // dad and mom get spouse id from each other
        mom.setSpouseId(dad.getPersonID());
        dad.setSpouseId(mom.getPersonID());

        // Add dad and mom to the generatedPersons data
        this.generatedPersons.add(mom);
        this.generatedPersons.add(dad);

        // Marry mom and dad
        // Create identical marriage events just with their own personID's and associatedUsernames
        Event momMarriage = generateWifeMarriageEvent(mom, dad, childBirthYear);
        Event dadMarriage = generateHusbandMarriage(momMarriage, dad);

        // Add the two events to the generatedEventsData
        this.generatedEvents.add(momMarriage);
        this.generatedEvents.add(dadMarriage);

        // Create mom and dads birthdays, deathdays, and baptisms
        // Get their birth years for the recursive call
        int dadBirthYear = generateAndAddParentLifeEvents(dad, dadMarriage.getYear(), childBirthYear);
        int momBirthYear = generateAndAddParentLifeEvents(mom, momMarriage.getYear(), childBirthYear);

        int parentsMarriageYear = dadMarriage.getYear();

        // generateAncestry on dad and mom.
        int dadsGenerationsNum = generations - 1;
        int momsGenerationsNum = generations - 1;
        generateAncestry(dad, dadBirthYear, dadsGenerationsNum);
        generateAncestry(mom, momBirthYear, momsGenerationsNum);
    }

    /**
     * Creates an event object for any given eventType
     * @param eventType The type of event
     * @param eventYear When the event happened.
     * @param person Person for whom the event belongs
     * @return An Event object
     */
    private Event createEvent(String eventType, int eventYear, Person person) {
        int randomIndexForLocation = random.nextInt(this.CITIES.size());
        String city = this.CITIES.get(randomIndexForLocation);
        double latitude = this.LATITUDES.get(randomIndexForLocation);
        double longitude = this.LONGITUDES.get(randomIndexForLocation);
        String country = this.COUNTRIES.get(0);

        return new Event(UUID.randomUUID().toString(), person.getAssociatedUsername(), person.getPersonID(),
                latitude, longitude, country, city, eventType, eventYear);
    }

    private Event generateHusbandMarriage(Event wifeMarriage, Person husband) {
        return new Event(wifeMarriage.getEventID(), husband.getAssociatedUsername(),
                husband.getPersonID(), wifeMarriage.getLatitude(), wifeMarriage.getLongitude(),
                wifeMarriage.getCountry(), wifeMarriage.getCity(), wifeMarriage.getEventType(),
                wifeMarriage.getYear());
    }

    /**
     * Create a parents birthday, baptism, and maybe death event
     * @param parent Person object representing the parent
     * @param marriageYear The year the parent was married
     * @return The parent's birth year
     */
    private int generateAndAddParentLifeEvents(Person parent, int marriageYear, int childBirthYear) {
        int birthYear = getParentBirthYear(marriageYear, childBirthYear);



        int baptismYear = birthYear + 8; // They ALL are members of the church haha

        Event birth = createEvent("Birth", birthYear, parent);
        Event baptism = createEvent("Baptism", baptismYear, parent);

        this.generatedEvents.add(birth);
        this.generatedEvents.add(baptism);

        int parentAgeAtMarriage = marriageYear - birthYear;

        // For the test cases the parents are always dead.
        // MAX_DEATH_AGE - currentParentAge. I do this so the parent dies at 120 or under.
        int deathYear = marriageYear + random.nextInt(MAX_DEATH_AGE - parentAgeAtMarriage);
        Event death = createEvent("death", deathYear, parent);
        this.generatedEvents.add(death);

        return birthYear;
    }

    /**
     * Generates a marriage event for the wife only. Husband marriage event can be coppied
     * and then its event associatedUsername and personID can be manually set.
     * @param wife Person object representing the wife
     * @param husband Person object representing the wife
     * @param childBirthYear Year the couples child was born
     * @return An Event object representing the marriage
     */
    private Event generateWifeMarriageEvent(Person wife, Person husband, int childBirthYear) {
        int randomIndexForLocation = random.nextInt(this.CITIES.size());
        String city = this.CITIES.get(randomIndexForLocation);
        double latitude = this.LATITUDES.get(randomIndexForLocation);
        double longitude = this.LONGITUDES.get(randomIndexForLocation);
        String country = this.COUNTRIES.get(0);
        int marriageYear = childBirthYear - random.nextInt(7); // We will say that all children are born
                                                                      // within 6 years of marriage

        Event wifeMarriage = new Event(UUID.randomUUID().toString(), wife.getAssociatedUsername(), wife.getPersonID(),
                latitude, longitude, country, city, "Marriage", marriageYear);
        return wifeMarriage;
    }

    /**
     * @return A random female name
     */
    private String getRandomFemaleName() {
        int randomIndex = random.nextInt(this.FEMALE_NAMES.size());
        return this.FEMALE_NAMES.get(randomIndex);
    }

    /**
     @return A random male name
     */
    private String getRandomMaleName() {
        int randomIndex = random.nextInt(this.MALE_NAMES.size());
        return this.MALE_NAMES.get(randomIndex);
    }

    /**
     * @return A random last name
     */
    private String getRandomLastName() {
        int randomIndex = random.nextInt(this.LAST_NAMES.size());
        return this.LAST_NAMES.get(randomIndex);
    }

    private void createDaosAndSetConnections(Database db) {
        eventDao = new EventDao();
        personDao = new PersonDao();
        userDao = new UserDao();

        eventDao.setConnection(db.getConnection());
        personDao.setConnection(db.getConnection());
        userDao.setConnection(db.getConnection());
    }

    private void insertEventsAndPersonsIntoDatabase() {
        try {
            for(Event singleEvent: this.generatedEvents) {
                eventDao.insertEvent(singleEvent);
            }

            for(Person singlePerson: this.generatedPersons) {
                personDao.insertPerson(singlePerson);
            }
        } catch(DatabaseException ex) {
            System.out.println("Problem in insertEventsAndPersonsIntoDatabase() function in FillService class: " + ex.getMessage());
        }
    }

    private void setUpDatabase(Database db) {
        try {
            db.loadDriver();
            db.openConnection();
            db.initializeTables();
            db.commitConnection(true);
        } catch(DatabaseException ex) {
            System.out.println("Problem in setUpDatabase() function in FillService class: " + ex.getMessage());
        }
    }

    private void setRootPersonAndAddToGeneratedPersons(Person rootPerson, String userName, Person preDeletePersonCopy) {
        rootPerson.setAssociatedUsername(userName);
        rootPerson.setPersonID(preDeletePersonCopy.getPersonID());
        this.generatedPersons.add(rootPerson);
    }

    private void generateFillPersonBirthAndAddToGeneratedEvents(int birthYear, Person rootPerson) {
        Event childBirth = createEvent("Birth", birthYear, rootPerson); // Makes sure the preDeletePerson is born
        this.generatedEvents.add(childBirth);
    }

    private Person createRootPerson(Person preDeletePersonCopy) {

//        String gender;
//
//        // Randomly get person's gender
//        int result = random.nextInt(2);
//        if(result == 0){
//            gender = "m";
//        } else{
//            gender = "f";
//        }

        return createPersonObject(preDeletePersonCopy.getPersonID(),
                preDeletePersonCopy.getGender(), preDeletePersonCopy.getFirstName(), preDeletePersonCopy.getLastName(), true);
    }

    private FillResult errorCheckParameters(String userName, int generations, Database db) {
        if(userName == null) {
            try {
                db.closeConnection();
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
            return new FillResult(ApiResult.INVALID_FILL_PARAM, "userName is null");
        }
        if(generations < 0) {
            try {
                db.closeConnection();
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
            return new FillResult(ApiResult.INVALID_FILL_PARAM, "Must request non-negative number of " +
                    "generations. Generations requested: " + generations);
        }
        return null;
    }

    private void deletePreExistingUserData(String userName, Person preDeletePersonCopy, Database db) {
        try {
            eventDao.deleteAllEventsForAssociatedUsername(userName);
            personDao.deletePerson(preDeletePersonCopy.getPersonID());
            db.commitConnection(true);

        } catch(DatabaseException ex) {
            System.out.println("Problem in deletePreExistingUserData() function in FillService class: " + ex.getMessage());
        }
    }

    private FillResult finishFill(Database db) {
        try {
            db.commitConnection(true);
            db.closeConnection();
            return new FillResult(this.generatedPersons.size(), this.generatedEvents.size());
        } catch(DatabaseException ex) {
            System.out.println("Problem in finishFill() function in FillService class: " + ex.getMessage());
        }
        return null;
    }

    private int getParentBirthYear(int marriageYear, int childBirthYear) {
        boolean getNewParentBirthYear = true;
        int parentBirthYear = 0;

        // Make parent 13 years older than child
        while(getNewParentBirthYear) {

            // We are assuming people will get married between 18 and 24 years of age
            // So we get their birthYear by saying they are 18 + (0 to 6) years old when they were married.
            parentBirthYear = marriageYear - (ADULT_AGE + random.nextInt(7)); // Get's a random ma

            // Check if parent-child age difference is ok
            int parentChildAgeDifference = parentBirthYear - childBirthYear;
            if(parentChildAgeDifference >= MIN_PARENT_CHILD_AGE_DIFFERENCE) getNewParentBirthYear = false;

            // Check if maximum birthing age for parent is ok
            int parentAgeAtChildBirth = (childBirthYear - parentBirthYear);
            if(parentAgeAtChildBirth <= MAX_BIRTHING_AGE) getNewParentBirthYear = false;
        }
        return parentBirthYear;
    }
}