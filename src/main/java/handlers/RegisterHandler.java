package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.RegisterRequest;
import result.RegisterResult;
import service.RegisterService;
import util.ObjectEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class RegisterHandler implements HttpHandler {

    // URL Path: /user/register
    // HTTP Method: POST
    // Auth Token Required: No

    public final int RESPONSE_LENGTH = 0;
    public final String HTTP_METHOD = "POST";

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        boolean errorFree = true;

        // set the result and service
        RegisterService registerService = new RegisterService();
        RegisterResult registerResult = new RegisterResult();
        RegisterRequest registerRequest = null;

        // validate the HTTP exchange
        if(!isValidHttpMethod(httpExchange)) {
            // Invalid HTTP Method
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, RESPONSE_LENGTH);
            registerResult.setSuccess(false);
            registerResult.setMessage("Http 400, Bad Request");
            registerResult.setDescription("Invalid HTTP method. Method should be " + HTTP_METHOD);
            errorFree = false;
        }

        // Valid Request. Send the HTTP OK
        if(errorFree) {
            InputStream inputStream = httpExchange.getRequestBody();
            ObjectEncoder objectEncoder = new ObjectEncoder();
            registerRequest = (RegisterRequest) objectEncoder.deserialize(inputStream, RegisterRequest.class);
            registerResult = registerService.register(registerRequest);
            if(registerResult.getSuccess()) {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, RESPONSE_LENGTH);
            } else {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, RESPONSE_LENGTH);
            }
        }
        try {
            // Serialize the Result Object
            ObjectEncoder objectEncoder = new ObjectEncoder();
            String json = objectEncoder.serialize(registerResult);

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
            registerResult.setSuccess(false);
            registerResult.setMessage("Internal Server Error");
            registerResult.setDescription("Error: " + e.getMessage());
            ObjectEncoder objectEncoder = new ObjectEncoder();
            String json = objectEncoder.serialize(registerResult);
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
        if(urlParts.length != 2) {
            return false;
        }
        if(!url.contains("/user/register")) return false;
        return true;
    }
}
