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

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AddSupervisor
 */
@WebServlet("/AddSupervisor")
public class AddSupervisor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddSupervisor() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String Inpsno = request.getParameter("inputMDEx3");
		
		try(Connection con=new getConnection().getConnection();)
		{
			Class.forName("com.mysql.jdbc.Driver");
			String psn = "UPDATE userdata SET supervisor=? WHERE psno=? and validity=1;";
		    PreparedStatement ps1 = con.prepareStatement(psn);   
		    ps1.setString(1, Inpsno);
		    ps1.setInt(2, Integer.parseInt(Inpsno));
		    
		    int rs1=ps1.executeUpdate();
		    if(rs1==0)
		    	System.out.println("No rows affected!");
		    	else
		    		System.out.print("Success!!");
		    ps1.close();
		    con.close();
		    
		}
		catch(Exception e)
		{
			System.err.println(e.toString());
			System.out.print(e.toString());
		}
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin.jsp");
                		dispatcher.forward(request,response);
		
	}

}
