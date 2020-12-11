<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<html lang="en">
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
%>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<title>GreenBox</title>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-2.2.4.min.js" integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44="
crossorigin="anonymous"></script>

<!-- Bootstrap files (jQuery first, then Popper.js, then Bootstrap JS) -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/zf-6.4.3/jq-3.3.1/dt-1.10.20/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/material-design-lite/1.1.0/material.min.css"/>
<link rel="stylesheet" type="text/css" href="assets/css/bootstrap-4.2.1.min.css">

<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"/>
<link rel="stylesheet" href="assets/css/sticky-dark-top-nav-with-dropdown.css"/>
<link rel="stylesheet" href="assets/css/styles.css"/>
<link rel="stylesheet" href="assets/css/text-box.css"/>
<link rel="stylesheet" href="assets/css/foopicker.css"/>
<link rel="icon" type="image/png" href="assets/images/faviconn.png">

<style type="text/css">
/* ============ desktop view ============ */
@media all and (min-width: 992px) {
	.navbar .nav-item .dropdown-menu{ display: none; }
	.navbar .nav-item:hover .nav-link{  background-color: #e1e1e1;  }
	.navbar .nav-item:hover .dropdown-menu{ display: block; }
	.navbar .nav-item .dropdown-menu{ margin-top:1px; }
}	
/* ============ desktop view .end// ============ */

#dt_Table_wrapper{
width:100%;
text-align: center;
}
.row{
margin-left:0px;
text-align: center;
}
label{
 font-size: small;
 font-family: Tahoma, Geneva, sans-serif;
 font-weight: 700;
}
highlight {
    border: 2px solid red;
}

</style>
<script src="assets/js/foopicker.js"></script>
<script type="text/javascript" src="assets/js/datatables.min.js"></script>

<script>
function basicPopup(url) {
	popupWindow = window.open(url,'popUpWindow','height=600,width=1000,left=50,top=50,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no,directories=no, status=yes');
		}
		
function base64ToArrayBuffer(base64) {
    var binaryString = window.atob(base64);
    var binaryLen = binaryString.length;
    var bytes = new Uint8Array(binaryLen);
    for (var i = 0; i < binaryLen; i++) {
        var ascii = binaryString.charCodeAt(i);
        bytes[i] = ascii;
    }
    return bytes;
}	

function startView(incoming) {
	$.ajax({
        type: "POST",
        url: "ViewFile",
        data: {viewthis:JSON.stringify(incoming)},
        success : function(data, textStatus, request){
        	
        	var fname=request.getResponseHeader('Cdoc');
        	var ctype=request.getResponseHeader('Ctype');
        	var exten=request.getResponseHeader('Cextend');
        	console.log(ctype);
        	
        	var bytes_decoded = base64ToArrayBuffer(data);
        	var binaryData = [];
        	binaryData.push(bytes_decoded);
        	
            var file = new Blob(binaryData, {type: ctype});
            var fileURL = URL.createObjectURL(file);
            window.open(fileURL);
        	}
		});
	}
	
function startDown(incoming) {
	$.ajax({
        type: "POST",
        url: "DownloadFile",
        data: {downloadthis:JSON.stringify(incoming)},
        success : function(data, textStatus, request){
        	
        	var fname=request.getResponseHeader('Cdoc');
        	var ctype=request.getResponseHeader('Ctype');
        	var exten=request.getResponseHeader('Cextend');
        	console.log(ctype);
        	
        	var bytes_decoded = base64ToArrayBuffer(data);
        	var binaryData = [];
        	binaryData.push(bytes_decoded);
        	
            a = document.createElement('a');
            a.href = window.URL.createObjectURL(new Blob(binaryData, {type: ctype}));
            a.download = fname+'.'+exten;
            a.style.display = 'none';
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
        	}
		});
	}
	
$(document).on('click', '.some-class', function(){ 
    var $btn=$(this);
    var $tr=$btn.closest('tr');
    var dataTableRow=$('#dt_Table').DataTable().row($tr[0]); // getting dt row
    var rowData=dataTableRow.data();
    console.log(rowData.msg);
});

