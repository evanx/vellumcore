package vellum.crypto.asymetric;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import vellum.util.Base64;

public final class AsymmetricCipher {
    public static final String rsaAlgorithm = "RSA";
    static final String rsaAlgorithmModePadding = "RSA/ECB/PKCS1Padding";    
    static final String rsaProvider = null;
    static final int rsaKeySize = 2048;
    public static final String ecAlgorithm = "EC";
    static final String ecAlgorithmModePadding = "EC";
    static final String ecProvider = "SunEC";
    static final int ecKeySize = 256;
    
    String algorithm = rsaAlgorithm;
    String provider = rsaProvider;
    String algorithmModePadding = rsaAlgorithmModePadding;
    int keySize = rsaKeySize;
    PublicKey publicKey;
    PrivateKey privateKey;

    public AsymmetricCipher() {
    }
    
    public AsymmetricCipher(String algorithmType) {
        init(algorithmType);
    }

    public void init(String algorithmType) {
        if (algorithmType.equals(rsaAlgorithm)) {
            provider = rsaProvider;
            algorithm = rsaAlgorithm;
            algorithmModePadding = rsaAlgorithmModePadding;
            keySize = rsaKeySize;
        } else if (algorithmType.equals(ecAlgorithm)) {
            provider = ecProvider;
            algorithm = ecAlgorithm;
            algorithmModePadding = ecAlgorithmModePadding;
            keySize = ecKeySize;            
        }        
    }
    
    public AsymmetricCipher(int keySize) {
        this.keySize = keySize;
    }
        
    public void generateKeyPair() throws GeneralSecurityException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(keySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
    
    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public String getEncodedPublicKey() {
        byte[] encodedKey = publicKey.getEncoded();
        return Base64.encode(encodedKey);
    }
    
    public void setEncodedPublicKey(String key) throws GeneralSecurityException {
        byte[] encodedKey = Base64.decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
        publicKey = KeyFactory.getInstance(algorithm).generatePublic(keySpec);
    }
    
    public Cipher createEncryptCipher() throws GeneralSecurityException {
        Cipher encryptCipher = Cipher.getInstance(algorithmModePadding);
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return encryptCipher;
    }

    public Cipher createDecryptCipher() throws GeneralSecurityException {
        Cipher decryptCipher = Cipher.getInstance(algorithmModePadding);
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
        return decryptCipher;
    }

    public byte[] encrypt(byte[] bytes) throws Exception {
        return createEncryptCipher().doFinal(bytes);
    }
    
    public byte[] decrypt(byte[] bytes) throws Exception {
        return createDecryptCipher().doFinal(bytes);
    }
}
