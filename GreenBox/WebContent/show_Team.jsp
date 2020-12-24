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

<link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">

<!-- Material Design Bootstrap -->
<link href="assets/css/mdb.min.css" rel="stylesheet">

  <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
  <script type="text/javascript" src="assets/js/popper.min.js"></script>
  <!-- Bootstrap core JavaScript -->
  <script type="text/javascript" src="assets/js/bootstrap.min.js"></script>
  <!-- MDB core JavaScript -->
  <script type="text/javascript" src="assets/js/mdb.min.js"></script>

<script type="text/javascript" src="assets/js/datatables.min.js"></script>

<script>
$(document).ready(function(){
	  $("#search").on("keyup", function() {
	    var value = $(this).val().toLowerCase();
	    $("#table tr").filter(function() {
	      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
	    });
	  });
	});

//JavaScript popup window function
	function basicPopup(url) {
popupWindow = window.open(url,'popUpWindow','height=600,width=600,left=50,top=50,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no,directories=no, status=yes');
	}

//////////////////Static Validate
(function() {
		'use strict';
		window.addEventListener('load', function() {
		// Fetch all the forms we want to apply custom Bootstrap validation styles to
		var forms = document.getElementsByClassName('needs-validation');
		// Loop over them and prevent submission
		var validation = Array.prototype.filter.call(forms, function(form) {
		form.addEventListener('submit', function(event) {
		console.log(form.checkValidity());
		if (form.checkValidity() === false) {
		event.preventDefault();
		event.stopPropagation();
		}
		form.classList.add('was-validated');
		}, false);
		});
		}, false);
		})();
</script>

<style>

#dt_Table_sx td { font-size: 1.1rem; }
#dt_Table_s td { font-size: 1.1rem; }
#dt_Table_sx td { white-space: normal;}
#dt_Table_sx td { white-space: wrap;}
#dt_Table_sx_paginate .pagination { margin : 0;}
#dt_Table_s td { white-space: normal;}
#dt_Table_s td { white-space: wrap;}
#dt_Table_s_paginate .pagination { margin : 0;}

input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Firefox */
input[type=number] {
  -moz-appearance: textfield;
}
</style>

</head>

<div style="width:100%">
<div class="col-md-12 col-lg-12 col-sm-12" style="margin-top:10px">

<div class="md-form col-md-12 col-lg-12 col-sm-12">
<form class="form-inline needs-validation" method="post" action="ChangeTeam" style="margin-bottom:20px" novalidate>
  <div>
      <div class="md-form mt-0 input-group" >
        <input type="number" id="inputMDEx1" name="inputMDEx1" class="form-control" style="font-size:1.6rem" required>
  		<label for="inputMDEx1" style="font-size:1.3rem">TeamMember PSno</label>
  		<div class="invalid-feedback">
        Numeric Characters only!
      	</div>
      </div>
      <div class="md-form mt-0 ml-3 input-group" style="">
        <input type="number" id="inputMDEx2" name="inputMDEx2" class="form-control" style="font-size:1.6rem" required> 
  		<label for="inputMDEx2" style="font-size:1.3rem">TeamLeader PSno</label>
  		<div class="invalid-feedback">
        Numeric Characters only!
      	</div>
  		</div>
  	  <input type="submit" class="btn btn-indigo ml-3" value="Submit"/>
  </div>
</form>

</div>

<div class="col-md-5 col-lg-5 col-sm-5">
<label style="font-size:1.4rem">List of Unlinked Users</label>
<table id="dt_Table_sx" class="mdl-data-table" style="width:100%;">
<thead>
<tr>
<th scope="col" class="col-md-1 col-lg-1 col-sm-2">No.</th>
<th scope="col" class="col-md-1 col-lg-1 col-sm-2">User Name</th>
<th scope="col" class="col-md-1 col-lg-1 col-sm-2">User Psno</th>
<th scope="col" class="col-md-1 col-lg-1 col-sm-2">Email ID</th>
<th scope="col" class="col-md-1 col-lg-1 col-sm-2">Domain</th>
</tr>
</thead>
</table>
</div>

<div class="col-md-1 col-lg-1 col-sm-1">
</div>

<div class="col-md-6 col-lg-6 col-sm-6" style="">
<label style="font-size:1.4rem">Users attached to TeamLeader</label>
<table id="dt_Table_s" class="mdl-data-table" style="width:100%;">
<thead>
<tr>
<th scope="col" class="col-md-1 col-lg-1 col-sm-1">No.</th>
<th scope="col" class="col-md-1 col-lg-1 col-sm-2">Member Name</th>
<th scope="col" class="col-md-2 col-lg-2 col-sm-1">Member Psno</th>
<th scope="col" class="col-md-1 col-lg-1 col-sm-1">TeamLeader Name</th>
<th scope="col" class="col-md-1 col-lg-1 col-sm-1">TeamLeader Psno</th>

</tr>
</thead>
</table>
</div>

</div>
</div>
<script>
$(document).ready(function(){
	$.ajax({
    	type: "POST",
    	url: "ShowTeams",
    	data: {},
    	success : function(responseText) {
        if(responseText!="")
            {
        	var result = JSON.parse(responseText);
        	$("#dt_Table_s").DataTable().destroy();
        	$('#dt_Table_s').DataTable( {
                fixedHeader: true,
            	columnDefs: [
                	{
                    targets: [ 0, 1, 2, 3, 4 ],
                    className: 'mdl-data-table__cell--non-numeric'
                	},
                	{
                		orderable:true,
                		targets: [1,2,3,4]
                	}
             	  ],
                 "sDom": '<"top">rft<"bottom">lp<"clear">',
                 "data" : result,
                 "columns" : [
                	 { "data" : "countme" },
                	 { "data" : "memname" },
                     { "data" : "mempsno" },
                     { "data" : "supname" },
                     { "data" : "suppsno" }
                     
                     ]                
             });
            }
    	var table = $('#dt_Table_s').DataTable();
        }
      });
	
	$.ajax({
    	type: "POST",
    	url: "STAllUser",
    	data: {},
    	success : function(responseText) {
        if(responseText!="")
            {
        	var result = JSON.parse(responseText);
        	$("#dt_Table_sx").DataTable().destroy();
        	$('#dt_Table_sx').DataTable( {
                fixedHeader: true,
            	columnDefs: [
                	{
                    targets: [ 0, 1, 2,3,4],
                    className: 'mdl-data-table__cell--non-numeric'
                	},
                	{
                		orderable:true,
                		targets: [2,3,4]
                	}
             	  ],
                 "sDom": '<"top">rft<"bottom">lp<"clear">',
                 "data" : result,
                 "columns" : [
                	 { "data" : "countme" },
                     { "data" : "uname" },
                     { "data" : "upsno" },
                     { "data" : "uemail" },
                     { "data" : "udomain" }
                     ]                
             });
            }
    	var table = $('#dt_Table_sx').DataTable();
        }
      });
	});

</script>

</html>