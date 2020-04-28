<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<%
    session.invalidate();
    Cookie[] cookies = request.getCookies();
    if(cookies !=null)
    {
    for(Cookie cookie : cookies){
	cookie.setMaxAge(0);
        cookie.setValue(null);
        response.addCookie(cookie);
        }
    }
         
         response.sendRedirect("home.jsp");
%>

</body>
</html>

