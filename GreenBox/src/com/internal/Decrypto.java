package com.internal;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
public class Decrypto {

	public static void decryptit(byte cipherText[])
	{
		try {
		Signature sign = Signature.getInstance("SHA256withRSA");
	      
	      //Creating KeyPair generator object
	      KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
	      
	      String pubx="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhxi6nHmllwHADJvkHaUGE2c7RGrPxltERN6OvJKVrQXUKCLFV0yWN+j4TR8ud51sqno7WN16ucoer4EAYoUI6xkVKo8OAnprO43NjTuJYIWJ1a7RoG1UyH74z75bl93GCR58TxpJqWb5WygdDgd6JaEyaxrmGhtPxG/wvp56Zk/qvOwf689YnWcjtuQrdtVwn+uFcUrg0dd2Z8TCPWYU+mFsx19lyrqoae5MQthg+u/UFQAUUQ9vUYESCOiqdyNAdBotoCb1UBsWo3Orh/0xEyE4sLbbek/5eKfTlqBCnbUdfj9uQQyqeSCBB5vK98M4mX1lHVsY6fI970mnQs9rfwIDAQAB";
	      byte [] pubkey=DatatypeConverter.parseBase64Binary(pubx);
	      X509EncodedKeySpec spec = new X509EncodedKeySpec(pubkey);
	      KeyFactory kf = KeyFactory.getInstance("RSA");
	      
	      //Creating a Cipher object
	      Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

	      
	      //Initializing the same cipher for decryption
	      cipher.init(Cipher.DECRYPT_MODE, kf.generatePublic(spec));
	      //cipher.update(cipherText);
	      //Decrypting the text
	      byte[] decipheredText = cipher.doFinal(cipherText);
	      System.out.println(new String(decipheredText));
	   }
		catch(Exception e)
		{
		  System.out.println("Decryption problem: "+e.toString());
		}
	}
}
