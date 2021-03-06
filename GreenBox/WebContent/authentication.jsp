<%@page import="com.internal.getConnection"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%
    String userName = null,userPsno=null, showMonth=null;
    Cookie[] cookies = request.getCookies();
    if(cookies !=null){
    for(Cookie cookie : cookies)
    {
	if(cookie.getName().equals("c_name")) userName = cookie.getValue();
        if(cookie.getName().equals("c_psno")) userPsno = cookie.getValue();       
    	}
    }
    if(userName == null) response.sendRedirect("home.jsp");

Connection connection = null;
Statement statement = null;
ResultSet rs = null;
ResultSet rs1 = null;
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>GreenBox</title>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>

<script>
//JavaScript popup window function
	function basicPopup(url) {
popupWindow = window.open(url,'popUpWindow','height=600,width=600,left=50,top=50,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no,directories=no, status=yes');
	}

</script>
<style>
#tablezz td{
font-size:1.3rem;
}
#tablezz th{
font-size:1.3rem;
}
</style>
</head>

<div class="container" >
<div>
<table class="table table-hover table-bordered" id="tablezz">
<thead>
<tr>
<th scope="col">Name</th>
<th scope="col">PSno</th>
<th scope="col">Email</th>
<th scope="col">Designation</th>
<th scope="col">Domain</th>
<th scope="col">Approve</th>
</tr>
</thead>
<%
try{
connection = new getConnection().getConnection();
statement=connection.createStatement();
String sql ="select name, psno, email, designation, domain from userdata where validity=?;";
PreparedStatement ps = connection.prepareStatement(sql); 
ps.setInt(1, 0);
rs=ps.executeQuery();
while(rs.next()){
%>
<tbody>
<tr>
<td><%=rs.getString("name") %></td>
<%String psn = rs.getString("psno"); %>
<td><%out.print(psn);%></td>
<td><%=rs.getString("email") %></td>
<td><%=rs.getString("designation") %></td>
<td><%=rs.getString("domain") %></td>
<td>
<a onclick="updation(<%=psn%>)" href=#><button type="button" class="btn btn-default btn-sm" style="font-size:1.1rem">
          <span class="glyphicon glyphicon-ok-sign"></span>&nbsp;Accept
        </button></a><div id="up"></div>
<a onclick="deletion(<%=psn%>)" href=#><button type="button" class="btn btn-default btn-sm" style="font-size:1.1rem">
          <span class="glyphicon glyphicon-remove-sign"></span>&nbsp;Decline
        </button></a><div id="del"></div>
</td>
</tr>
</tbody>
<%
}
connection.close();
} 
catch (Exception e) {
e.printStackTrace();
}
%>
</table>
</div>
</div>
<script>
function updation(str){
	var xhttp;
	  if (str == "") {
	    document.getElementById("up").innerHTML = "";
	    return;
	  }
	  xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	    document.getElementById("up").innerHTML = this.responseText;
	    }
	  };
	  var xhttp;  
	  xhttp.open("POST", "update.jsp?psno="+str, true);
	  xhttp.send();
	  window.location.reload(true);
}
function deletion(str){
	var xhttp;
	  if (str == "") {
	    document.getElementById("del").innerHTML = "";
	    return;
	  }
	  xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	    document.getElementById("del").innerHTML = this.responseText;
	    }
	  };
	 var xhttp;  
	  xhttp.open("POST", "deleteentry.jsp?psno="+str, true);
	  xhttp.send();
	  window.location.reload(true);
}

</script>

</html>