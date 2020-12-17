<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
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

<!-- Bootstrap files (jQuery first, then Popper.js, then Bootstrap JS) -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/zf-6.4.3/jq-3.3.1/dt-1.10.20/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/material-design-lite/1.1.0/material.min.css"/>
<link rel="stylesheet" type="text/css" href="assets/css/bootstrap-4.2.1.min.css">

<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"/>
<link rel="stylesheet" href="assets/css/text-box.css"/>
<link rel="stylesheet" href="assets/css/foopicker.css"/>
<link rel="stylesheet" href="assets/css/sweetalert2.min.css">
<link rel="stylesheet" href="assets/css/sweetalert2.css">
<link rel="icon" type="image/png" href="assets/images/faviconn.png">

<style type="text/css">
#dt_Table_s_wrapper{
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
<script src="assets/js/moment.js"></script>
<script src="assets/js/sweetalert2.all.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/promise-polyfill@8/dist/polyfill.js"></script>

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
        url: "ViewOtherFile",
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
        url: "DownloadOtherFile",
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
	
function startUpld(incoming) {
	$.ajax({
        type: "POST",
        url: "Confirmm",
        data: {confirmthis:JSON.stringify(incoming)},
        success : function(responseText){
        	if(responseText!="")
        		showError(responseText);
        	else
        		showInfo();
        	}
		});
	}

function removeUpld(incoming) {
	$.ajax({
        type: "POST",
        url: "Removee",
        data: {confirmthis:JSON.stringify(incoming)},
        success : function(responseText){
        	if(responseText!="")
        		showError(responseText);
        	else
        		showInfo();
        	}
		});
	}

$(document).on('click', '.some-class', function(){ 
    var $btn=$(this);
    var $tr=$btn.closest('tr');
    var dataTableRow=$('#dt_Table_s').DataTable().row($tr[0]); // getting dt row
    var rowData=dataTableRow.data();
    console.log(rowData.msg);
});

</script>

</head>
	<div class="row col-md-12 col-lg-12 col-sm-12" style="margin-top:10px">
				<table id="dt_Table_s" class="mdl-data-table" style="width:100%">
        			<thead>
            			<tr>
                			<th>Document name</th>
                			<th>Uploaded Date</th>
                			<th>Document Tags</th>
                			<th>Status</th>
                			<th>Uploaded By</th>
                			<th>Upsno</th>
                			<th></th>
            			</tr>
       			    </thead>
        			<tbody>
           
        			</tbody>
    			</table>
		   </div>
<script>
function showError(txxt)
{
    Swal.fire({
    title: 'Data Submission Error',
    text: txxt,
    type: 'error',
    confirmButtonText: 'Ok'
    });
}
function showInfo()
{
    Swal.fire({
    title: 'Success',
    text: "Record updated. Page will refresh now.",
    type: 'info',
    confirmButtonText: 'Ok'
    }).then(function(){
    	document.getElementById("sectionList").innerHTML = "";
        jQuery("#sectionList").load('penuploads.jsp');
    	}
    );
}
$(document).ready(function(){
	$.ajax({
    	type: "POST",
    	url: "ViewPending",
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
                    targets: [ 0, 1, 2, 3, 4, 5, 6 ],
                    className: 'mdl-data-table__cell--non-numeric'
                	},
                	{
                		targets: -1,
                		data: null,
                		defaultContent: '<button class="btn btn-outline-secondary" id="vme"><span class="glyphicon glyphicon-eye-open"></span>&nbsp;View</button>&nbsp;&nbsp;&nbsp;<button class="btn btn-outline-success" id="dme"><span class="glyphicon glyphicon-download-alt"></span>&nbsp;Download</button>'
                	},
                	{
                		orderable:false,
                		targets: [4,5,6]
                	}
             	  ],
                 "sDom": '<"top">rt<"bottom"lp><"clear">',
                 "data" : result,
                 "columns" : [
                     { "data" : "documentname" },
                     { "data" : "uploaddate" },
                     { "data" : "tags" },
                     { "data" : "status" },
                     { "data" : "user_upload_by" },
                     { "data" : "userpsno_upload_by" },
                     {"data"  : "" }
                     ],
                 "createdRow": function( row, data, dataIndex){
                	 //console.log(row);
                         if( data['status'] == 'Pending'  ){
                        	 //$(row).find('td:eq(3)').css('color', 'red');
                             $(row).find('td:eq(3)').replaceWith( "<button type='button' class='btn btn-info' style='margin-bottom: 1vmin;margin-left: 2vmin;margin-top: 3vmin;float: left;text-align: center;' id='pen'><span class='glyphicon glyphicon-ok-sign'>&nbsp;</span>Approve</button><button type='button' class='btn btn-danger'style='margin-left: 2vmin;margin-bottom: 1vmin;float: left;' id='penR'><span class='glyphicon glyphicon-remove-sign'>&nbsp;</span>Reject</button>" );
                        	 //$(row).css('background-color', '#F39B9B');
                         }
                         else
                        	 {
                        	$(row).find('td:eq(3)').css('color', '#9400D3');
                        	 }
                         }
                
             });
            }
    	var table = $('#dt_Table_s').DataTable();
        column = table.column(5);
        column.visible(false);
        }
      });
    
	});
			
$('#dt_Table_s tbody').on( 'click', '#vme', function () {
    var table = $('#dt_Table_s').DataTable();
	var data = table.row( $(this).parents('tr') ).data();
	var sendForV=[];
    sendForV.push(data);
    //console.log("i'm viewing");
	startView(sendForV);
	});
$('#dt_Table_s tbody').on( 'click', '#dme', function () {
    var table = $('#dt_Table_s').DataTable();
	var data = table.row( $(this).parents('tr') ).data();
	var sendForD=[];
    sendForD.push(data);
	startDown(sendForD);
	});
$('#dt_Table_s tbody').on( 'click', '#pen', function () {
    var table = $('#dt_Table_s').DataTable();
	var data = table.row( $(this).parents('tr') ).data();
	var sendForC=[];
    sendForC.push(data);
	startUpld(sendForC);
	});
$('#dt_Table_s tbody').on( 'click', '#penR', function () {
    var table = $('#dt_Table_s').DataTable();
	var data = table.row( $(this).parents('tr') ).data();
	var sendForR=[];
    sendForR.push(data);
	removeUpld(sendForR);
	});
</script>
</html>