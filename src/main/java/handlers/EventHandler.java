package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import result.EventResult;
import service.EventService;
import util.ObjectEncoder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class EventHandler implements HttpHandler {

    // URL Path: /event
    // HTTP Method: GET
    // Auth Token Required: Yes

    // URL Path: /event/[eventID]
    // HTTP Method: GET
    // Auth Token Required: Yes

    public final int RESPONSE_LENGTH = 0;
    public final String HTTP_METHOD = "GET";
    public final String AUTHORIZATION = "Authorization";

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        boolean errorFree = true;

        // set the result and service
        EventService eventService = new EventService();
        EventResult eventResult = new EventResult();

        // validate the HTTP exchange
        if(!isValidHttpMethod(httpExchange)) {
            // Invalid HTTP Method
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, RESPONSE_LENGTH);
            eventResult.setSuccess(false);
            eventResult.setMessage("Http 400, Bad Request");
            eventResult.setDescription("Invalid HTTP method. Method should be " + HTTP_METHOD);
            errorFree = false;
        }

        // Check for invalid AuthToken
        if(httpExchange.getRequestHeaders().containsKey(AUTHORIZATION)) {
            if(!isValidAuthToken(httpExchange)) {
                // Invalid AuthToken
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, RESPONSE_LENGTH);
                eventResult.setSuccess(false);
                eventResult.setMessage("Http 400, Bad Request");
                eventResult.setDescription("Invalid URL. URL should be '/event' or '/event/[eventID]'");
                errorFree = false;
            }
        } else {
            // Invalid HTTP Method
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, RESPONSE_LENGTH);
            eventResult.setSuccess(false);
            eventResult.setMessage("Http 400, Bad Request");
            eventResult.setDescription("No Authorization in request header.");
            errorFree = false;
        }

        // Valid Request. Send the HTTP OK
        if(errorFree) {
            int urlLength = getUrlLength(httpExchange);
            String authToken = httpExchange.getRequestHeaders().getFirst(AUTHORIZATION);
            if(urlLength == 1) {
                eventResult = eventService.getAllEvents(authToken);
            } else if (urlLength == 2) {
                String eventID = getEventID(httpExchange);
                eventResult = eventService.getEvent(eventID, authToken);
            }

            if(eventResult.getSuccess()) {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, RESPONSE_LENGTH);
            } else {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, RESPONSE_LENGTH);
            }
        }
        try {
            // Serialize the Result Object
            ObjectEncoder objectEncoder = new ObjectEncoder();
            String json = objectEncoder.serialize(eventResult);

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
            eventResult.setSuccess(false);
            eventResult.setMessage("Internal Server Error");
            eventResult.setDescription("Error: " + e.getMessage());
            ObjectEncoder objectEncoder = new ObjectEncoder();
            String json = objectEncoder.serialize(eventResult);
            OutputStream outputStream = httpExchange.getResponseBody();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(json);
            outputStreamWriter.flush();
            outputStream.close();
            httpExchange.getResponseBody().close();
        }
    }

    /**
     * Checks for a valid authToken
     * @param httpExchange Http Exchange object
     * @return If the authToken is valid
     */
    private boolean isValidAuthToken(HttpExchange httpExchange) {
        String authToken = httpExchange.getRequestHeaders().getFirst(AUTHORIZATION);
        if(authToken.equals(null)) {
            return false;
        } return true;
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
     *
     * @param exchange An HttpExchange object
     * @return If the method in the request matches the method of the handler
     */
    private boolean isValidHttpMethod(HttpExchange exchange) {
        if(getHttpMethod(exchange).equals(HTTP_METHOD)) return true;
        return false;
    }



    private String getEventID(HttpExchange httpExchange) {
        String url = httpExchange.getRequestURI().toString();
        String[] urlParts = url.split("/");
        return urlParts[2];
    }

    /**
     * @param httpExchange An HttpExchange object
     * @return The length of the URL
     */
    private int getUrlLength(HttpExchange httpExchange) {
        String url = httpExchange.getRequestURI().toString();
        if(url.equals("/event")) {return 1;}
        else if (url.contains("/event/")) {return 2;}
        return 666;
    }
}
