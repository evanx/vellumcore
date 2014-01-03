/*
 Source https://code.google.com/p/vellum by @evanxsummers
 * 
 */
package vellum.exception;

/**
 *
 * @author evan.summers
 */
public class ParseException extends Exception {

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
}
