package com.internal;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.internal.getConnection;
import com.sun.xml.internal.fastinfoset.util.StringArray;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
@WebServlet
@MultipartConfig
public class UploadFile extends HttpServlet{
	private boolean isMultipart;
	   private String filePath;
	   private int maxFileSize = 40000000;
	   private int maxMemSize = 4 * 1024;
	   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		  
		  PrintWriter out = response.getWriter();
		  isMultipart = ServletFileUpload.isMultipartContent(request);
	      response.setContentType("text/html");
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
		   try(Connection conn=new getConnection().getConnection();) {     
	           //Parsing loogic
	           DiskFileItemFactory factory = new DiskFileItemFactory();
	           
	           // maximum size that will be stored in memory
	           factory.setSizeThreshold(maxMemSize);
	        
	           // Location to save data that is larger than maxMemSize.
	           factory.setRepository(new File("c:\\temp"));

	           // Create a new file upload handler
	           ServletFileUpload upload = new ServletFileUpload(factory);
	        
	           // maximum file size to be uploaded.
	           upload.setSizeMax( maxFileSize );
	           List fileItems = upload.parseRequest(new ServletRequestContext(request));
	           // Process the uploaded file items
	           Iterator iter = fileItems.iterator();
	           InputStream inputStream = null;
	           ArrayList<String> flist=new ArrayList<String>();
	           ArrayList<String> tlist=new ArrayList<String>();
	           ArrayList<String> category_list=new ArrayList<String>();
	           String elist="";
	           
	           while ( iter.hasNext () ) 
	           {
	        	   FileItem item = (FileItem) iter.next();
	        	   if (item.isFormField()) {
	        		   String name = item.getFieldName();
	        		   String value = item.getString();
	        		   if(name.equalsIgnoreCase("filename"))
	        			   flist.add(value);
	        		   else if(name.equalsIgnoreCase("category"))
	        			   category_list.add(value);
	        		   else
	        			   tlist.add(value);
	        	   } else {
	        		   //System.out.println(item.getName());
	                   //System.out.println(item.getSize());
	                   //System.out.println(item.getContentType());
	                   elist=item.getContentType();
	                   inputStream = item.getInputStream();
	        	    }
	        	   if(inputStream!=null)
	        	   {
	        		   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        		   Date dt=new Date();
	        		   String query="insert into document_details_backup(docname,username,date_,tag2,contents,extension,userpsno,catname) values(?,?,?,'{"+tlist.get(0)+"}',?,?,?,?)";
	        		   PreparedStatement prepS = conn.prepareStatement(query);
        			   conn.setAutoCommit(false);
	        		   for(int i=0;i<flist.size();i++)
	        		   {
	        			   prepS.setString(1, flist.get(i).trim());
	        			   prepS.setString(2, userName);
	        			   java.sql.Date datezz=java.sql.Date.valueOf(sdf.format(dt));
	        			   prepS.setDate(3, datezz);
	        			   //prepS.setString(4, tlist.get(i));
	        			   
	        			   //prepS.setArray(4, conn.createArrayOf("VARCHAR", tlist.toArray()));
	        			   prepS.setBinaryStream(4, inputStream);
	        			   prepS.setString(5, elist);
	        			   prepS.setInt(6, Integer.parseInt(userPsno.trim()));
	        			   prepS.setString(7, category_list.get(i));
	        			   prepS.addBatch();
	        		   }
	        		   prepS.executeBatch();
	        		   conn.commit();
	                   prepS.close();
	                   flist=new ArrayList<String>();
	                   tlist=new ArrayList<String>();
	                   inputStream=null;
	        	   }
	           }
	           //statz.close();
               conn.close();
               
		    }
             catch (Exception e) 
              {
            	 System.out.println("Error in Upload "+e.toString());
            	 out.print(e.toString().substring(60));
            	 System.err.print("Error in Upload "+e.toString());
                 //e.printStackTrace();
              }
		  }
     
     private String getHours(String f1,String f2)
     {
         double hxx=0.0;
         try{
         SimpleDateFormat format = new SimpleDateFormat("HH:mm");
         Date date1 = format.parse(f1);
         Date date2 = format.parse(f2);
         long difference = date2.getTime() - date1.getTime();
         hxx=(difference*0.001)/3600;
         }
         catch(Exception e)
         {
             System.err.print(e.toString());
         }
         return hxx+"";   
     }
     
}