$(document).ready(function() {
	
	$.ajax({
        type: "POST",
        url: "Fetch_All",
        data: {},
        success : function(responseText) {
            if(responseText!="")
                {
            	//console.log(responseText);
            	var result = JSON.parse(responseText);
            	
            	$('#dt_Table').DataTable({
                    fixedHeader: true,
                    columnDefs: [
                    	{
                        targets: [ 0, 1, 2, 3 ],
                        className: 'mdl-data-table__cell--non-numeric'
                    	},
                    	{
                    		targets: -1,
                    		data: null,
                    		defaultContent: '<button type="button" class="press press-brown" id="vme"><span class="glyphicon glyphicon-eye-open"></span>&nbsp;View</button>&nbsp;&nbsp;&nbsp;<button type="button" class="press press-teal press-round press-ghost" id="dme"><span class="glyphicon glyphicon-download-alt"></span>&nbsp;Download</button>'
                    	},
                    	{
                    		orderable:false,
                    		targets: [4,5]
                    	}
                 	  ],
                     "sDom": '<"top">rt<"bottom"lp><"clear">',
                     "data" : result,
                     "columns" : [
                         { "data" : "documentname" },
                         { "data" : "username" },
                         { "data" : "uploaddate" },
                         { "data" : "app_date" },
                         { "data" : "tags" },
                         {"data"  : ""}
                        ]
                    
                   });
                }
        }
	});
	$('#dt_Table tbody').on( 'click', '#vme', function () {
	    var table = $('#dt_Table').DataTable();
    	var data = table.row( $(this).parents('tr') ).data();
    	var sendForV=[];
        sendForV.push(data);
        //console.log("i'm viewing");
		startView(sendForV);
		});
	$('#dt_Table tbody').on( 'click', '#dme', function () {
	    var table = $('#dt_Table').DataTable();
    	var data = table.row( $(this).parents('tr') ).data();
    	var sendForD=[];
        sendForD.push(data);
		startDown(sendForD);
		});
    
});
</script>


</head>
<body>

<!-- ========================= SECTION CONTENT ========================= -->

<div class="container-fluid">

<nav class="navbar navbar-expand-md navigation-clean">
  <img class="img-fluid" style="height:auto;width:180px" src="assets/images/swc_logo_new.jpg">
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#main_nav" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>
  <div class="collapse navbar-collapse" id="main_nav">
	<ul class="navbar-nav ml-auto">
		<li class="nav-item active">
							<a href="dashboard.jsp" class="btn btn-md" style="margin-right:5vmin">
                            <span class="glyphicon glyphicon-home"></span></a>
                    </li>
		<li class="nav-item" role="presentation" style="font-size:18px;margin-right:3vmin">
                	   <a href="uploadFiles.jsp" onclick="basicPopup(this.href);return false" class="btn btn-success btn-lg openBtn" style="margin-right:10px">
          				    <span class="glyphicon glyphicon-open"></span> Upload Files 
        				</a>
                	</li>
		<li class="nav-item dropdown">
			<a class="nav-link  dropdown-toggle" href="#" data-toggle="dropdown"> <span class="glyphicon glyphicon-user"></span> <%out.println(userName);%></a>
		    <ul class="dropdown-menu dropdown-menu-right">
		      <li><a href="#" onClick="loadme()" class="btn btn-md">
                            <span class="glyphicon glyphicon-duplicate"></span> My Uploads
                        </a></li>
              <li><a href="#" onClick="loadmetoo()" class="btn btn-md">
                            <span class="glyphicon glyphicon-share"></span> Pending Uploads
                        </a></li>
			  <li><a href="logout.jsp" class="btn btn-md" style="color: red">
                            <span class="glyphicon glyphicon-log-out"></span> Log out
                        </a></li>
		    </ul>
		</li>
	</ul>
  </div> <!-- navbar-collapse.// -->
</nav>

<section class="section-content py-5" id="sectionList">
			<div class="row col-md-12 col-lg-12 col-sm-12" >
								<form id="search-form" action="" role="form" style="display: block;" class="row col-md-12 col-lg-12 col-sm-12">
								   	<div class="roww" style="text-align:left">
									 	<div class="col-sm-2">
								   			<label style="margin-top:-3px">Document Name</label>
								   			<input type="text" name="anydocs" id="anydocs" tabindex="1" class="form-control" placeholder="Enter document name">
								   		</div>
								   		<div class="col-sm-1">
								   			<label style="margin-top:0pxx">Category</label>
								   			<input type="text" name="cats" id="cats" tabindex="2" class="form-control" placeholder="Enter type">
								   		</div>
									 	<div class="col-sm-2">
								   			<label style="margin-top:-3px">Document Tags</label>
								   			<input type="text" name="anywords" id="anywords" tabindex="3" class="form-control" placeholder="Enter tags separated by comma">
								   		</div>
								   		<div class="col-sm-2">
								   			<label>Username</label>
								   			<input type="text" name="username" id="username" tabindex="4" placeholder="Enter username" class="form-control">
								   		</div>
								   		<div class="col-sm-2">
								   			<label>From Date</label>
								   			<input type="text" id="sdate" name="dates" id="dates" tabindex="5" class="form-control" placeholder="From Date">
								   			
								   		</div>
								   		<div class="col-sm-2">
								   			<label>End Date</label>
								   			<input type="text" id="tdate" name="dates" id="dates" tabindex="6" class="form-control" placeholder="End Date">
								   			
								   		</div>
								   		<div class="col-sm-1">
								   			<a id="filterme" href="#" class="btn btn-info" style="margin-top:4vmin;">
          									<span class="glyphicon glyphicon-filter"></span> Filter 
        									</a>
								   		</div>
									</div>
								</form>
            </div>
            <div class="row col-md-12 col-lg-12 col-sm-12" style="margin-top:10px">
				<table id="dt_Table" class="mdl-data-table" style="width:100%">
        			<thead>
            			<tr>
                			<th>Document name</th>
                			<th>Uploaded By</th>
                			<th>Uploaded Date</th>
                			<th>Approved Date</th>
                			<th>Document Tags</th>
                			<th></th>
            			</tr>
       			    </thead>
        			<tbody>
           
        			</tbody>
    			</table>
		   </div>
