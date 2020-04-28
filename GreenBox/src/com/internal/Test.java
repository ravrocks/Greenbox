package com.internal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;

public class Test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Path path = Paths.get("E:\\LNT_GreenBox\\test.txt");
	    byte[] data = Files.readAllBytes(path);
	    //Encrypto.encryptit(data);
	    //byte [] cipher=Encrypto.getEncryptedData();
	    //System.out.println(new String(cipher, "UTF8"));
	    //Decrypto.decryptit(cipher);
	    ArrayList<String> tlist=new ArrayList<String>();
	    tlist.add("sample,tic");
	    
	    System.out.println(tlist.get(0));
	    
	    
	}

}
