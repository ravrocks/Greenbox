<%@page import="com.internal.SendMail"%>
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
        if(cookie.getName().equals("show_month")) showMonth = cookie.getValue();        
    }
    }
    if(userName == null) response.sendRedirect("home.jsp");

Connection connection = null;
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
String sql ="update userdata set validity=? where psno=?";
PreparedStatement ps = connection.prepareStatement(sql); 
ps.setInt(1, 1);
ps.setInt(2, Integer.parseInt(psn));
ps.executeUpdate();
ps.close();
PreparedStatement ps2 = connection.prepareStatement("select email from userdata where psno=?"); 
ps2.setInt(1, Integer.parseInt(psn));
ResultSet rsz=ps2.executeQuery();
rsz.next();
String to_send=rsz.getString(1);
rsz.close();
ps2.close();
connection.close();
String content_prepare="<div><p>Your account access has been successfully approved.</p><p> Kindly login to the app- 'https://14.141.163.130:7879/GreenBox' ";
SendMail ssdz=new SendMail(to_send,content_prepare,"GreenBox User Approval");


} 
catch (Exception e) {
e.printStackTrace();
}
%>
</body>
</html>