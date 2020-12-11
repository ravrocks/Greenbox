
<link href="assets/css/select2.min.css" rel="stylesheet" />
<script src="assets/js/jquery-3.3.1.min.js"></script>
<script src="assets/js/select2.min.js"></script>
<script>
$(document).ready(function() {
	
	var $eventSelect = $(".js-example-basic-single");
	$eventSelect.select2();
    $eventSelect.on("select2:select", function (e) { 
    	var data = e.params.data;
        console.log(data.text);
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
});

function showRest(get_docname,get_activity,get_tender_clause,get_activity_type)
{

	$(".js-example-basic-single-ac_type").empty();
	$(".js-example-basic-single-docname").empty();
	$(".js-example-basic-single-ac").empty();
	
	$(".js-example-basic-single-ac").select2({data:get_activity});
	$(".js-example-basic-single-docname").select2({data:get_docname});
	$(".js-example-basic-single-ac_type").select2({data:get_activity_type});
	
}
</script>
<html>
<select class="js-example-basic-single" style="width:20%" name="Category">
  <option value="OSS">OSS</option>
  <option value="NOC">NOC</option>
  <option value="DCI">DC IT</option>
  <option value="DCN">DC Non IT</option>
  <option value="CYB">Cybersecurity</option>
  <option value="INT">Integration</option>
</select>
<select class="js-example-basic-single-ac_type" style="width:20%" name="Type of Activity"></select>
<select class="js-example-basic-single-ac" style="width:20%" name="Activity"></select>
<select class="js-example-basic-single-docname" style="width:20%" name="Document"></select>

</html>