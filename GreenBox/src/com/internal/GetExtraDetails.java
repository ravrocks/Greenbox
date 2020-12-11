package com.internal;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class GetExtraDetails
 */
@WebServlet("/GetExtraDetails")
public class GetExtraDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetExtraDetails() {
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
		String requestz = request.getParameter("get_this").toLowerCase().trim().concat("_content");
		//System.out.println(requestz);
		PrintWriter out=response.getWriter();
	    Connection connection = null;
	    ResultSet rs = null;
	    String documentname= "\0";
		String activity_name="\0";
		String activity_type="\0";
		String tender_clause="\0";
		//String id="\0";
		JSONArray jarray_tosend = new JSONArray();
	    try {
	    	connection=new getConnection().getConnection();
	    	
	    	if(requestz.equalsIgnoreCase("dc it_content"))
	    		requestz="dc_it_content";
	    	else if(requestz.equalsIgnoreCase("dc non it_content"))
	    		requestz="dc_non_it_content";
	    	
	    	if((requestz.contains("cyber"))||(requestz.contains("dc_it")))
	    	{
	    		String sqlq ="select * from "+requestz;
		        PreparedStatement ps = connection.prepareStatement(sqlq);
		        rs=ps.executeQuery();
		        
	        	
	        	Set<String> mapset1=new HashSet<String>();
	        	Set<String> mapset2=new HashSet<String>();
	        	Set<String> mapset3=new HashSet<String>();
	        	Set<String> mapset4=new HashSet<String>();
	        	
		        while(rs.next())
		        {	
		        	documentname= rs.getString(4);
					activity_name=rs.getString(3);
					tender_clause=rs.getString(5);
					activity_type=rs.getString(2);
					
					mapset1.add(documentname);
					mapset2.add(activity_name);
					mapset3.add(tender_clause);
					mapset4.add(activity_type);
					
		        }
		        
		        List<String> mapzz1 = new ArrayList<String>(mapset1);
	        	List<String> mapzz2 = new ArrayList<String>(mapset2);
	        	List<String> mapzz3 = new ArrayList<String>(mapset3);
	        	List<String> mapzz4 = new ArrayList<String>(mapset4);
		        
		    	jarray_tosend.add(0,new Gson().toJson(mapzz1));
		    	jarray_tosend.add(1,new Gson().toJson(mapzz2));
		    	jarray_tosend.add(2,new Gson().toJson(mapzz3));
		    	jarray_tosend.add(3,new Gson().toJson(mapzz4));
	    	}
	    	else
	    	{
	    	String sqlq ="select * from "+requestz;
	        PreparedStatement ps = connection.prepareStatement(sqlq);
	        rs=ps.executeQuery();
	        
	        Set<String> mapset1=new HashSet<String>();
        	Set<String> mapset2=new HashSet<String>();
        	Set<String> mapset3=new HashSet<String>();
        	
	        
	        while(rs.next())
	        {	
	        	documentname= rs.getString(3);
				activity_name=rs.getString(2);
				tender_clause=rs.getString(4);
				mapset1.add(documentname);
				mapset2.add(activity_name);
				mapset3.add(tender_clause);
				
	        }
	        
	        List<String> mapzz1 = new ArrayList<String>(mapset1);
        	List<String> mapzz2 = new ArrayList<String>(mapset2);
        	List<String> mapzz3 = new ArrayList<String>(mapset3);
	        
	    	jarray_tosend.add(0,new Gson().toJson(mapzz1));
	    	jarray_tosend.add(1,new Gson().toJson(mapzz2));
	    	jarray_tosend.add(2,new Gson().toJson(mapzz3));
	    	
	       }
	    	rs.close();
			connection.close();
	    	out.write(jarray_tosend.toString());
	    }
	    catch(Exception e)
	    {
	    	System.out.println("Error in GettingExtra: "+e.toString());
	    }
		
		
	}

}
