package com.internal;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Servlet implementation class RemoveMyFile
 */
@WebServlet("/RemoveFile")
public class RemoveMyFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemoveMyFile() {
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
				String userPsno = null,userName=null;
		        Cookie[] cookies = request.getCookies();
		        if(cookies !=null){
		            for(Cookie cookie : cookies){
		            	if(cookie.getName().equals("c_name")) userName = cookie.getValue();
		                if(cookie.getName().equals("c_psno")) userPsno = cookie.getValue();
		            }
		        }
		        if(userName==null || userPsno==null) {
		        	System.out.println("Security breach!");
		        	return;
		        }
				 String requestz = request.getParameter("removethis");
				 
				  try {
				  Connection conn=null;
				  PreparedStatement prepS=null;
				  ResultSet ress=null;
				  JsonParser jp = new JsonParser();
		          JsonElement je = jp.parse(requestz);
		          JsonArray jaaray=je.getAsJsonArray();
		          JsonElement joshObj=null;
		          String stat_of_file=null;
		          ///////////////////////////////////
		          
		          joshObj= jaaray.get(0);
		          org.json.JSONObject off=new org.json.JSONObject (joshObj.toString());
		          String docname=off.get("documentname").toString().trim();
		          //String uname=off.get("username").toString().trim();
		          String udate=off.get("uploaddate").toString().trim();
		          String ctags=off.get("tags").toString().trim();

		          ctags = ctags.replace("\"", "");

		          ///Logic for handling malformed tags
		          StringBuilder sbr = new StringBuilder();
		          for (char c : ctags.toCharArray()) {
		        	  if (!((c=='\\')||(c=='[')||(c==']')||(c=='/'))) 
		        		  sbr.append(c);

		          }
		          ////////////////////////////////////

		          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		          Date sdate=null;
		          try { 
		        	  sdate = sdf.parse(udate);
		        	  sdate = new java.sql.Date(sdate.getTime());
		          } catch (Exception e1) {
		        	  response.getWriter().print(e1.toString().substring(0, 60));
		        	  System.err.println("Date Parsing error "+e1.toString());
		          }
		          conn=new getConnection().getConnection();
		          String queryx="SELECT * FROM document_details_backup where docname=? and tag2='{"+sbr.toString()+"}' and date_=? and username=?";
		          prepS=conn.prepareStatement(queryx);
		          prepS.setString(1,docname);
		          prepS.setDate(2, (java.sql.Date)sdate);
		          prepS.setString(3,userName);
		          ress=prepS.executeQuery();
		          
		          while(ress.next())
		          {
		        	  stat_of_file=ress.getString("status");
		        	  if(stat_of_file.equalsIgnoreCase("Pending"))
		        	  {
		        		  PreparedStatement pr_one=conn.prepareStatement("DELETE FROM document_details_backup WHERE docname=? and tag2='{"+sbr.toString()+"}' and date_=? and username=? and status='Pending'");
		        		  pr_one.setString(1,docname);
		        		  pr_one.setDate(2, (java.sql.Date)sdate);
		        		  pr_one.setString(3,userName);
		        		  int zz=pr_one.executeUpdate();
		        	      //System.out.print("Deleted "+zz+" rows FROM db");
		        	      pr_one.close();
		        	  }
		        	  else
		        	  {
		        		  PreparedStatement pr_one=conn.prepareStatement("DELETE FROM document_details_backup WHERE docname=? and tag2='{"+sbr.toString()+"}' and date_=? and username=? and status='Approved'");
		        		  pr_one.setString(1,docname);
		        		  pr_one.setDate(2, (java.sql.Date)sdate);
		        		  pr_one.setString(3,userName);
		        		  int zz=pr_one.executeUpdate();
		        	      //System.out.print("Deleted "+zz+" rows FROM db");
		        	      pr_one.close();
		        	      ////////////////////////////////////////
		        	      PreparedStatement pr_two=conn.prepareStatement("DELETE FROM document_details WHERE docname=? and tag2='{"+sbr.toString()+"}' and date_=? and username=?");
		        	      pr_two.setString(1,docname);
		        	      pr_two.setDate(2, (java.sql.Date)sdate);
		        	      pr_two.setString(3,userName);
		        		  int zzz=pr_two.executeUpdate();
		        	      //System.out.print("Deleted "+zzz+" rows FROM 2nd DB");
		        	      pr_two.close();
		        	  }
		        	  break;
		          }
		          ress.close();
		          prepS.close();
		          conn.close();
		          
				  }
				  catch(Exception e)
				  {
					  response.getWriter().print(e.toString().substring(0, 60));
					  System.err.print(e.toString());
				  }
	     }

}
