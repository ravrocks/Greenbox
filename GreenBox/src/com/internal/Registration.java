package com.internal;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Registration extends HttpServlet{
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		  response.setContentType("text/html");
		  PrintWriter out = response.getWriter();  
		  String name = request.getParameter("name");
		  String psno = request.getParameter("psno");
		  String email = request.getParameter("email");
		  String designation = request.getParameter("designation");
		  String password = request.getParameter("password");
		  String supervisor= request.getParameter("supervisor");
		  String confirmpass = request.getParameter("confirmpass");
		  if(supervisor==null || supervisor.equalsIgnoreCase("Optional"))
			  supervisor="MS";
		  String usertype="user";
		  String domain = request.getParameter("domain");
		  int validity=0;
		  Connection con=null;
		   boolean status=false;
		   try {
				
			    Class.forName("com.mysql.jdbc.Driver");
			    con = new getConnection().getConnection();
			    String psn = "select psno from userdata where psno=?";
			    PreparedStatement ps1 = con.prepareStatement(psn);   
			    ps1.setInt(1, Integer.parseInt(psno));
			    ResultSet rs1=ps1.executeQuery();
			    status=rs1.next();
			    System.out.print(status);
		    
		        if(status) {
		        	con.close();
		        	//System.out.println("status: "+status);
                    request.setAttribute("rfailure", "Registration Failed");
		        	RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
				rd.forward(request, response);
		          }
			    else {
					    String pattern = "MM";
                             //SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                             //String month_created=Integer.parseInt(simpleDateFormat.format(new Date()))+"";
                             String query = "insert into userdata values(?,?,?,?,?,?,?,?,?)";
					    PreparedStatement ps = con.prepareStatement(query);   
					    ps.setString(1, name);
					    ps.setInt(2, Integer.parseInt(psno));
					    ps.setString(3, email);
					    ps.setString(4, designation);
					    ps.setString(5, password);
					    
					    ps.setString(6, supervisor);
					    ps.setString(7, usertype);
					    ps.setString(8, domain);
                        ps.setInt(9, validity);
					    ps.executeUpdate();		    
					    System.out.println("successfuly inserted");
					    ps.close();
					    con.close();
					    request.setAttribute("success", "Registration successful.");
					  //Logic for Mail Generation						   
						   // Recipient's email ID needs to be mentioned.
						   String to = "gr-ravi@lntecc.com";

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
						      message.setSubject("GreenBox User Approval");
						      
						      message.setContent("<div><p>Kindly, approve the access for the new user-</p></div>"
						      		+ "<div><table style='border: 1px solid black'>"
						      		+ "<tr ><td style='border: 1px solid black'>UserName</td><td style='border: 1px solid black'>"+name+"</td></tr>"
						      		+ "<tr ><td style='border: 1px solid black'>Psno</td><td style='border: 1px solid black'>"+psno+"</td></tr>"
						      		+ "<tr ><td style='border: 1px solid black'>Email</td><td style='border: 1px solid black'>"+email+"</td></tr>"
						      		+ "<tr ><td style='border: 1px solid black'>Designation</td><td style='border: 1px solid black'>"+designation+"</td></tr>"
						      		+ "<tr ><td style='border: 1px solid black'>Team</td><td style='border: 1px solid black'>"+domain+"</td></tr>"
						      		+ "</table></div>", "text/html; "+ "charset=utf-8");
						      	Transport.send(message);
						      	System.out.println("Sent message successfully to "+email);
						      
						    	} catch (Exception mex) {
						    		mex.printStackTrace();
						    		System.err.println("Registration- Error: unable to send alert to user..");
						    	}
					    
                     RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
						rd.forward(request, response);
					   } 
				   }catch (ClassNotFoundException | SQLException e) {
					   		try {
								con.close();
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						    e.printStackTrace();
						   }
				   
		  }
		 
}