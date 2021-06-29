package com.internal;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
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
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ForgotPass
 */
@WebServlet("/ForgotPass")
public class ForgotPass extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ForgotPass() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		  Date visitDate = null;
		  response.setContentType("text/html");
		  System.out.println("In ForgotPass here");
		  String psno = request.getParameter("fpsno");
		  Connection con=null;
		  boolean status=false;
		  int count_for_Reset=-1;String email="",paszz="";
		  try
		  {
			    Class.forName("com.mysql.jdbc.Driver");
			    con = new getConnection().getConnection();
			    String psn = "select * from userdata where psno=?";
			    PreparedStatement ps1 = con.prepareStatement(psn);   
			    ps1.setInt(1, Integer.parseInt(psno));
			    ResultSet rs1=ps1.executeQuery();
			    status=rs1.next();
			    if(status!=false)
			    	{
			    	count_for_Reset=rs1.getInt("fp_counter");
			    	email = rs1.getString("email");
			    	paszz= rs1.getString("password");
			    	}
			    rs1.close();
			    ps1.close();
			    if((count_for_Reset!=-1)&&(count_for_Reset<5))
			    { 
			    	Statement updateSS=con.createStatement();
			    	updateSS.executeUpdate("update userdata set fp_counter="+(count_for_Reset+1)+" where psno="+psno);          	  
		          	updateSS.close();
			    }
			    con.close();
			    
		  }
		  catch(Exception e)
		  {
			  e.printStackTrace();
		  }
		  
		  //Logic for Mail Generation
		  
		   String result;
		   
		   // Recipient's email ID needs to be mentioned.
		   String to = email;

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

		   if(count_for_Reset<5)
		   {
		   try {
		      // Create a default MimeMessage object.
		      MimeMessage message = new MimeMessage(mailSession);
		      
		      // Set From: header field of the header.
		      message.setFrom(new InternetAddress(from));
		      
		      // Set To: header field of the header.
		      message.addRecipient(Message.RecipientType.TO,
		                               new InternetAddress(to));
		      	      
		      // Set Subject: header field
		      message.setSubject("GreenBox Password Reset");
		      
		      
		      message.setContent("<div><p>Your old password has been successfully sent.</p><p> The password reset attempt is now "+(count_for_Reset+1)+".</p><p> Please find your credentials below-</p></div>"
		      		+ "<div><table style='border: 1px solid black'>"
		      		+ "<tr ><td style='border: 1px solid black'>UserName</td><td style='border: 1px solid black'>"+psno+"</td></tr>"
		    		+ "<tr ><td style='border: 1px solid black'>Password</td><td style='border: 1px solid black'>"+paszz+"</td></tr>"
		      		+ "</table></div>", "text/html; "+ "charset=utf-8");
		      	Transport.send(message);
		      	System.out.println("Sent message successfully to "+email);
		      
		    	} catch (Exception mex) {
		    		mex.printStackTrace();
		    		result = "Error: unable to send message.."+email+"..";
		    	}
		   		request.setAttribute("reset_stat", "Password Reset successful.");
		   		RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
		   		rd.forward(request, response);
		   }
		   else
		   {
			   request.setAttribute("reset_stat_error", "Password Reset unsuccessful.");
	           RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
			   rd.forward(request, response);  
		   }	   
	}

}
