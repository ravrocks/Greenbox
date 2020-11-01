package com.internal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RemoveSupervisor
 */
@WebServlet("/RemoveSupervisor")
public class RemoveSupervisor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemoveSupervisor() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
				String Inpsno = request.getParameter("inputMDEx7");
				
				try(Connection con=new getConnection().getConnection();)
				{
					Class.forName("com.mysql.jdbc.Driver");
					String psn = "UPDATE userdata SET supervisor=? WHERE supervisor=?";
				    PreparedStatement ps1 = con.prepareStatement(psn);   
				    ps1.setString(1, "MS");
				    ps1.setString(2, Inpsno);
				    int rs1=ps1.executeUpdate();
				    if(rs1==0)
				    	System.out.println("No rows affected!");
				    	else
				    		System.out.print("Success!!");
				    ps1.close();
				    
				    String psn2="UPDATE userdata SET supervisor=? WHERE psno=?";
				    PreparedStatement ps2 = con.prepareStatement(psn2);   
				    ps2.setString(1, "MS");
				    ps2.setInt(2, Integer.parseInt(Inpsno));
				    int rs2=ps2.executeUpdate();
				    if(rs2==0)
				    	System.out.println("No rows affected!");
				    else
				    		System.out.print("Success!!");
				    
				    ps2.close();
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
