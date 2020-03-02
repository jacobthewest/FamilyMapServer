package dataAccess;

/**
 * Class to throw exceptions we encounter when working with the Database
 */
public class DatabaseException extends Exception{
    /**
     * Constructor for DatabaseException. Creates a new DatabaseException
     * @param message String message for the DatabaseException.
     */
    public DatabaseException(String message)
    {
        super(message);
    }

    /**
     * Empty constructor for DatabaseException. Creates a new DatabaseException
     * without a message.
     */
    DatabaseException()
    {
        super();
    }
}
