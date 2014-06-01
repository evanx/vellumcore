package vellum.crypto.asymmetric;

import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vellum.crypto.asymetric.AsymmetricCipher;
import vellum.util.Base64;

public class AsymmetricCipherTest {
    static Logger logger = LoggerFactory.getLogger(AsymmetricCipherTest.class); 
    
    AsymmetricCipher cipher = new AsymmetricCipher(2048);

    @Test
    public void test() throws Exception {
        cipher.generateKeyPair();
        String text = "Let's test this baby...";
        byte[] encryptedBytes = cipher.encrypt(text.getBytes());
        logger.info("encrypted {}", Base64.encode(encryptedBytes));
        byte[] decryptedBytes = cipher.decrypt(encryptedBytes);
        String decryptedText = new String(decryptedBytes);
        Assert.assertEquals(text, decryptedText);
    }
}
