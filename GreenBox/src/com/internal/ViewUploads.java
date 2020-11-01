package com.internal;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * Servlet implementation class ViewUploads
 */
@WebServlet("/ViewUploads")
public class ViewUploads extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewUploads() {
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
        if(userName==null || userPsno==null) {
        	System.out.println("Security breach!");
        	return;
        }
	    ResultSet rs = null;
	    String documentname= "\0";
		String statuss="\0";
		String subdate="\0";
		String tags="\0";
		JSONArray jarray_tosend = new JSONArray();
		int arry=0;
	    try(Connection connection=new getConnection().getConnection();) {
	    	String sqlq ="select docname, date_, tag2, status from document_details_backup where username=?";
	        PreparedStatement ps = connection.prepareStatement(sqlq);
	        ps.setString(1, userName);
	        rs=ps.executeQuery();
	        while(rs.next())
	        {
	        	Map<String, String> mapzz = new HashMap<String, String>();
	    		JSONObject json_tosend = new JSONObject();
	        	documentname= rs.getString(1);
				tags=rs.getString(3);
				subdate=rs.getString(2);
				statuss=rs.getString(4);
				String[] str1=tags.split(",");
				mapzz.put("documentname",documentname);
				mapzz.put("uploaddate",subdate);
				mapzz.put("status",statuss);
				
				json_tosend.accumulateAll(mapzz);
				
				List<String> list_tags = new ArrayList<String>();
		    	for(int j=0;j<str1.length;j++){
		    			if(j==0)
		    				list_tags.add(str1[j].substring(1, str1[j].length()));
		    			else if(j+1==str1.length)
		    				list_tags.add(str1[j].substring(0, str1[j].length()-1));
		    			else
		    				list_tags.add(str1[j]);
		    		}
		    	json_tosend.accumulate("tags", list_tags);
		    	jarray_tosend.add(arry,json_tosend);
		    	arry++;
	        }
	        rs.close();
			connection.close();
			//System.out.println(jarray_tosend.toString());
			out.write(jarray_tosend.toString());
	    }
	    catch(Exception e)
	    {
	    	System.out.println("Error in ViewUploads: "+e.toString());
	    }
	}

}
