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
		  String confirmpass = request.getParameter("confirmpass");
		  String supervisor="MS";
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
