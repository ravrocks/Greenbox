<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="assets/css/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="assets/css/material.min.css"/>

<!--  Javascripts-->

<script type="text/javascript" src="assets/js/datatables.min.js"></script>

</head>
<body>
<table id="example" class="mdl-data-table" style="width:100%">
        <thead>
            <tr>
                <th>Document name</th>
                <th>Uploaded By</th>
                <th>Uploaded Date</th>
                <th>Tags</th>
                <th>*</th>
            </tr>
        </thead>
        <tbody>
           
        </tbody>
    </table>
</body>
<script>
function oyeoye(incoming) {
	
	$.ajax({
        type: "POST",
        url: "DownloadFile",
        data: {downloadthis:JSON.stringify(incoming)},
        success : function(data, textStatus, request){
        	var fheader=request.getResponseHeader('Content-disposition');
        	var fname=fheader.substring(20,fheader.length);
        	var ctype=request.getResponseHeader('Ctype');
        	//console.log(data);
        	var binaryData = [];
        	binaryData.push(data);
        	
            a = document.createElement('a');
            a.href = window.URL.createObjectURL(new Blob(binaryData, {type: ctype}));
            // Give filename you wish to download
            a.download = fname;
            a.style.display = 'none';
            document.body.appendChild(a);
            a.click();
            
        	}
		});
	}
	
$(document).ready(function(){
	
	
	var responseText='[{"uploaddate":"2020-04-27","documentname":"favicon","username":"test2","tags":"{sample,second,image}"},{"uploaddate":"2020-04-27","documentname":"Testing","username":"test2","tags":"{sample,first}"}]';
	var result = JSON.parse(responseText);
	//Initializing Datatable
	$('#example').DataTable( {
    	fixedHeader: true,
    	columnDefs: [
        	{
            targets: [ 0, 1, 2 ],
            className: 'mdl-data-table__cell--non-numeric'
        	},
        	{
        		targets: -1,
        		data: null,
        		defaultContent: '<button>Download</button>',
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
         	{ "data"  : ""
         	}
         ]
    
 		});
 	
 	//Getting back row data
		$('#example tbody').on( 'click', 'button', function () {
	    var table = $('#example').DataTable();
    	var data = table.row( $(this).parents('tr') ).data();
    	//alert( data["username"] +"'s salary is: "+ data["tags"] );
    	var sendFor=[];
        sendFor.push(data);
		oyeoye(sendFor);
		alert("yes");
		});
 	
 	
});
</script>
</html>