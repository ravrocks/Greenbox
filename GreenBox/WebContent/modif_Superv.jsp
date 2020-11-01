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

<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/zf-6.4.3/jq-3.3.1/dt-1.10.20/datatables.min.css"/>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
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

#dt_Table_sss td { font-size: 1.1rem; }
#dt_Table_ss td { font-size: 1.1rem; }
#dt_Table_sss td { white-space: normal;}
#dt_Table_ss td { white-space: wrap;}
#dt_Table_ss_paginate .pagination { margin : 0;}

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
<form class="col-md-6 col-lg-6 col-sm-6 needs-validation" method="post" action="AddSupervisor" style="margin-bottom:20px" novalidate>
  <div class="row">
    <div>
      <div class="md-form mt-0">
        <input id="inputMDEx3" name="inputMDEx3" type="number" class="form-control" style="font-size:1.6rem" required>
  		<label for="inputMDEx2">Supervisor PSno</label>
  		<div class="invalid-feedback">
        Numeric Characters only!
      	</div>
  	</div>
  	<div class="col">
  		<input type="submit" class="btn btn-indigo" value="Submit"/>
      </div>
    </div>
  </div>
</form>

<form method="post" action="RemoveSupervisor" style="margin-bottom:20px" class="col-md-6 col-lg-6 col-sm-6 needs-validation" novalidate>
  <div class="row">
    <div>
      <div class="md-form mt-0">
        <input id="inputMDEx7" name="inputMDEx7" class="form-control" type="number" style="font-size:1.6rem" required>
  		<label for="inputMDEx7">Supervisor PSno [to be removed]</label>
  		<div class="invalid-feedback">
        Numeric Characters only!
      	</div>
  	</div>
  	<div class="col">
  		<input type="submit" class="btn btn-danger" value="Remove"/>
      </div>
    </div>
  </div>
</form>

</div>


<div class="col-md-6 col-lg-6 col-sm-6" style="">
<table id="dt_Table_sss" class="mdl-data-table" style="width:100%">
<thead>
<tr>
<th scope="col" class="col-md-1 col-lg-1 col-sm-1">No.</th>
<th scope="col" class="col-md-1 col-lg-1 col-sm-1">User Name</th>
<th scope="col" class="col-md-1 col-lg-1 col-sm-1">User Psno</th>
<th scope="col" class="col-md-2 col-lg-2 col-sm-2">Email ID</th>
<th scope="col" class="col-md-1 col-lg-1 col-sm-1">Domain</th>
</tr>
</thead>
</table>
</div>


<div class="col-md-1 col-lg-1 col-sm-1">
</div>

<div class="col-md-5 col-lg-5 col-sm-5" style="">
<table id="dt_Table_ss" class="mdl-data-table" style="width:100%">
<thead>
<tr>
<th scope="col" class="col-md-1 col-lg-1 col-sm-1">No.</th>
<th scope="col" class="col-md-2 col-lg-2 col-sm-2">Supervisor Name</th>
<th scope="col" class="col-md-2 col-lg-2 col-sm-2">Supervisor Psno</th>
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
    	url: "ShowSuper",
    	data: {},
    	success : function(responseText) {
        if(responseText!="")
            {
        	var result = JSON.parse(responseText);
        	$("#dt_Table_ss").DataTable().destroy();
        	$('#dt_Table_ss').DataTable( {
                fixedHeader: true,
            	columnDefs: [
                	{
                    targets: [ 0, 1, 2],
                    className: 'mdl-data-table__cell--non-numeric'
                	},
                	{
                		orderable:true,
                		targets: [1,2]
                	}
             	  ],
                 "sDom": '<"top">rt<"bottom">lp<"clear">',
                 "data" : result,
                 "columns" : [
                	 { "data" : "countme" },
                     { "data" : "supname" },
                     { "data" : "suppsno" }
                     ]                
             });
            }
    	var table = $('#dt_Table_ss').DataTable();
        }
      });
	$.ajax({
    	type: "POST",
    	url: "SSAllUser",
    	data: {},
    	success : function(responseText) {
        if(responseText!="")
            {
        	var result = JSON.parse(responseText);
        	$("#dt_Table_sss").DataTable().destroy();
        	$('#dt_Table_sss').DataTable( {
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
                 "sDom": '<"top">rtf<"bottom">lp<"clear">',
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
    	var table = $('#dt_Table_sss').DataTable();
        }
      });	
	});

</script>

</html>