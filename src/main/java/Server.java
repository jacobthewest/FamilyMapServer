import com.sun.net.httpserver.HttpServer;
import dataAccess.Database;
import dataAccess.DatabaseException;
import handlers.*;

import java.net.InetSocketAddress;

public class Server {
    private static final int NO_SUCCESS = 1;
    private static int PORT = 8080;
    private int BACKLOG = 10;
    private HttpServer server;
    private Database db;

    public static void main(String[] args) {
//        if (args.length != 1) System.exit(NO_SUCCESS);
//        PORT = Integer.parseInt(args[0]);

        try {
            (new Server()).run(); // Need to wrap in parens to avoid
                                  // weird problems with HttpServer being abstract
        } catch(Exception e) {
            System.err.println("Problem running server in main() of Server.java: " + e.getMessage());
            System.exit(NO_SUCCESS);
        }

    }

    private void run() {
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), BACKLOG);
            initializeDatabase();
            createHandlerContexts();
            server.setExecutor(null);
            server.start();

        } catch(Exception e) {
            System.err.println("Problem running server in run() of Server.java: " + e.getMessage());
            System.exit(NO_SUCCESS);
        }
    }

    /**
     * Opens, loads, starts, initializes, and commits the database
     */
    private void initializeDatabase() {
        try {
            db = new Database();
            db.loadDriver();
            db.openConnection();
            db.initializeTables();
            db.commitConnection(true);
        } catch(DatabaseException e) {
            System.err.println("Error initializing the database: " + e.getMessage());
            System.exit(NO_SUCCESS);
        }
    }

    /**
     * Creates URL contexts for the Handler classes
     */
    private void createHandlerContexts() {
        server.createContext("/", new DefaultHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/user/register", new RegisterHandler());
    }
}
