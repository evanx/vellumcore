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
public class RedirectHttpHandler implements HttpHandler {
    
    Logger logger = LoggerFactory.getLogger(RedirectHttpHandler.class);
    String redirectUrl;
    
    public RedirectHttpHandler(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String host = httpExchange.getRequestURI().getHost();
        logger.info("host {} {}", host, redirectUrl);
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        httpExchange.getResponseHeaders().add("Location", redirectUrl);
        httpExchange.close();
    }

}
