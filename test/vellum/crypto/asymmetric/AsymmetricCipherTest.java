package vellum.crypto.asymmetric;

import junit.framework.Assert;
import org.junit.Test;
import vellum.crypto.asymetric.AsymmetricCipher;
import vellum.util.Base64;

public class AsymmetricCipherTest {
    AsymmetricCipher cipher = new AsymmetricCipher(2048);

    @Test
    public void test() throws Exception {
        cipher.generateKeyPair();
        String text = "Let's test this baby...";
        byte[] encryptedBytes = cipher.encrypt(text.getBytes());
        System.out.println("encrypted: " + Base64.encode(encryptedBytes));
        byte[] decryptedBytes = cipher.decrypt(encryptedBytes);
        String decryptedText = new String(decryptedBytes);
        Assert.assertEquals(text, decryptedText);
    }
}
