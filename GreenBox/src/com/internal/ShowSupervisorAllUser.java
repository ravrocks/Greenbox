package com.internal;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ShowSupervisorAllUser
 */
@WebServlet("/SSAllUser")
public class ShowSupervisorAllUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowSupervisorAllUser() {
        super();
        // TODO Auto-generated constructor stub
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
		        	System.err.print("ShowSupervisorALLUser- Security breach!");
		        	return;
		        }
		        ResultSet rs = null;
			    String uname= "\0";
				String upsno="\0";
				String uemail= "\0";
				String udomain="\0";
				
				JSONArray jarray_tosenddz = new JSONArray();
				Statement statement=null;
				try(Connection connection=new getConnection().getConnection();){
					statement=connection.createStatement();
					String sql ="SELECT name, psno, email, domain\r\n" + 
							"	FROM public.userdata where validity=1 and supervisor like 'MS';";
					PreparedStatement ps = connection.prepareStatement(sql); 
					rs=ps.executeQuery();
					int count=0;
					while(rs.next()){
					JSONObject jsonn_tosend = new JSONObject();  
					uname=rs.getString("name");
					upsno = rs.getInt("psno")+"";
					uemail=rs.getString("email");
					udomain=rs.getString("domain");
					
					jsonn_tosend.accumulate("countme",count+1);
					jsonn_tosend.accumulate("uname",uname);
					jsonn_tosend.accumulate("upsno",upsno);
					jsonn_tosend.accumulate("uemail",uemail);
					jsonn_tosend.accumulate("udomain",udomain);
					jarray_tosenddz.add(count++,jsonn_tosend);
					}
					rs.close();
					statement.close();
					connection.close();
					out.write(jarray_tosenddz.toString());
					} 
					catch (Exception e) {
					e.printStackTrace();
					System.err.print(e.toString());
					}
			}

}
