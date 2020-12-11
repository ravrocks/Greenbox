package com.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

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
 * Servlet implementation class EditOwnFile
 */
@WebServlet("/EditMyFile")
public class EditOwnFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditOwnFile() {
        super();
        // TODO Auto-generated constructor stub
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
				 String requestz = request.getParameter("editthis");
				 //System.out.println("Im Editing only.");
				  //File stream related logic/////////
		         InputStream inputStream = null;
				  try {
				  Connection conn=null;
				  PreparedStatement prepS=null;
				  ResultSet ress=null;
				  JsonParser jp = new JsonParser();
		          JsonElement je = jp.parse(requestz);
		          JsonArray jaaray=je.getAsJsonArray();
		          JsonElement joshObj=null;
		          String fileetype="";
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
		         			System.out.println("Date Parsing error "+e1.toString());
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
		        	 fileetype=ress.getString("extension");
		        	 inputStream=ress.getBinaryStream("contents");
		         }
		         
		         
		         ress.close();
		         prepS.close();
		         conn.close();

		         //int tmp=fileetype.indexOf("/");
		         //String ex=fileetype.substring(tmp+1,fileetype.length());
		         //System.out.println(fileetype);
		         String ex=returnFType(fileetype);
		         response.setContentType("application/html");
		         response.setCharacterEncoding("UTF-8");
		         request.getSession().setAttribute("editthis",requestz);
		         getServletConfig().getServletContext().getRequestDispatcher("/editFiles.jsp").forward(request, response);
		         //response.sendRedirect("editFiles.jsp");
				  }
				  	catch(Exception e)
				  		{
				  			System.out.println("Editing own error- "+e.toString());
				  		}
		}

			private static String returnFType(String contentotype)
			{
				String rtn="";
				switch(contentotype)
				{
				case "application/msword": rtn="doc";
				break;
				case "application/vnd.openxmlformats-officedocument.wordprocessingml.document": rtn="docx";
				break;
				case "application/vnd.openxmlformats-officedocument.wordprocessingml.template": rtn="dotx";
				break;	
				case "application/pdf": rtn="pdf";
				break;
				case "application/rtf": rtn="rtf";
				break;
				case "image/png": rtn="png";
				break;
				case "image/jpeg": rtn="jpeg";
				break;
				case "text/plain": rtn="txt";
				break;
				case "application/vnd.ms-excel": rtn="xls";
				break;
				case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet": rtn="xlsx";
				break;
				case "application/vnd.openxmlformats-officedocument.spreadsheetml.template": rtn="xltx";
				break;
				case "application/vnd.ms-excel.sheet.macroEnabled.12": rtn="xlsm";
				break;
				case "application/vnd.ms-excel.template.macroEnabled.12": rtn="xltm";
				break;
				case "application/vnd.ms-excel.addin.macroEnabled.12": rtn="xlam";
				break;
				case "application/vnd.ms-excel.sheet.binary.macroEnabled.12": rtn="xlsb";
				break;
				case "application/vnd.ms-powerpoint": rtn="ppt";
				break;
				case "application/vnd.openxmlformats-officedocument.presentationml.presentation": rtn="pptx";
				break;
				case "application/vnd.openxmlformats-officedocument.presentationml.slideshow": rtn="ppsx";
				break;
				case "text/csv": rtn="csv";
				default: rtn="txt";
				}
				return rtn;
			}

}
