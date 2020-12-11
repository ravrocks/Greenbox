package com.internal;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
@WebServlet
@MultipartConfig
public class UploadFile extends HttpServlet{
	private boolean isMultipart;
	   private String filePath;
	   private int maxFileSize = 40000000;
	   private int maxMemSize = 4 * 1024;
	   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		  
		  PrintWriter out = response.getWriter();
		  isMultipart = ServletFileUpload.isMultipartContent(request);
	      response.setContentType("text/html");
		  String userPsno = null,userName=null;
            Cookie[] cookies = request.getCookies();
            if(cookies !=null){
                for(Cookie cookie : cookies){
                	if(cookie.getName().equals("c_name")) userName = cookie.getValue().trim();
                    if(cookie.getName().equals("c_psno")) userPsno = cookie.getValue().trim();
                }
            }
            if(userName==null || userPsno==null) {
            	System.out.println("Security breach!");
            	return;
            }
		   try(Connection conn=new getConnection().getConnection();) {     
	           //Parsing loogic
	           DiskFileItemFactory factory = new DiskFileItemFactory();
	           
	           // maximum size that will be stored in memory
	           factory.setSizeThreshold(maxMemSize);
	        
	           // Location to save data that is larger than maxMemSize.
	           factory.setRepository(new File("c:\\temp"));

	           // Create a new file upload handler
	           ServletFileUpload upload = new ServletFileUpload(factory);
	        
	           // maximum file size to be uploaded.
	           upload.setSizeMax( maxFileSize );
	           List fileItems = upload.parseRequest(new ServletRequestContext(request));
	           // Process the uploaded file items
	           Iterator iter = fileItems.iterator();
	           InputStream inputStream = null;
	           ArrayList<String> flist=new ArrayList<String>();
	           ArrayList<String> tlist=new ArrayList<String>();
	           ArrayList<String> category_list=new ArrayList<String>();
	           String elist="";
	           
	           while(iter.hasNext()) 
	           {
	        	   FileItem item = (FileItem) iter.next();
	        	   if (item.isFormField()) {
	        		   String name = item.getFieldName();
	        		   String value = item.getString();
	        		   if(name.equalsIgnoreCase("filename"))
	        			   flist.add(value);
	        		   else if(name.equalsIgnoreCase("category"))
	        			   category_list.add(value);
	        		   else
	        			   tlist.add(value);
	        	   } else {
	        		   //System.out.println(item.getName());
	                   //System.out.println(item.getSize());
	                   //System.out.println(item.getContentType());
	                   elist=item.getContentType();
	                   inputStream = item.getInputStream();
	        	    }
	        	   if(inputStream!=null)
	        	   {
	        		   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        		   Date dt=new Date();
	        		   String query="insert into document_details_backup(docname,username,date_,tag2,contents,extension,userpsno,catname) values(?,?,?,'{"+tlist.get(0)+"}',?,?,?,?)";
	        		   PreparedStatement prepS = conn.prepareStatement(query);
        			   conn.setAutoCommit(false);
	        		   for(int i=0;i<flist.size();i++)
	        		   {
	        			   prepS.setString(1, flist.get(i).trim());
	        			   prepS.setString(2, userName);
	        			   java.sql.Date datezz=java.sql.Date.valueOf(sdf.format(dt));
	        			   prepS.setDate(3, datezz);
	        			   //prepS.setString(4, tlist.get(i));
	        			   
	        			   //prepS.setArray(4, conn.createArrayOf("VARCHAR", tlist.toArray()));
	        			   prepS.setBinaryStream(4, inputStream);
	        			   prepS.setString(5, elist);
	        			   prepS.setInt(6, Integer.parseInt(userPsno.trim()));
	        			   prepS.setString(7, category_list.get(i));
	        			   prepS.addBatch();
	        		   }
	        		   prepS.executeBatch();
	        		   conn.commit();
	                   prepS.close();
	                   flist=new ArrayList<String>();
	                   tlist=new ArrayList<String>();
	                   inputStream=null;
	        	   }
	           }
	           PreparedStatement find_super_mail=conn.prepareStatement("select email from userdata u where CAST(u.psno as varchar) in (select supervisor from userdata u2 where u2.psno="+userPsno+")");
	           ResultSet find_super_mail_result=find_super_mail.executeQuery();
	           find_super_mail_result.next();
	           String to_mail=find_super_mail_result.getString(1);
	           find_super_mail_result.close();
	           find_super_mail.close();
	           
	           if(!(to_mail.length()<1))
	           {
	        	   final String by_username=userName;
	        	   System.out.println("mail to -> "+to_mail);
	           new Thread(()->
	            {
	        	 //Logic for Mail Generation						   
				   // Recipient's email ID needs to be mentioned.
				   String to = to_mail;
				   
				   // Sender's email ID needs to be mentioned
				   String from = "swctic.greenbox@gmail.com";

				   // Assuming you are sending email from localhost
				   //String host = "localhost";

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
				      message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
				      // Set Subject: header field
				      message.setSubject("GreenBox Document Update");
				      
				      message.setContent("<div><p>A new document has been uploaded by <b>"+by_username+"</b>. Kindly, take necessary action.</p></div>", "text/html; "+ "charset=utf-8");
				      	Transport.send(message);
				      	System.out.println("Sent message successfully to "+to);
				      
				    	} catch (Exception mex) {
				    		mex.printStackTrace();
				    		System.err.println("Mail- Error: unable to send mail to supervisor..");
				    	}
			    
	              }).start();
	            }
	           //statz.close();
               conn.close();
               
               
		    }
             catch (Exception e) 
              {
            	 System.out.println("Error in Upload "+e.toString());
            	 out.print(e.toString().substring(60));
            	 System.err.print("Error in Upload "+e.toString());
                 //e.printStackTrace();
              }
		  }
     
     private String getHours(String f1,String f2)
     {
         double hxx=0.0;
         try{
         SimpleDateFormat format = new SimpleDateFormat("HH:mm");
         Date date1 = format.parse(f1);
         Date date2 = format.parse(f2);
         long difference = date2.getTime() - date1.getTime();
         hxx=(difference*0.001)/3600;
         }
         catch(Exception e)
         {
             System.err.print(e.toString());
         }
         return hxx+"";   
     }
     
}
