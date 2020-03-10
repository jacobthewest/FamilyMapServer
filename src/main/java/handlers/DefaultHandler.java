package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import javax.swing.text.html.HTMLEditorKit;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class DefaultHandler implements HttpHandler {

    public final int RESPONSE_LENGTH = 0;
    public final String HTTP_METHOD = "GET";
    public final String INDEX_URL = "/index.html";
    public final String NOT_FOUND = "/html/404.html";

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Path filePath = null;
        File file = null;

        // My problems with things
            // Only works for GET requests?
            // How do I tell if the ClearHandler file is a file?

        try {
            if (isIndexPage(httpExchange)) { // Is the URL just "localhost:8080/" or is it just "localhost:8080"
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, RESPONSE_LENGTH);
                filePath = FileSystems.getDefault().getPath("web" + INDEX_URL);
            } else {
                String url = httpExchange.getRequestURI().toString();
                file = new File("web" + url);
                if (file.exists()) {
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, RESPONSE_LENGTH);
                    filePath = FileSystems.getDefault().getPath("web" + url);
                } else {
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, RESPONSE_LENGTH);
                    filePath = FileSystems.getDefault().getPath("web" + NOT_FOUND);
                }
            }

            Files.copy(filePath, httpExchange.getResponseBody());
            httpExchange.getResponseBody().close();
        } catch(Exception e) {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, RESPONSE_LENGTH);
            httpExchange.getResponseBody().close();
        }
    }

    /**
     *
     * @param exchange An HttpExchange object
     * @return If the method in the request matches the method of the handler
     */
    private boolean isValidHttpMethod(HttpExchange exchange) {
        if(getHttpMethod(exchange).equals(HTTP_METHOD)) return true;
        return false;
    }

    /**
     * Gets the http method used (post, get, put, etc.)
     * @param exchange An HttpExchange object
     * @return All-caps String of the http method
     */
    private String getHttpMethod(HttpExchange exchange) {
        String httpMethod = exchange.getRequestMethod().toUpperCase();
        return httpMethod;
    }

    /**
     * Checks for a valid URL
     * @param httpExchange Http Exchange object
     * @return If the url is valid
     */
    private boolean isIndexPage(HttpExchange httpExchange) {
        String url = httpExchange.getRequestURI().toString();
        if(!url.equals("/") && !url.equals(null)) return false;
        if(!httpExchange.getRequestMethod().toUpperCase().equals("GET")) return false;
        return true;
    }
}
