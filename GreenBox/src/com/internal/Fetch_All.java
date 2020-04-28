package com.internal;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class Fetch_All
 */
@WebServlet("/Fetch_All")
public class Fetch_All extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Fetch_All() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();  
		  String requestz = request.getParameter("filters");
		  
		    Connection connection = null;
		    Statement statement = null;
		    ResultSet rs = null;
		    String documentname= "\0";
			String username="\0";
			String subdate="\0";
			String tags="\0";
			String id="\0";
			JSONArray jarray_tosend = new JSONArray();
			int arry=0;
		    try {
		    	connection=new getConnection().getConnection();
		    	String sqlq ="select docname, username, date_, tag2 from document_details";
		        PreparedStatement ps = connection.prepareStatement(sqlq);
		        rs=ps.executeQuery();
		        while(rs.next())
		        {
		        	Map<String, String> mapzz = new HashMap<String, String>();
		    		JSONObject json_tosend = new JSONObject();
		        	documentname= rs.getString(1);
					username=rs.getString(2);
					subdate=rs.getString(3);
					tags=rs.getString(4);
					String[] str1=tags.split(",");
					mapzz.put("documentname",documentname);
					mapzz.put("username",username);
					mapzz.put("uploaddate",subdate);
					
					json_tosend.accumulateAll(mapzz);
					
					List<String> list_tags = new ArrayList<String>();
			    	for(int j=0;j<str1.length;j++){
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
		    	System.out.println("Error in Fetch: "+e.toString());
		    }
	}

}
