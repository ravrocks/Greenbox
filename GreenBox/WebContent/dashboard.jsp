<html>
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
%>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/zf-6.4.3/jq-3.3.1/dt-1.10.20/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/material-design-lite/1.1.0/material.min.css"/>
<link rel="stylesheet" type="text/css" href="assets/css/bootstrap-4.2.1.min.css">

<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"/>

<link rel="stylesheet" href="assets/css/sticky-dark-top-nav-with-dropdown.css"/>

<link rel="stylesheet" href="assets/css/text-box.css"/>
<link rel="stylesheet" href="assets/css/foopicker.css"/>
<link rel="icon" type="image/png" href="assets/images/faviconn.png">

<style>
#dt_Table_wrapper{
width:100%;
text-align: center;
}
.row{
margin-left:0px;
text-align: center;
}
label{
 font-size: 14px;
 font-family: "Times New Roman", Times, serif;
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
                        targets: [ 0, 1, 2 ],
                        className: 'mdl-data-table__cell--non-numeric'
                    	},
                    	{
                    		targets: -1,
                    		data: null,
                    		defaultContent: '<span class="glyphicon glyphicon-eye-open"></span>&nbsp;<button id="vme">View</button>&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-download-alt"></span>&nbsp;<button id="dme">Download</button>',
                    	},
                    	{
                    		orderable:false,
                    		targets: [3,4]
                    	}
                 	  ],
                     "sDom": '<"top">rt<"bottom"lp><"clear">',
                     "data" : result,
                     "columns" : [
                         { "data" : "documentname" },
                         { "data" : "username" },
                         { "data" : "uploaddate" },
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
        console.log("i'm viewing");
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


<body class="container-fluid">
<div class="row col-md-12 col-lg-12 col-sm-12">

<div class="col-md-12 col-sm-12 sticky-top align-self-center align-top">
        <nav class="navbar navbar-expand-md navigation-clean" style="margin-bottom: 5px">
		<img class="img-fluid" style="height:auto" src="assets/images/lnt_Swc_logo.jpg">
		<button class="navbar-toggler" data-toggle="collapse" data-target="#navcol-1">
		<span class="sr-only">Toggle navigation</span>
          <span class="navbar-toggler-icon"><i class="fa fa-navicon" style="float: right"></i></span>
		</button>
            <div class="collapse navbar-collapse" id="navcol-1" >
                <ul class="nav navbar-nav ml-auto" style="float:right;">
                	<li class="nav-item" role="presentation" style="font-size:18px">
                	   <a href="uploadFiles.jsp" onclick="basicPopup(this.href);return false" class="btn btn-success btn-lg openBtn" style="margin-right:10px">
          				    <span class="glyphicon glyphicon-open"></span> Upload Files 
        				</a>
        				
        				
                	</li>
                    <li class="nav-item" role="presentation" style="font-size:18px">
                        <a href="#" class="btn btn-lg">
                            <span class="glyphicon glyphicon-user"></span> <%out.println(userName);%>
                        </a>
                    </li>
                    <li class="nav-item" role="presentation" style="font-size:18px">
                        <a href="logout.jsp" class="btn btn-md">
                            <span class="glyphicon glyphicon-log-out"></span> Log out
                        </a>
                    </li>
                </ul>
            </div>
        
        </nav>
    </div>
    	<div class="row col-md-12 col-lg-12 col-sm-12" >
								<form id="search-form" action="" role="form" style="display: block;" class="row col-md-12 col-lg-12 col-sm-12">
								   	<div class="roww" style="text-align:left">
									 	<div class="col-sm-3">
								   			<label>Filter by Captions</label>
								   			<input type="text" name="anywords" id="anywords" tabindex="1" class="form-control" placeholder="Enter tags separated by comma">
								   		</div>
								   		<div class="col-sm-2">
								   			<label>Filter by Username</label>
								   			<input type="text" name="username" id="username" tabindex="2" class="form-control">
								   		</div>
								   		<div class="col-sm-2">
								   			<label>From Date</label>
								   			<input type="text" id="sdate" name="dates" id="dates" tabindex="3" class="form-control" placeholder="From Date">
								   			
								   		</div>
								   		<div class="col-sm-2">
								   			<label>End Date</label>
								   			<input type="text" id="tdate" name="dates" id="dates" tabindex="4" class="form-control" placeholder="End Date">
								   			
								   		</div>
								   		<div class="col-sm-3">
								   			<a id="filterme" href="#" class="btn btn-info" style="margin-top:4vmin;float:right">
          									<span class="glyphicon glyphicon-filter"></span> Filter 
        									</a>
								   		</div>
									</div>
									
								</form>
            </div>
</div>
<div class="row col-md-12 col-lg-12 col-sm-12">
<table id="dt_Table" class="mdl-data-table" style="width:100%">
        <thead>
            <tr>
                <th>Document name</th>
                <th>Uploaded By</th>
                <th>Uploaded Date</th>
                <th>Document Tags</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
           
        </tbody>
    </table>
</div>
</body>

<script>
function validateInputs(tfir,usec,sdda,edda)
{
	var valiid=true;
	if(tfir=="")
		{
		   alert("Caption names is mandatory field.");
		   var capElem = $("#anywords");
		   //capElem.parent().css({"color": "red", "border": "2px solid red"});
		   capElem.before("<div id='validtags' style='color:red;margin-bottom: 0px;'>Required Field</div>");
		    setTimeout(
		        function() { 
		        	//capElem.parent().css({"color": "", "border": "0px"});
		        	$('#validtags').remove(); 
		        	},
		        4000);
		    valiid=false;
		}
	
	var letters_only = '^[a-zA-Z_]+( [a-zA-Z_]+)*$';
	if(!(usec.match(letters_only)))
		{
			if((valiid==true)&&usec.length>1)
				{
				  alert("Usernames can only be text-values.");
				   var capElem = $("#username");
				   //capElem.parent().css({"color": "red", "border": "2px solid red"});
				   capElem.before("<div id='validtags' style='color:red;margin-bottom: 0px;'>Text-only values allowed.</div>");
				    setTimeout(
				        function() { 
				        	//capElem.parent().css({"color": "", "border": "0px"});
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
		var tname=$("#anywords").val();
		var uname=$("#username").val();
		var sdate=$("#sdate").val();
		var edate=$("#tdate").val();
		var result=validateInputs(tname,uname,sdate,edate);
		if(result)
			{
			itemx = {};var sendObj=[];
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
                            targets: [ 0, 1, 2 ],
                            className: 'mdl-data-table__cell--non-numeric'
                        	},
                        	{
                        		targets: -1,
                        		data: null,
                        		defaultContent: '<span class="glyphicon glyphicon-eye-open"></span>&nbsp;<button id="vme">View</button>&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-download-alt"></span>&nbsp;<button id="dme">Download</button>'
                        	},
                        	{
                        		orderable:false,
                        		targets: [3,4]
                        	}
                     	  ],
                         "sDom": '<"top">rt<"bottom"lp><"clear">',
                         "data" : result,
                         "columns" : [
                             { "data" : "documentname" },
                             { "data" : "username" },
                             { "data" : "uploaddate" },
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