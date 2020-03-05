package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.net.HttpURLConnection;

import result.ClearResult;
import service.ClearService;

import java.io.IOException;

public class ClearHandler implements HttpHandler {

    public final int RESPONSE_LENGTH = 0;
    public final String HTTP_METHOD = "POST";

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        // HTTP Method: POST
        // Auth Token Required: No

        // Verify only HTTP Method
        if(!isValidHttpMethod(httpExchange)) {
            // param 1 = response code to send. param2 = Don't worry, just let it be zero.
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, RESPONSE_LENGTH);
            httpExchange.close();
        }

        ClearService clearService = new ClearService();
        ClearResult clearResult = clearService.clear();

        // Serialize the object
        // Get responsebody from httpexchnage
        // write to responsebody with serialized json
        // close response body.
        // We are good.
    }

    private String getHttpMethod(HttpExchange exchange) {
        String httpMethod = exchange.getRequestMethod().toUpperCase();
        return httpMethod;
    }

    private boolean isValidHttpMethod(HttpExchange exchange) {
        if(getHttpMethod(exchange).equals(HTTP_METHOD)) return true;
        return false;
    }
}


