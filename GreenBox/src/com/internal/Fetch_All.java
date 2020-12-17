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
		    //String requestz = request.getParameter("filters");
		  
		    Connection connection = null;
		    ResultSet rs = null;
		    String documentname= "\0";
			String username="\0",subdate="\0",appdate="\0",tags="\0",id="\0",catprint="\0";
			JSONArray jarray_tosend = new JSONArray();
			int arry=0;
		    try {
		    	connection=new getConnection().getConnection();
		    	String sqlq ="select docname, username, date_, appdate, catname, tag2 from document_details";
		        PreparedStatement ps = connection.prepareStatement(sqlq);
		        rs=ps.executeQuery();
		        while(rs.next())
		        {
		        	Map<String, String> mapzz = new HashMap<String, String>();
		    		JSONObject json_tosend = new JSONObject();
		        	documentname= rs.getString(1);
					username=rs.getString(2);
					subdate=rs.getString(3);
					appdate=rs.getString(4);
					catprint=rs.getString(5);
					tags=rs.getString(6);
					
					String[] str1=tags.split(",");
					mapzz.put("documentname",documentname);
					mapzz.put("username",username);
					mapzz.put("uploaddate",subdate);
					mapzz.put("app_date",appdate);
					mapzz.put("category", catprint);
					
					json_tosend.accumulateAll(mapzz);
					
					List<String> list_tags = new ArrayList<String>();
			    	for(int j=0;j<str1.length;j++){
			    		if((j==0)&&(j+1==str1.length))
		    				list_tags.add(str1[j].substring(1, str1[j].length()-1));
		    			else if(j==0)
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
		    	System.out.println("Error in Fetch: "+e.toString());
		    }
	}

}
