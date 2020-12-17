package com.internal;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;

public class SendMail {

	public SendMail(String to_send,String contentz,String subz) {
		// TODO Auto-generated constructor stub
		//Logic for Mail Generation
		  
		   String result;
		   
		   // Recipient's email ID needs to be mentioned.
		   String email = to_send;

		   // Sender's email ID needs to be mentioned
		   String from = "swctic.greenbox@gmail.com";

		   // Get system properties object
		   Properties props = System.getProperties();

		   // Setup mail server
		      props.put("mail.smtp.host", "smtp.gmail.com");    
	          props.put("mail.smtp.socketFactory.port", "465");    
	          props.put("mail.smtp.socketFactory.class",    
	                    "javax.net.ssl.SSLSocketFactory");    
	          props.put("mail.smtp.auth", "true");    
	          props.put("mail.smtp.port", "465"); 

		   // Get the default Session object.
		   Session mailSession = Session.getInstance(props,new javax.mail.Authenticator() {    
	           protected PasswordAuthentication getPasswordAuthentication() {    
	               return new PasswordAuthentication(from,"Lnt@123456");  
	               }});

		   
		   try {
		      // Create a default MimeMessage object.
		      MimeMessage message = new MimeMessage(mailSession);
		      
		      // Set From: header field of the header.
		      message.setFrom(new InternetAddress(from));
		      
		      
		      // Set To: header field of the header.
		      message.addRecipient(Message.RecipientType.TO,
		                               new InternetAddress(email));
		      message.addRecipient(Message.RecipientType.BCC, new InternetAddress(
		              "djeswani@lntecc.com"));
		      
		      // Set Subject: header field
		      message.setSubject(subz);
		      
		      message.setContent(contentz, "text/html; "+ "charset=utf-8");
		      	Transport.send(message);
		      	System.out.println("Sent message successfully to "+email);
		      
		    	} catch (Exception mex) {
		    		mex.printStackTrace();
		    		result = "Error: unable to send message.. to "+email+"..";
		    	}
	}

}