</section>

</div><!-- container //  -->

</body>
<script>

function loadme()
{
	document.getElementById("sectionList").innerHTML = "";
    jQuery("#sectionList").load('showuploads.jsp');	
}

function loadmetoo()
{
	document.getElementById("sectionList").innerHTML = "";
    jQuery("#sectionList").load('penuploads.jsp');	
}

function validateInputs(docsearch,catsearch,tfir,usec,sdda,edda)
{
	var valiid=true;
	
	if(catsearch=="")
		{
		   alert("Category name is mandatory field.");
		   var capElem = $("#cats");
		   //capElem.parent().css({"color": "red", "border": "2px solid red"});
		   capElem.before("<div id='validtags' style='color:red;margin-bottom: 0px;'>Required Field</div>");
		    setTimeout(
		        function() { 
		        	//capElem.parent().css({"color": "", "border": "0px"});
		        	$('#validtags').remove(); 
		        	},
		        3000);
		    valiid=false;
		}
	
	var letters_only = '^[a-zA-Z_]+( [a-zA-Z_]+)*$';
	if(!(usec.match(letters_only)))
		{
			if((valiid==true)&&usec.length>1)
				{
				  alert("Usernames can only be text-values.");
				   var capElem = $("#username");
				   capElem.parent().css({"color": "red", "border": "2px solid red"});
				   capElem.before("<div id='validtags' style='color:red;margin-bottom: 0px;'>Text-only values allowed.</div>");
				    setTimeout(
				        function() { 
				        	capElem.parent().css({"color": "", "border": "0px"});
				        	$('#validtags').remove(); 
				        	},
				        4000);
				    valiid=false;
				}
		}
	
	return valiid;
}
	
	
$(document).ready(function(){
	
	var foopicker = new FooPicker({
	    id: 'sdate',
	    dateFormat: 'dd/MM/yyyy'
	});
	var foopicker2 = new FooPicker({
	      id: 'tdate',
	      dateFormat: 'dd/MM/yyyy'
	    });
	
	$("#filterme").click(function (){
		var docname=$("#anydocs").val();
		var catname=$("#cats").val();
		var tname=$("#anywords").val();
		var uname=$("#username").val();
		var sdate=$("#sdate").val();
		var edate=$("#tdate").val();
		var result=validateInputs(docname,catname,tname,uname,sdate,edate);
		if(result)
			{
			itemx = {};var sendObj=[];
			itemx["docsearch"]= docname;
			itemx["category"]=catname;
        	itemx ["allwords"] = tname;
        	itemx ["user"] = uname;
        	itemx ["sdate"]=sdate;
        	itemx["edate"]=edate;
        	sendObj.push(itemx);
        	$.ajax({
            	type: "POST",
            	url: "GetFilterData",
            	data: {filters:JSON.stringify(sendObj)},
            	success : function(responseText) {
                if(responseText!="")
                    {
                	//console.log(responseText);
                	var result = JSON.parse(responseText);
                	
                	$("#dt_Table").DataTable().destroy();
                	$('#dt_Table').DataTable( {
                        fixedHeader: true,
                    	columnDefs: [
                        	{
                            targets: [ 0, 1, 2, 3 ],
                            className: 'mdl-data-table__cell--non-numeric'
                        	},
                        	{
                        		targets: -1,
                        		data: null,
                        		defaultContent: '<button type="button" class="btn btn-default btn-sm" id="vme"><span class="glyphicon glyphicon-eye-open">&nbsp;View</span></button>&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-default btn-sm" id="dme"><span class="glyphicon glyphicon-download-alt">&nbsp;Download</span></button>'
                        	},
                        	{
                        		orderable:false,
                        		targets: [4,5]
                        	}
                     	  ],
                         "sDom": '<"top">rt<"bottom"lp><"clear">',
                         "data" : result,
                         "columns" : [
                             { "data" : "documentname" },
                             { "data" : "username" },
                             { "data" : "uploaddate" },
                             { "data" : "app_date" },
                             { "data" : "tags" },
                             {"data"  : "" }
                             ]
                        
                     });
                    }
                }
              });
			}
	  });
	}
);
</script>
</html>