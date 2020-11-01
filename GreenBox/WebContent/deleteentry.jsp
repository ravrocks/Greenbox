<%@page import="com.internal.getConnection"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%
    String userName = null,userPsno=null, showMonth=null;
    Cookie[] cookies = request.getCookies();
    if(cookies !=null){
    for(Cookie cookie : cookies){
	if(cookie.getName().equals("c_name")) userName = cookie.getValue();
        if(cookie.getName().equals("c_psno")) userPsno = cookie.getValue();  
    }
    }
    if(userName == null) response.sendRedirect("home.jsp");

Connection connection = null;
Statement statement = null;
ResultSet rs = null;
ResultSet rs1 = null;
String psn = request.getParameter("psno");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Time Sheet</title>
<link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
</head>
<body>
<%
try{
connection = new getConnection().getConnection();
statement=connection.createStatement();
String sql ="delete from userdata where psno=? and validity=0";
PreparedStatement ps = connection.prepareStatement(sql); 
ps.setInt(1, Integer.parseInt(psn));
ps.executeUpdate();
connection.close();
} 
catch (Exception e) {
e.printStackTrace();
}
%>
</body>
</html>