/*
 * Source https://code.google.com/p/vellum by @evanxsummers
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
public class RedirectPortHttpHandler implements HttpHandler {
    
    Logger logger = LoggerFactory.getLogger(RedirectPortHttpHandler.class);
    int redirectPort;
            
    public RedirectPortHttpHandler(int redirectPort) {
        this.redirectPort = redirectPort;
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String hostAddress = "https://" + httpExchange.getRequestHeaders().getFirst("Host");
        logger.info("host {}", hostAddress);
        String redirectUrl = String.format("https://%s:%d", hostAddress, redirectPort);
        logger.info("redirect {}", hostAddress, redirectUrl);
        httpExchange.getResponseHeaders().add("Location", redirectUrl);
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_MOVED_PERM, -1);
        httpExchange.close();
    }
}
