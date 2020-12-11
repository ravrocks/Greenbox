package com.internal;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class GetFilterData
 */
@WebServlet("/GetFilterData")
public class GetFilterData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetFilterData() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		  PrintWriter out = response.getWriter();  
		  String requestz = request.getParameter("filters");
		  
		    
		    ResultSet rs = null;
		    Set set=new HashSet();
		  	String alwords = null,uname =  null,sdates = null,edates = null,docs_to_search=null,cat_search=null;
			
			JsonElement joshObj=null;
			
		try(Connection connection=new getConnection().getConnection();) {
			//Parsing loogic
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(requestz);
            JsonArray jaaray=je.getAsJsonArray();
			 for(int i=0;i<jaaray.size();i++)
             {
              joshObj= jaaray.get(i);
              org.json.JSONObject off=new org.json.JSONObject (joshObj.toString());
              alwords=off.get("allwords").toString().trim();
              uname=off.get("user").toString().trim();
              sdates=off.get("sdate").toString().trim();
              edates=off.get("edate").toString().trim();
              docs_to_search=off.get("docsearch").toString().toLowerCase().trim();
              cat_search=off.get("category").toString().trim();
              //System.out.println("allwords is-"+alwords);
              //System.out.println("user is-"+uname);
              //System.out.println("sdate is-"+sdates);
              //System.out.println("edate is-"+edates);
             }
			 if(uname.length()<1)
					uname="%";
			 if(docs_to_search.length()<1)
				 docs_to_search="";
			 
			 Date sdate = null;
			 Date edate = null;
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			 try { 
				    sdate = sdf.parse(sdates);
					sdate = new java.sql.Date(sdate.getTime());
					edate = sdf.parse(edates);
					edate = new java.sql.Date(edate.getTime());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
			    if(alwords.length()<1)
			    {
			    	JSONArray jarray_tosend = new JSONArray();
			    	String sql ="select id,docname, username, date_, appdate, tag2 from document_details where UPPER(catname) like UPPER('%"+cat_search+"%') AND LOWER(docname) like '%"+docs_to_search+"%' AND username like '%"+uname+"%'";
					PreparedStatement ps = connection.prepareStatement(sql);
					System.out.println("coming- "+cat_search);
					rs=ps.executeQuery();
					
					String username="\0",documentname= "\0",subdate="\0",appdate="\0",tags="\0",id="\0";
					int arry=0;
					
			    	while(rs.next())
						{
			    		Map<String, String> mapzz = new HashMap<String, String>();
			    		JSONObject json_tosend = new JSONObject();
						documentname= rs.getString(2);
						username=rs.getString(3);
						subdate=rs.getString(4);
						appdate=rs.getString(5);
						tags=rs.getString(6);
						id=rs.getInt(1)+"";
						
						set.add(rs.getInt(1)+"");
							
						String[] str1=tags.split(",");
						mapzz.put("documentname",documentname);
						mapzz.put("username",username);
						mapzz.put("uploaddate",subdate);
						mapzz.put("app_date",appdate);
							
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
					out.write(jarray_tosend.toString());
			    }
			    else
			    {
			    String[] str = alwords.split(",");
				int length = str.length;
				JSONArray jarray_tosend = new JSONArray();
				int arry=0;
				for(int i=0;i<length;i++)
				  {
					//System.out.println(" value of i "+i);
					String sql;
					
					if(sdate==null)
					{
						sql ="select id,docname, username, date_, appdate, tag2 from document_details where lower(tag2::text)::text[] @> ARRAY['"+str[i].toLowerCase()+"'] AND username like '%"+uname+"%' AND LOWER(docname) like '%"+docs_to_search+"%'";
						PreparedStatement ps = connection.prepareStatement(sql);
						System.out.println("coming- "+str[i].toLowerCase());
						rs=ps.executeQuery();
					}
					else{
				    sql ="select id,docname, username, date_, appdate, tag2 from document_details where lower(tag2::text)::text[] @> ARRAY['"+str[i].toLowerCase()+"'] AND username like '%"+uname+"%' AND date_>=? AND date_<=? AND LOWER(docname) like '%"+docs_to_search+"%'";
				    PreparedStatement ps = connection.prepareStatement(sql);
					ps.setString(1, uname);
					ps.setDate(2, (java.sql.Date)sdate);
					ps.setDate(3, (java.sql.Date)edate);
					rs=ps.executeQuery(); 
					}
				
					String username="\0",documentname= "\0",subdate="\0",appdate="\0",tags="\0";
					String id="\0";
					
			    	while(rs.next())
						{
			    		Map<String, String> mapzz = new HashMap<String, String>();
			    		JSONObject json_tosend = new JSONObject();
						documentname= rs.getString(2);
						username=rs.getString(3);
						subdate=rs.getString(4);
						appdate=rs.getString(5);
						tags=rs.getString(6);
						
						id=rs.getInt(1)+"";
						if(i==0)
					    	{
							set.add(rs.getInt(1)+"");
							
							String[] str1=tags.split(",");
							mapzz.put("documentname",documentname);
							mapzz.put("username",username);
							mapzz.put("uploaddate",subdate);
							mapzz.put("app_date",appdate);
							
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
						else{
							if(set.contains(Integer.toString(rs.getInt(1)))){
								//System.out.println("set: "+set);
							}
							else{
							set.add(rs.getString(1));
							//System.out.println("Set2: "+set);
							 	String[] str1=tags.split(",");
							 	mapzz.put("documentname",documentname);
								mapzz.put("username",username);
								mapzz.put("uploaddate",subdate);
								mapzz.put("app_date",appdate);
								
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
						}
					}
					
				}
				rs.close();
			  connection.close();
			  out.write(jarray_tosend.toString());
			  }
			}
			catch(Exception e)
			{
				System.out.println("Help me [GetFilterData]: "+e.toString());
			}
	}

}
