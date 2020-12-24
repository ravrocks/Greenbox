var success=true;
var fileTypes = ['pdf', 'docx','doc', 'rtf', 'jpg', 'jpeg', 'png', 'txt','csv','xls','xlsx','pptx','xltx','ppt','ppsx','xltx','xlsm','zip'];  //acceptable file types
function readURL(input) {
    if (input.files && input.files[0]) {
        var extension = input.files[0].name.split('.').pop().toLowerCase();  //file extension from input file
        //console.log('extension is',extension);
            isSuccess = fileTypes.indexOf(extension) > -1;  //is extension in acceptable types

        if (isSuccess) { //yes
            var reader = new FileReader();
            reader.onload = function (e) {
                if (extension == 'pdf'){
                	$(input).closest('.fileUpload').find(".icon").attr('src','assets/images/179483.svg');
                }
                else if ((extension == 'docx')||(extension == 'doc')){
                	$(input).closest('.fileUpload').find(".icon").attr('src','assets/images/281760.svg');
                }
                else if (extension == 'rtf'){
                	$(input).closest('.fileUpload').find(".icon").attr('src','assets/images/136539.svg');
                }
                else if (extension == 'png'){ 
                	$(input).closest('.fileUpload').find(".icon").attr('src','assets/images/136523.svg'); 
                }
                else if (extension == 'jpg' || extension == 'jpeg'){
                	$(input).closest('.fileUpload').find(".icon").attr('src','assets/images/136524.svg');
                }
                else if (extension == 'zip'){ 
                	$(input).closest('.fileUpload').find(".icon").attr('src','assets/images/zip.svg'); 
                }
              else if (extension == 'txt'){
                	$(input).closest('.fileUpload').find(".icon").attr('src','assets/images/136538.svg');
                }
              else if ((extension == 'xls') || (extension == 'xlsx') || (extension == 'csv') || (extension=='xltx') || (extension=='xlsm') || (extension=='xltm')|| (extension=='xlam')|| (extension=='xlsb'))
            	  {
            	  $(input).closest('.fileUpload').find(".icon").attr('src','assets/images/excel.svg');
            	  }
              else if ((extension == 'ppt') || (extension == 'pptx') || (extension= 'ppsx')){
              	$(input).closest('.fileUpload').find(".icon").attr('src','assets/images/ppt.svg');
              }
                else {
                	//console.log('here=>'+$(input).closest('.uploadDoc').length);
                	$(input).closest('.fileUpload').find(".icon").attr('src','assets/images/unknown.svg');
                	$(input).closest('.uploadDoc').find(".docErr").slideUp('slow');
                	$('#fsubmit').attr("disabled", true);
                	$('#fsubmit').click(false);
                }
            }
            $('#fsubmit').attr("disabled", false);
            reader.readAsDataURL(input.files[0]);
        }
        else {
        	console.log('here=>'+$(input).closest('.uploadDoc').find(".docErr").length);
        	$(input).closest('.fileUpload').find(".icon").attr('src','assets/images/unknown.svg');
            $(input).closest('.uploadDoc').find(".docErr").fadeIn();
            setTimeout(function() {
				   	$('.docErr').fadeOut('slow');
					}, 5000);
            $('#fsubmit').attr("disabled", true);
            $('#fsubmit').click(function(){return false;});
        }
       
    }
}
$(document).ready(function(){
   
   $(document).on('change','.up', function(){
   	var id = $(this).attr('id'); /* gets the filepath and filename from the input */
	   var profilePicValue = $(this).val();
	   var fileNameStart = profilePicValue.lastIndexOf('\\'); /* finds the end of the filepath */
	   profilePicValue = profilePicValue.substr(fileNameStart + 1).substring(0,20); /* isolates the filename */
	   //var profilePicLabelText = $(".upl"); /* finds the label text */
	   if (profilePicValue != '') {
	   	//console.log($(this).closest('.fileUpload').find('.upl').length);
	      $(this).closest('.fileUpload').find('.upl').html(profilePicValue); /* changes the label text */
	   }
   });

   $(".btn-new").on('click',function(){
	   
	     //$('#uploader').children().last().clone().appendTo("#uploader");
	   
	   	 var $div= $('#uploader').children().last();
	   	 var num = parseInt( $div.prop("id").match(/\d+/g), 10 ) +1;
	   	 var $klon = $div.clone().prop('id', 'klon'+num );
	   
	   
	     //console.log($klon.find('#tags').find('span'));
	     $klon.find('#tags').find('span').remove();
	     $klon.find('#tags').find('.bootstrap-tagsinput').remove();
	     $klon.find('#tags').find('input').tagsinput('');
	     $klon.find('#up').find('input:text').val('');
	     $klon.find('.upl').html('Upload document');
	     $klon.find('.fileUpload').find(".icon").attr('src','assets/images/136549.svg');
	     //$klon.find('input:text').val('');
	     $klon.find("#namez").val('');
	     $div.after( $klon );
	     $('#fsubmit').attr("disabled", false);
        
   });
    
   $(document).on("click", "a.btn-check" , function() {
     if($(".uploadDoc").length>1){
        $(this).closest(".uploadDoc").remove();
      }else{
        alert("You have to upload at least one document.");
      } 
   });
});