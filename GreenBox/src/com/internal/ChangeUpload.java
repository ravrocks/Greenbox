package com.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
@MultipartConfig
/**
 * Servlet implementation class ChangeUpload
 */
@WebServlet("/ChangeUpload")
public class ChangeUpload extends HttpServlet{
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
	           String vimp_id="\0";
	           InputStream inputStream = null;
	           ArrayList<String> flist=new ArrayList<String>();
	           ArrayList<String> tlist=new ArrayList<String>();
	           String elist="";
	           
	           while ( iter.hasNext () ) 
	           {
	        	   FileItem item = (FileItem) iter.next();
	        	   if (item.isFormField()) {
	        		   String name = item.getFieldName();
	        		   String value = item.getString();
	        		   
	        		   if(name.equalsIgnoreCase("filename"))
	        			   flist.add(value);
	        		   else if(name.equalsIgnoreCase("imp_id"))
	        			   vimp_id=value;
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
	        		   System.out.println("New id is "+vimp_id);
	        		   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        		   Date dt=new Date();
	        		   String query="UPDATE document_details_backup SET docname=?,username=?,date_=?,tag2='{"+tlist.get(0)+"}',contents=?,extension=?,userpsno=? WHERE id=?";
	        		   PreparedStatement prepS = conn.prepareStatement(query);
        			   conn.setAutoCommit(false);
	        		   for(int i=0;i<flist.size();i++)
	        		   {
	        			   prepS.setString(1, flist.get(i));
	        			   prepS.setString(2, userName);
	        			   java.sql.Date datezz=java.sql.Date.valueOf(sdf.format(dt));
	        			   prepS.setDate(3, datezz);
	        			   //prepS.setString(4, tlist.get(i));
	        			   
	        			   //prepS.setArray(4, conn.createArrayOf("VARCHAR", tlist.toArray()));
	        			   prepS.setBinaryStream(4, inputStream);
	        			   prepS.setString(5, elist);
	        			   prepS.setInt(6, Integer.parseInt(userPsno.trim()));
	        			   prepS.setInt(7, Integer.parseInt(vimp_id.trim()));
	        			   prepS.addBatch();
	        		   }
	        		   prepS.executeBatch();
	        		   conn.commit();
	                   prepS.close();
	                   
	                   //////////document_Details update//////////////////////////
	                   String query2="UPDATE document_details SET docname=?,username=?,date_=?,tag2='{"+tlist.get(0)+"}',contents=?,extension=?,userpsno=? WHERE id=?";
	        		   PreparedStatement prepS2 = conn.prepareStatement(query2);
        			   conn.setAutoCommit(false);
	        		   for(int i=0;i<flist.size();i++)
	        		   {
	        			   prepS2.setString(1, flist.get(i));
	        			   prepS2.setString(2, userName);
	        			   java.sql.Date datezz=java.sql.Date.valueOf(sdf.format(dt));
	        			   prepS2.setDate(3, datezz);
	        			   //prepS.setString(4, tlist.get(i));
	        			   
	        			   //prepS.setArray(4, conn.createArrayOf("VARCHAR", tlist.toArray()));
	        			   prepS2.setBinaryStream(4, inputStream);
	        			   prepS2.setString(5, elist);
	        			   prepS2.setInt(6, Integer.parseInt(userPsno.trim()));
	        			   prepS2.setInt(7, Integer.parseInt(vimp_id.trim()));
	        			   prepS2.addBatch();
	        		   }
	        		   prepS2.executeBatch();
	        		   conn.commit();
	                   prepS2.close();
	                   
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
            	 System.out.println("Error in ChangeUpload "+e.toString());
            	 out.print(e.toString().substring(60));
            	 System.err.print("Error in ChangeUpload "+e.toString());
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
