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
public class RedirectUrlHttpHandler implements HttpHandler {
    
    Logger logger = LoggerFactory.getLogger(RedirectUrlHttpHandler.class);
    String redirectUrl;
            
    public RedirectUrlHttpHandler(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        logger.info("host {}", redirectUrl);
        httpExchange.getResponseHeaders().add("Location", redirectUrl);
        httpExchange.close();
    }

}
