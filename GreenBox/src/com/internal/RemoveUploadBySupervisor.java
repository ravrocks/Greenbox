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
@WebServlet("/Removee")
public class RemoveUploadBySupervisor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemoveUploadBySupervisor() {
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
         
         System.out.println("original is- "+ctags.toString()+" len is "+ctags.length());
         ///Logic for handling malformed tags
         StringBuilder sbr = new StringBuilder();
 			for (char c : ctags.toCharArray()) {
 		    if (!((c=='\\')||(c=='[')||(c==']')||(c=='/'))) 
 		    	sbr.append(c);
 		    
 			}
 			String cleaned = sbr.toString();
         System.out.println("after edit- "+cleaned+" len is "+cleaned.length());
         ////////////////////////////////////

         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         Date sdate=null;
         try { 
        	 	sdate = sdf.parse(udate);
        	 	sdate = new java.sql.Date(sdate.getTime());
        	 	
         	} catch (Exception e1) {
         			System.out.println("Date Parsing error "+e1.toString());
         	}
         
         String queryx="DELETE FROM document_details_backup WHERE docname=? and tag2='{"+cleaned+"}' and date_=? and username=? and userpsno=? and status='Pending'";
         prepS=conn.prepareStatement(queryx);
         prepS.setString(1,docname);
         prepS.setDate(2, (java.sql.Date)sdate);
         prepS.setString(3,user_upload_name);
         prepS.setInt(4,Integer.parseInt(user_upload_psno));         
         int zz=prepS.executeUpdate();
         System.out.print("Deleted "+zz+" rows");
         prepS.close();        
         conn.close();
		  }
     
	catch(Exception e)
		  		{
					response.getWriter().print(e.toString());
		  			System.out.println("Remove error- "+e.toString());
		  			System.err.print("Remove error- "+e.toString());
		  		}
	}

}
