package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.LoadRequest;
import result.LoadResult;
import service.LoadService;
import util.ObjectEncoder;

import java.io.*;
import java.net.HttpURLConnection;

public class LoadHandler implements HttpHandler {

    // URL Path: /load
    // HTTP Method: POST
    // Auth Token Required: No

    public final int RESPONSE_LENGTH = 0;
    public final String HTTP_METHOD = "POST";

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        boolean errorFree = true;

        // set the result and service
        LoadService loadService = new LoadService();
        LoadResult loadResult = new LoadResult();
        LoadRequest loadRequest = null;

        // validate the HTTP exchange
        if(!isValidHttpMethod(httpExchange)) {
            // Invalid HTTP Method
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, RESPONSE_LENGTH);
            loadResult.setSuccess(false);
            loadResult.setMessage("Http 400, Bad Request");
            loadResult.setDescription("Invalid HTTP method. Method should be " + HTTP_METHOD);
            errorFree = false;
        }

        // Valid Request. Send the HTTP OK
        if(errorFree) {
            InputStream inputStream = httpExchange.getRequestBody();
            ObjectEncoder objectEncoder = new ObjectEncoder();
            loadRequest = (LoadRequest) objectEncoder.deserialize(inputStream, LoadRequest.class);
            loadResult = loadService.load(loadRequest);
            if(loadResult.getSuccess()) {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, RESPONSE_LENGTH);
            } else {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, RESPONSE_LENGTH);
            }
        }
        try {
            // Serialize the Result Object
            ObjectEncoder objectEncoder = new ObjectEncoder();
            String json = objectEncoder.serialize(loadResult);

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
            loadResult.setSuccess(false);
            loadResult.setMessage("Internal Server Error");
            loadResult.setDescription("Error: " + e.getMessage());
            ObjectEncoder objectEncoder = new ObjectEncoder();
            String json = objectEncoder.serialize(loadResult);
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
}
