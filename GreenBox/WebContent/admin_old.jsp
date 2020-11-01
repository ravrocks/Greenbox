
<%@page import="com.internal.getConnection"%>
<%@ page import="java.sql.*" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
    String userName = null;
    Cookie[] cookies = request.getCookies();
    if(cookies !=null){
    for(Cookie cookie : cookies){
	if(cookie.getName().equals("c_name")) userName = cookie.getValue();
    //if(cookie.getName().equals("c_psno")) userPsno = cookie.getValue();
    }
    }
    //System.out.println(userName);
    if(!userName.equals("admin")) response.sendRedirect("logout.jsp");
    
%>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>demo</title>
    <script src="assets/js/jquery.min.js"></script>
    
    
    <!-- Css files  -->
    <link rel="stylesheet" href="assets/css/bootstrap-4.2.1.min.css">
    <link rel="stylesheet" href="assets/fonts/simple-line-icons.min.css">
    <link rel="stylesheet" href="assets/css/Animated-numbers-section.css">
    <link rel="stylesheet" href="assets/css/styles.css">
    
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Cookie">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
    <link rel="stylesheet" href="assets/css/dh-navbar-inverse.css">
    
    <script src="assets/js/bootstrap-4.2.1.min.js"></script>
</head>

<body>
    
    <section class="wrapper-numbers">
        <div class="container-fluid">
            <div class="row countup">
               <div class="col-md-12 col-sm-6 sticky-top align-self-center align-top" style="margin-top: -60px;padding:0px">
                    <nav class="navbar navbar-expand-md navigation-clean " style="-webkit-border-radius: 0;-moz-border-radius: 0;border-radius: 0;">
                    <img class="img-fluid" style="height:auto" src="assets/images/swclogo.png">
                    <button class="navbar-toggler" data-toggle="collapse" data-target="#navcol-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navcol-1" >
                    <ul class="nav navbar-nav ml-auto" style="float:right;">
                        <li class="nav-item" role="presentation"><a uk-scroll="offset:100">Welcome&nbsp;<%out.println(userName);%></a></li>
                        <li class="nav-item" role="presentation"><a href="logout.jsp" uk-scroll="offset:50">Logout</a></li>
                    </ul>
                    </div>
                    </nav>
                </div>
            </div>
        </div>
    </section>
    <div>
             <br>
    </div>
    <section>
    	 <div class="card" style="margin-top: -40px">
                <div class="card-header">
                    <ul class="nav nav-tabs card-header-tabs" role="tablist">
                        <li class="nav-item"><a class="nav-link active" href="#item-1-1" id="item-1-1-tab" data-toggle="tab" role="tab" aria-controls="item-1-1" aria-selected="true">User Authentication</a></li>
                        </ul>
                </div>
         <style>a:hover{font-weight:normal;}</style>
            <div class="card-body" >
                <div id="nav-tabContent" class="tab-content">
                <div style="color: #000;text-align: left" id="item-1-1" class="tab-pane active" role="tabpanel" aria-labelledby="item-1-1-tab">
                    <jsp:include page="authentication.jsp" />
                </div>
                </div>
            </div>
     </div>
    </section>
    
</body>

</html>