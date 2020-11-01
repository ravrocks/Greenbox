<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>GreenBox</title>
<%
    String userName=null,userPsno=null;
    Cookie[] cookies = request.getCookies();
    if(cookies !=null){
    for(Cookie cookie : cookies){
	if(cookie.getName().equals("c_name")) userName = cookie.getValue();
    if(cookie.getName().equals("c_psno")) userPsno = cookie.getValue();
    	}
    }
    //System.out.println(userName);
    if(userName==null) response.sendRedirect("logout.jsp");
    
%>

<link rel="stylesheet" type="text/css" href="assets/css/bootstrap-4.2.1.min.css">
<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="assets/css/styles2.css">


<!--  Javascripts -->

<script src="assets/js/jquery-3.3.1.min.js"></script>
<script src="assets/js/bootstrap-4.2.1.min.js"></script>
<script src="assets/js/sweetalert2.all.min.js"></script>
<script src="assets/js/script2.js"></script>
<!--  IE support for sweetalert -->
<script src="https://cdn.jsdelivr.net/npm/promise-polyfill@7.1.0/dist/promise.min.js"></script>

<link rel="icon" type="image/png" href="assets/images/faviconn.png">
</head>

<body>

<div class="container">
<div class="row it">
<div class="col-sm-offset-1 col-sm-10" id="one">
<p>
Please upload documents only in 'pdf', 'xls', 'xlsx', 'csv', 'ppt', 'pptx', 'docx', 'rtf', 'jpg', 'jpeg', 'png' and 'txt' format.
</p><br>
<div class="row">
  <div class="col-sm-offset-4 col-sm-4 form-group">
    <h3 class="text-center">Upload Documents</h3>
  </div><!--form-group-->
</div><!--row-->
<div id="uploader">

<div class="row uploadDoc" id="klon1">
  <div class="col-sm-3">
    <div class="docErr">Please upload valid file</div><!--error-->
    <div class="fileUpload btn btn-orange">
      <img src="assets/images/136549.svg" class="icon">
      <span class="upl" id="upload">Upload document</span>
      <input type="file" class="upload up" id="up" onclick="readURL(this);" onchange="readURL(this);" />
    </div>
  </div>
  
  <div class="col-sm-8">
    <input type="text" id="namez" class="form-control" name="namez" placeholder="Title" maxlength="60">
    <div id='tags' style="margin-top:5px">
    	    <link rel='stylesheet' href='assets/css/bootstrap-3.3.6.min.css'>
			<link rel='stylesheet' type="text/css" href="assets/css/bootstap-tagsinput.css">
			<input type="text" value="" maxlength="80" data-role="tagsinput" class="form-control" placeholder="Enter tags separted by comma(,)" required/>
			<!-- partial -->
			<script src='assets/js/jquery-3.3.1.min.js'></script>
			<script src='assets/js/bootstrap-4.2.1.min.js'></script>
			<script src='https://cdn.jsdelivr.net/bootstrap.tagsinput/0.8.0/bootstrap-tagsinput.min.js'></script>
    </div>
  </div><!--col-8-->
  
  <div class="col-sm-1"><a class="btn-check"><i class="fa fa-times"></i></a></div><!-- col-1 -->
</div><!--row-->

</div><!--uploader-->
<div class="text-center">
<a class="btnn btn-new" ><i class="fa fa-plus"></i> Add new</a>
<a class="btn btn-next" id="fsubmit"><i class="fa fa-paper-plane"></i> Submit</a>
</div>
</div><!--one-->
</div><!-- row -->
</div><!-- container -->

</body>

<script>
var success=true;
$(document).ready(function(){
	$("#fsubmit").click(function (){
		var joshObj=new FormData();
		var succ_array=[];
		$("#uploader .uploadDoc").each(function(){
			var current_row=$(this);
			var filename=current_row.find("#namez").val();
			var tags=current_row.find("#tags").find("input.form-control").val();
			//console.log(current_row.find("#up"));
			var ffcontent=current_row.find("#up").val();
			if(filename.length<1 || tags.length<1 || ffcontent.length<1)
				{
				alert("Fields cannot be left blank.");
				succ_array.push(false);
				return;
				}
			joshObj.append("filename", filename);
			joshObj.append("tags",tags);
			joshObj.append("content",current_row.find("#up")[0].files[0]);
			var mefile=current_row.find("#up")[0].files[0].name;
			var fileTypes = ['pdf', 'docx','doc', 'rtf', 'jpg', 'jpeg', 'png', 'txt','csv','xls','xlsx','pptx','xltx','ppt','ppsx','xltx','xlsm'];  //acceptable file types
			filezz= mefile.split('.');
			var extension = filezz[1].toLowerCase();
	        successd= fileTypes.indexOf(extension) > -1;
	        //console.log("val of successd-",successd);
	        succ_array.push(successd);
		});
		
		let checker = arr => arr.every(v => v === true);
		
		if(checker(succ_array))
		{
			console.log("I'm in!");
		  $.ajax({
	            type: "POST",
	            url: "UploadFile",
	            enctype: 'multipart/form-data',
	            processData: false,  // Important!
	            contentType: false,
	            cache: false,
	            timeout: 600000,
	            data: joshObj,
	            success : function(responseText) {
	                if(responseText!="")
	                showError(responseText);
	                else
	                showInfo();
	                }
	            }); 
		}
		var $div= $('#uploader').children().last();
	});
	
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
		//e.preventDefault();
	    Swal.fire({
	    title: 'Success',
	    text: "Record saved.",
	    type: 'info',
	    confirmButtonText: 'Ok'
	    }).then(function(){ 
	    	window.close();
	    });
	}
});
</script>
</html>