package vellum.crypto;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vellum.crypto.hmac.Hmacs;

public class HmacTest {
    static Logger logger = LoggerFactory.getLogger(HmacTest.class); 
    
    @Test
    public void test() throws Exception {
        String secret = Hmacs.generateSecret();
        String text = "Let's test this baby...";
        logger.info("mac {}", Hmacs.mac(secret, text));
    }
}
