var onGetDirectPay = function(response_type){
	var requestdata = "", url = $("#mock").attr("action");
	var i=0;
	$("input:hidden").each(function(){
		var key = $(this).attr("name");
		var value = $(this).val();
		var entry = key + "=" + value;
		if(i==0){
			requestdata +=entry
		}else{
			requestdata += "&" + entry
		}
		i++;
	});
	requestdata += "&trade_status="+ $("#tradeStatus").val();
	requestdata += "&error_code="+ $("#errorCode").val();
	requestdata += "&action="+ response_type;
	
	
	$.ajax({
        type: "GET",
        url: url,
        data: requestdata,
        success: function(result) {
            alert(result);
        }   
    });

}

var onClickNotify=function() {
		$("#notifyBtn").click(function() {
			onGetDirectPay($("#notifyBtn").val());	
		});	
};
var onSubmit = function(){
	$("#mock").submit(function() {
		return confirm("Confirm return!");
	});
};

var onClickError=function(){
	$("#notifyErrorBtn").click(function() {
		onGetDirectPay($("#notifyErrorBtn").val());	
	});	
	
};

$(function() {
	onClickNotify();
	onSubmit();
	onClickError();
});