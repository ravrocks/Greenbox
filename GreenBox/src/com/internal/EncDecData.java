package com.internal;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncDecData {
    private static final String ALGO = "AES";

    public static byte[] encrypt(byte[] Data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data);
        //String encryptedValue = new BASE64Encoder().encode(encVal);
        return encVal;
    }

    public static byte[] decrypt(byte[] encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);

        byte[] decValue = c.doFinal(encryptedData);
        return decValue;
    }

    private static Key generateKey() throws Exception {
    	// decode the base64 encoded string
    	byte[] decodedKey = Base64.getDecoder().decode("C8D7A6870BF79AE5851CC5BA063CAFF2F5C62F90BF2DC1F50AA262D4FA9FEF77");
    	// rebuild key using SecretKeySpec
    	SecretKey originalKey = new SecretKeySpec(decodedKey, 0, 32, "AES"); 
        return originalKey;
    }
}
