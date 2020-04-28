package com.internal;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;

import java.io.Writer;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

import java.util.Base64;

public class Encrypto_onetime {
	static byte[] cipherText;
	static private Base64.Encoder encoder = Base64.getEncoder();
	
	 static private void writeBase64(Writer out,Key key)throws java.io.IOException
	 {
		byte[] buf = key.getEncoded();
		out.write(encoder.encodeToString(buf));
		out.write("\n");
	 }
	 
	public static void encryptit(byte input[])
	{
		try {
		//Creating a Signature object
	      Signature sign = Signature.getInstance("SHA256withRSA");
	      
	      //Creating KeyPair generator object
	      KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
	      
	      //Initializing the key pair generator
	      keyPairGen.initialize(2048);
	      
	      //Generating the pair of keys
	      KeyPair pair = keyPairGen.generateKeyPair();      
	      
	      Writer out = null;
	      String outFile = null;
	      try {
	  	    if ( outFile != null ) out = new FileWriter(outFile + ".key");
	  	    else out = new OutputStreamWriter(System.out);

	  	    System.err.println("Private key format: " +
	  			       pair.getPrivate().getFormat());
	  	    out.write("-----BEGIN RSA PRIVATE KEY-----\n");
	  	    writeBase64(out, pair.getPrivate());
	  	    out.write("-----END RSA PRIVATE KEY-----\n");

	  	    if ( outFile != null ) {
	  		out.close();
	  		out = new FileWriter(outFile + ".pub");
	  	    }

	  	    System.out.println("Public key format: " +
	  	    		pair.getPublic().getFormat());
	  	    out.write("-----BEGIN RSA PUBLIC KEY-----\n");
	  	    writeBase64(out, pair.getPublic());
	  	    out.write("-----END RSA PUBLIC KEY-----\n");
	  	} finally {
	  	    if ( out != null ) out.close();
	  	}
	      //Creating a Cipher object
	      Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	        
	      //Initializing a Cipher object
	      cipher.init(Cipher.ENCRYPT_MODE, pair.getPublic());
		  
	      //Adding data to the cipher
	      //byte[] input = "Welcome to Tutorialspoint".getBytes();	  
	      	
		  
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
		return Encrypto_onetime.cipherText;
	}
}
