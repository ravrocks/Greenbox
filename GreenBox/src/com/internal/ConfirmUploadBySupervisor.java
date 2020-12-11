package com.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import org.apache.commons.codec.binary.Base64;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Servlet implementation class ConfirmUploadBySupervisor
 */
@WebServlet("/Confirmm")
public class ConfirmUploadBySupervisor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConfirmUploadBySupervisor() {
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
		 String requestz = request.getParameter("confirmthis");
		 

     try(Connection conn=new getConnection().getConnection();) {
		  PreparedStatement prepS=null;
		  PreparedStatement prepSS=null;
		  JsonParser jp = new JsonParser();
          JsonElement je = jp.parse(requestz);
          JsonArray jaaray=je.getAsJsonArray();
          JsonElement joshObj=null;
         ///////////////////////////////////
         
         joshObj= jaaray.get(0);
         org.json.JSONObject off=new org.json.JSONObject (joshObj.toString());
         String docname=off.get("documentname").toString().trim();
         //String uname=off.get("username").toString().trim();
         String udate=off.get("uploaddate").toString().trim();
         String ctags=off.get("tags").toString().trim();
         String user_upload_name=off.get("user_upload_by").toString().trim();
		 String user_upload_psno=off.get("userpsno_upload_by").toString().trim();
        
         ctags = ctags.replace("\"", "");
         
         ///Logic for handling malformed tags
          StringBuilder sbr = new StringBuilder();
 			for (char c : ctags.toCharArray()) {
 		    if (!((c=='\\')||(c=='[')||(c==']')||(c=='/'))) 
 		    	sbr.append(c);
 		    
 			}
 			String cleaned = sbr.toString();
 			
 			
         ////////////////////////////////////

         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         Date sdate=null,today_Date=null;
         try { 
        	 	sdate = sdf.parse(udate);
        	 	sdate = new java.sql.Date(sdate.getTime());
        	 	today_Date = new java.sql.Date(System.currentTimeMillis());
        	 	
         	} catch (Exception e1) {
         			System.out.println("Date Parsing error "+e1.toString());
         	}
         
         conn.setAutoCommit(false);
         String queryx="UPDATE document_details_backup SET status='Approved', appdate=?, appby=? WHERE docname=? and tag2='{"+cleaned+"}' and date_=? and username=? and userpsno=?";
         prepS=conn.prepareStatement(queryx);
         prepS.setDate(1, (java.sql.Date)today_Date);
         prepS.setString(2, userName.trim());
         prepS.setString(3,docname);
         prepS.setDate(4, (java.sql.Date)sdate);
         prepS.setString(5,user_upload_name);
         prepS.setInt(6,Integer.parseInt(user_upload_psno));         
         prepS.executeUpdate();
         prepS.close();
         
         
         String copystuff="INSERT INTO document_details(id,docname, username, date_, tag2, contents, extension, appby, appdate, userpsno, catname)\r\n" + 
         		"SELECT id,docname, username, date_, tag2, contents, extension, appby, appdate, userpsno, catname FROM  document_details_backup WHERE docname=? and tag2='{"+cleaned+"}' and date_=? and username=? and userpsno=? and status like 'Approved'";
         prepSS=conn.prepareStatement(copystuff);
         prepSS.setString(1,docname);
         prepSS.setDate(2, (java.sql.Date)sdate);
         prepSS.setString(3,user_upload_name);
         prepSS.setInt(4,Integer.parseInt(user_upload_psno));         
         prepSS.executeUpdate();
         
         prepSS.close();
         conn.commit();
         conn.close();
		  }
	catch(Exception e)
		  		{
					response.getWriter().print(e.toString());
		  			System.out.println("Confirm error- "+e.toString());
		  			System.err.print("Confirm error- "+e.toString());
		  		}
	}

}
