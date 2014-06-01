package vellum.crypto;

import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vellum.crypto.hmac.Hmac;

public class HmacTest {
    static Logger logger = LoggerFactory.getLogger(HmacTest.class); 
    
    Hmac hmac = new Hmac();

    @Test
    public void test() throws Exception {
        String secret = hmac.generateSecret();
        String text = "Let's test this baby...";
        String mac = hmac.mac(text);
        logger.info("mac {}", mac);
        hmac = new Hmac(secret);
        Assert.assertEquals(mac, hmac.mac(text));
    }
}
