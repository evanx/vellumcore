/*
 Source https://code.google.com/p/vellum by @evanxsummers
 */
package vellum.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.net.HttpURLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author evan.summers
 */
public class RedirectHttpsHandler implements HttpHandler {
    
    Logger logger = LoggerFactory.getLogger(RedirectHttpsHandler.class);
            
    public RedirectHttpsHandler() {
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String hostAddress = httpExchange.getRequestHeaders().getFirst("Host");
        logger.info("host {}", hostAddress);
        String redirectUrl = String.format("https://%s", hostAddress);
        httpExchange.getResponseHeaders().add("Location", redirectUrl);
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_MOVED_PERM, -1);
        httpExchange.close();
    }
}
