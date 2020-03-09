package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import result.FillResult;
import service.FillService;
import util.ObjectEncoder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class FillHandler implements HttpHandler {

    // URL Path: /fill/[username]
    // HTTP Method: POST
    // Auth Token Required: No

    // URL Path: /fill/[username]/{generations}
    // HTTP Method: POST
    // Auth Token Required: Yes

    public final int RESPONSE_LENGTH = 0;
    public final String HTTP_METHOD = "POST";

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        boolean errorFree = true;

        // set the result and service
        FillService fillService = new FillService();
        FillResult fillResult = new FillResult();

        // validate the HTTP exchange
        if(!isValidHttpMethod(httpExchange)) {
            // Invalid HTTP Method
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, RESPONSE_LENGTH);
            fillResult.setSuccess(false);
            fillResult.setMessage("Http 400, Bad Request");
            fillResult.setDescription("Invalid HTTP method. Method should be " + HTTP_METHOD);
            errorFree = false;
        }

        // Check for invalid URL path
        String url = httpExchange.getRequestURI().toString();
        if(!isValidURL(httpExchange)) {
            // Invalid URL
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, RESPONSE_LENGTH);
            fillResult.setSuccess(false);
            fillResult.setMessage("Http 400, Bad Request");
            fillResult.setDescription("Invalid URL. URL should be '/fill/[username]' or '/fill/[username]/{generations}}'");
            errorFree = false;
        }

        // Valid Request. Send the HTTP OK
        if(errorFree) {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, RESPONSE_LENGTH);
            int urlLength = getUrlLength(httpExchange);
            String username = getUserName(httpExchange);
            if(urlLength == 2) {
                fillResult = fillService.fill(username);
            } else if (urlLength == 3) {
                int generations = getGenerations(httpExchange);
                fillResult = fillService.fill(username, generations);
            }
        }
        try {
            // Serialize the Result Object
            ObjectEncoder objectEncoder = new ObjectEncoder();
            String json = objectEncoder.serialize(fillResult);

            // Get response body from HTTP Exchange
            OutputStream outputStream = httpExchange.getResponseBody();

            // Create an OutputStreamWriter from the outputStream we get from httpExchange.getResponseBody()
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);

            // Write to response body with the serialized json we made using the OutputStreamWriter
            outputStreamWriter.write(json);

            // Flush the outputStreamWriter
            outputStreamWriter.flush();

            // Close the outputStream
            outputStream.close();

            // Close the Http Exchange.getResponseBody
            httpExchange.getResponseBody().close();
        } catch(IOException e) {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, RESPONSE_LENGTH);
            fillResult.setSuccess(false);
            fillResult.setMessage("Internal Server Error");
            fillResult.setDescription("Error: " + e.getMessage());
            ObjectEncoder objectEncoder = new ObjectEncoder();
            String json = objectEncoder.serialize(fillResult);
            OutputStream outputStream = httpExchange.getResponseBody();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(json);
            outputStreamWriter.flush();
            outputStream.close();
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
    private boolean isValidURL(HttpExchange httpExchange) {
        String url = httpExchange.getRequestURI().toString();
        String[] urlParts = url.split("/");
        if(urlParts.length < 1 || urlParts.length < 3) {
            return false;
        }
        if(!url.contains("/fill/")) return false;
        return true;
    }

    /**
     * @param httpExchange An HttpExchange object
     * @return The length of the URL
     */
    private int getUrlLength(HttpExchange httpExchange) {
        String url = httpExchange.getRequestURI().toString();
        String[] urlParts = url.split("/");
        return urlParts.length;
    }

    /**
     * Gets the username for the fill
     * @param httpExchange An HttpExchange object
     * @return The username for the fill
     */
    private String getUserName(HttpExchange httpExchange) {
        String url = httpExchange.getRequestURI().toString();
        String[] urlParts = url.split("/");
        return urlParts[1];
    }

    /**
     * Gets the number of generations for the fill
     * @param httpExchange An HttpExchange object
     * @return The number of generations for the fill
     */
    private int getGenerations(HttpExchange httpExchange) {
        String url = httpExchange.getRequestURI().toString();
        String[] urlParts = url.split("/");
        return Integer.parseInt(urlParts[2]);
    }
}
