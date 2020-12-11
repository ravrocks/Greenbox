
<%@page import="com.internal.getConnection"%>
<%@ page import="java.sql.*" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
    String userName = null;
    String showMonth=null;
    Cookie[] cookies = request.getCookies();
    if(cookies !=null){
    for(Cookie cookie : cookies){
	if(cookie.getName().equals("c_name")) userName = cookie.getValue();
        if(cookie.getName().equals("show_month")) showMonth = cookie.getValue();
    }
    }
    if(!userName.equals("admin")) response.sendRedirect("logout.jsp");
    
%>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GreenBox</title>
    <script src="assets/js/jquery-3.3.1.min.js"></script>
    
    
    <!-- Css files  -->
    <!-- Bootstrap files (jQuery first, then Popper.js, then Bootstrap JS) -->

	<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/material-design-lite/1.1.0/material.min.css"/>
	<link rel="stylesheet" type="text/css" href="assets/css/bootstrap-4.2.1.min.css">
    <link rel="stylesheet" href="assets/fonts/simple-line-icons.min.css">
    
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Cookie">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
    <link rel="stylesheet" href="assets/css/dh-navbar-inverse.css">
    <link rel="icon" type="image/png" href="assets/images/faviconn.png">
    
</head>

<body>
    
    
        <div class="container-fluid">
            <div class="row">
               <div class="col-md-12 col-sm-6 sticky-top align-self-center align-top" style="margin-top: 0px;padding:0px">
                    <nav class="navbar navbar-expand-md navigation-clean">
  					<img class="img-fluid" style="height:auto;width:180px" src="assets/images/swc_logo_new.jpg"">
  					<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#main_nav" aria-expanded="false" aria-label="Toggle navigation">
    				<span class="navbar-toggler-icon"></span>
  					</button>
                    <div class="collapse navbar-collapse" id="main_nav">
                    <ul class="nav navbar-nav ml-auto" style="float:right;">
                        <li class="nav-item" role="presentation"><a uk-scroll="offset:100">Welcome&nbsp;<%out.println(userName);%></a></li>
                        <li class="nav-item" role="presentation"><a href="logout.jsp" uk-scroll="offset:50">Logout</a></li>
                    </ul>
                    </div>
                    </nav>
                </div>
            </div>
            <div class="card" style="margin-top: 10px">
                <div class="card-header">
                    <ul class="nav nav-tabs" role="tablist" style="font-size:1.4rem">
                        <li class="nav-item"><a class="nav-link" href="#item-1-1" id="item-1-1-tab" data-toggle="tab" role="tab" aria-controls="item-1-1" aria-selected="true">User Authentication</a></li>
                        <li class="nav-item"><a class="nav-link" href="#item-1-2" id="item-1-2-tab" data-toggle="tab" role="tab" aria-controls="item-1-2" aria-selected="true">Modify Supervisor List</a></li>
                        <li class="nav-item"><a class="nav-link" href="#item-1-3" id="item-1-3-tab" data-toggle="tab" role="tab" aria-controls="item-1-3" aria-selected="false">Show/Modify Team</a></li>                   	
                    </ul>
                </div>
         
            <div class="card-body" >
                <div id="nav-tabContent" class="tab-content">
                <div style="color: #000;text-align: left;font-size:1.2rem" id="item-1-1" class="tab-pane active" role="tabpanel" aria-labelledby="item-1-1-tab">
                    <jsp:include page="authentication.jsp" />
                </div>
                <div style="color: #000;text-align: left;font-size:1.2rem" id="item-1-2" class="tab-pane" role="tabpanel" aria-labelledby="item-1-2-tab">
                    <jsp:include page="modif_Superv.jsp" />
                </div>
                <div style="color: #000;text-align: left;font-size:1.2rem" id="item-1-3" class="tab-pane" role="tabpanel" aria-labelledby="item-1-3-tab">
                    <jsp:include page="show_Team.jsp" />
                </div>
                </div>
            </div>
     </div>
        </div>

    	 
    
</body>

</html>