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
<link href="assets/css/select2.min.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="assets/css/styles2.css">


<!--  Javascripts -->

<script src="assets/js/jquery-3.3.1.min.js"></script>
<script src="assets/js/select2.min.js"></script>
<script src="assets/js/bootstrap-4.2.1.min.js"></script>
<script src="assets/js/sweetalert2.all.min.js"></script>
<script src="assets/js/script3.js"></script>

<!--  IE support for sweetalert -->
<script src="https://cdn.jsdelivr.net/npm/promise-polyfill@7.1.0/dist/promise.min.js"></script>

<link rel="icon" type="image/png" href="assets/images/faviconn.png">
</head>

<body>

<div class="container">
<div class="row it">
<div class="col-sm-offset-1 col-sm-10" id="one">
<p>
Please upload documents only in 'pdf', 'xls', 'xlsx', 'csv', 'ppt', 'pptx', 'docx', 'rtf', 'jpg', 'jpeg', 'png' , 'zip' and 'txt' format.
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
    <div style="margin-bottom:10px;magin-top:20px">
    <select class="js-example-basic-single form-control"  name="Category">
       <option value="NTA" selected="selected" disabled>Select Category</option>
  		<option value="OSS">OSS</option>
  		<option value="NOC">NOC</option>
  		<option value="DC IT">DC IT</option>
  		<option value="DC Non IT">DC Non IT</option>
  		<option value="Cybersecurity">Cybersecurity</option>
  		<option value="Integration">Integration</option>
  		<option value="Tender_Prebid">Tender_Prebid</option>
		<option value="Tender_Postbid">Tender_Postbid</option>
		<option value="Solution_Document">Solution_Document</option>
	</select>
	</div>
	<div style="margin-bottom:10px;margin-top:10px">
	<select class="js-example-basic-single-ac_type form-control"  name="Type of Activity"></select>
	</div>
	<div style="margin-bottom:10px;margin-top:10px">
	<select class="js-example-basic-single-ac form-control" style="margin-bottom:10px" name="Activity"></select>
	</div>
	<div style="margin-bottom:10px;margin-top:10px">
	<select class="js-example-basic-single-docname form-control" style="margin-bottom:10px" name="Document"></select>
    </div>
    
    <div id='tags' style="margin-top:5px">
    	    <link rel='stylesheet' href='assets/css/bootstrap-3.3.6.min.css'>
			<link rel='stylesheet' type="text/css" href="assets/css/bootstap-tagsinput.css">
			<input type="text" value="" maxlength="80" data-role="tagsinput" class="form-control" placeholder="Enter tags separted by comma(,)" required/>
			<!-- partial -->
			
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
	
	var $eventSelect = $(".js-example-basic-single");
	console.log($eventSelect);
	$eventSelect.select2();
    $eventSelect.on("select2:select", function (e) { 
    	var data = e.params.data;
       // console.log(data.text);
        $.ajax({
            type: "POST",
            url: "GetExtraDetails",
            data: {get_this:data.text},
            success : function(responseText) {
                if(responseText=="")
                showError(responseText);
                else
                   {
                	showRest(JSON.parse(responseText)[0],JSON.parse(responseText)[1],JSON.parse(responseText)[2],JSON.parse(responseText)[3]);
                   }
                }
            }); 
    });
	
	$("#fsubmit").click(function (){
		var joshObj=new FormData();
		var succ_array=[];
		$("#uploader .uploadDoc").each(function(){
			var current_row=$(this);
			//var filename=current_row.find("#namez").val();
			var filename=current_row.find(".js-example-basic-single-docname").select2('val');
			var tags=current_row.find("#tags").find("input.form-control").val();
			var catname=current_row.find(".js-example-basic-single").select2('val');
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
			joshObj.append("category",catname);
			joshObj.append("content",current_row.find("#up")[0].files[0]);
			var mefile=current_row.find("#up")[0].files[0].name;
			var fileTypes = ['pdf', 'docx','doc', 'rtf', 'jpg', 'jpeg', 'png', 'txt','csv','xls','xlsx','pptx','xltx','ppt','ppsx','xltx','xlsm','zip'];  //acceptable file types
			filezz= mefile.split('.');
			var extension = filezz[filezz.length-1].toLowerCase();
	        successd= fileTypes.indexOf(extension) > -1;
	        succ_array.push(successd);
		});
		
		let checker = arr => arr.every(v => v === true);
		
		if(!successd)
			{
			showError("Invalid File Type");
			return;
			}
		
		if(checker(succ_array))
		{
			console.log("I'm in!");
			swal.fire({
       			 title: 'Uploading...',
       			 html: '<h4 id="status">',
        		imageUrl:'assets/images/bean_eater.gif',
        		showConfirmButton: false,
        		allowOutsideClick: false,
    			});
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
	function showRest(get_docname,get_activity,get_tender_clause,get_activity_type)
	{

		$(".js-example-basic-single-ac_type").empty();
		$(".js-example-basic-single-docname").empty();
		$(".js-example-basic-single-ac").empty();
		
		$(".js-example-basic-single-ac").select2({data:get_activity});
		$(".js-example-basic-single-docname").select2({data:get_docname});
		$(".js-example-basic-single-ac_type").select2({data:get_activity_type});
		
	}
});
</script>
</html>