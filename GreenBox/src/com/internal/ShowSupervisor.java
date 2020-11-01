package com.internal;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ShowSupervisor
 */
@WebServlet("/ShowSuper")
public class ShowSupervisor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowSupervisor() {
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
				PrintWriter out = response.getWriter();  
			    //String requestz = request.getParameter("filters");
				String userPsno = null,userName=null;
		        Cookie[] cookies = request.getCookies();
		        if(cookies !=null){
		            for(Cookie cookie : cookies){
		            	if(cookie.getName().equals("c_name")) userName = cookie.getValue();
		                if(cookie.getName().equals("c_psno")) userPsno = cookie.getValue();
		            }
		        }
		        if(userName==null || userPsno==null || !userName.equalsIgnoreCase("admin")) {
		        	System.out.println("Security breach!");
		        	System.err.print("ShowTeams- Security breach!");
		        	return;
		        }
		        ResultSet rs = null;
			    String supname= "\0";
				String suppsno="\0";
				JSONArray jarray_tosendd = new JSONArray();
				Statement statement=null;
				try(Connection connection=new getConnection().getConnection();){
					statement=connection.createStatement();
					String sql ="SELECT DISTINCT name AS supervisorname,psno AS supervisorpsno FROM userdata WHERE validity=1 AND CAST(psno as VARCHAR) IN (SELECT supervisor FROM userdata);";
					PreparedStatement ps = connection.prepareStatement(sql); 
					rs=ps.executeQuery();
					int count=0;
					while(rs.next()){
					JSONObject jsonn_tosend = new JSONObject();  
					supname=rs.getString("supervisorname");
					suppsno = rs.getString("supervisorpsno");
					jsonn_tosend.accumulate("countme",count+1);
					jsonn_tosend.accumulate("supname",supname);
					jsonn_tosend.accumulate("suppsno",suppsno);
					jarray_tosendd.add(count++,jsonn_tosend);
					}
					rs.close();
					statement.close();
					connection.close();
					out.write(jarray_tosendd.toString());
					} 
					catch (Exception e) {
					e.printStackTrace();
					System.err.print(e.toString());
					}
			}

}
