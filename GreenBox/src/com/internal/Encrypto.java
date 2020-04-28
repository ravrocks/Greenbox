package com.internal;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;

public class Encrypto {
	static byte[] cipherText;
	public static void encryptit(byte input[])
	{
		try {
		//Creating a Signature object
	      Signature sign = Signature.getInstance("SHA256withRSA");
	      
	      //Creating KeyPair generator object
	      KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
	      
	      //Initializing the key pair generator
	      //keyPairGen.initialize(2048);
	      
	      //Generating the pair of keys
	      //KeyPair pair = keyPairGen.generateKeyPair();      
	      
	      
	      //Creating a Cipher object
	      Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	        
		  String privx="MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCHGLqceaWXAcAMm+QdpQYTZztEas/GW0RE3o68kpWtBdQoIsVXTJY36PhNHy53nWyqejtY3Xq5yh6vgQBihQjrGRUqjw4Cems7jc2NO4lghYnVrtGgbVTIfvjPvluX3cYJHnxPGkmpZvlbKB0OB3oloTJrGuYaG0/Eb/C+nnpmT+q87B/rz1idZyO25Ct21XCf64VxSuDR13ZnxMI9ZhT6YWzHX2XKuqhp7kxC2GD679QVABRRD29RgRII6Kp3I0B0Gi2gJvVQGxajc6uH/TETITiwttt6T/l4p9OWoEKdtR1+P25BDKp5IIEHm8r3wziZfWUdWxjp8j3vSadCz2t/AgMBAAECggEAJxcFsA8W1dngywU0fUtptaWSJZrOYfO4jkDjhjbX7keYesCM31S+xpC3gAE8vcmkTrOthmoXf7i3JksqZFdgHD6WvEjGYfswGfxO9MW8W1xoZJb/+u6HsLfkoJ/Q+uFl0u+qQm010TmFf1XdClkpg8ffwdP3rqgmbY2CWHWcrwuW+nfAx2axSdDKB+L10mNmqr/IRj7x/+Zy4SxM+qIQeEUMTiT60k+AWBlYblnop08w5NSUNJao/71io7nC+XpMVYiD7TeOIkTBNpdn/p9sked6uo5zJAPiz3k9nN3INkWHauYuZ/F0r9knAlH9qhHCn+TqSovJ7n3ENFyhYfyA2QKBgQDDs9SSZEda8R4AqjpqZavlHqsfLl3LlYXEy2f/IvuS+s68ALZsAzJ8NXCdgYzBN8ewVH0eDoRPSfVBCSLGF6Ha67XSIEYCrgl6H5c1OU3TKaQkfDLnK6bwy2uF5X63IaQhknO8ftk9k6ddM+gQ0ulrS9xv/EdIAL0KLZwhX01uBQKBgQCwuJF7NTcrtuwxERrOj/N2mtrdp6IIhU6zJDJX2MdRmK7GtfvWyJgUjvjSfluijcsitlOCUeUN03wtpd8L7L0ON5w2z2n+qffAn3JfFkbHqprkTocJUgjwp8fn0dYNTLZBQ8DgdA0obxSic0kGBsfWbxMLFmJkmM5ZmGI1yPfmswKBgAwH+TCKw6w+mIaX8IFsALZUPVbsZc3HQnMHwjv4LZkqnB0Iui5HwAnMy0CTn1CdmkQn3Y1EfTPWqyI1apLdDxFDXmktnMA7bM+6M3SNMlCiBbpMfGCj2V6CUvjnU/P0OH/r3fufnMFhW2+qakDmhn0XK9UioEs36GdAyJFwp3T1AoGAcMHTUaHqyeG1YrlfOV1wIsU70SIcqQMl6/e0h4ix5MZe/jsvrSPgpX10Fhq5rUZ7znI3ZneKhv/hw96KKrmrB19ENVSydONOSYFzIGi+ULfyjKnuT2wgeC0VyDyDL4k64PmyH4TswFff+qdEeKvq5k+I/xIRRc6aG2C3iUnpozECgYBG5B4zaR00QE2LOwFcb74WvWt8KxZwF5zovL3ybOj+z4NBf1Ybcf1uXhkgae1z3mv5pf+ohE0BsbShpWWE9JPGgIUJja4lDB5nApErElr6sKKVUYI+T2rOhIt4LqlvCIEQNnzj7n2X5dq8r4RiCAvAmm85QcNebDY1wQvaVgOlLw==";
		  byte[] privKey=DatatypeConverter.parseBase64Binary(privx);
		  PKCS8EncodedKeySpec spec2 = new PKCS8EncodedKeySpec(privKey);
		  KeyFactory kf2 = KeyFactory.getInstance("RSA");
		  
	      //Initializing a Cipher object
	      cipher.init(Cipher.ENCRYPT_MODE, kf2.generatePrivate(spec2));
		  
	      //Adding data to the cipher
	      //byte[] input = "Welcome to Tutorialspoint".getBytes();	  
	      	
		  cipher.update(input);
	      //encrypting the data
	      cipherText = cipher.doFinal();	 
	      //System.out.println(new String(cipherText, "UTF8"));
		}
		catch(Exception e)
		{
			System.out.println("Error in Encryption: "+e.toString());
		}
	}
	public static byte[] getEncryptedData()
	{
		return Encrypto.cipherText;
	}
}
