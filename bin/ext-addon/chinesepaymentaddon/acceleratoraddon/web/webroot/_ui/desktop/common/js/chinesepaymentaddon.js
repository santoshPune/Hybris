$(function() {
	if ($('#wrapper').height() <= $(document).height()) {
		$('.mask').css('height', $(document).height());
		var top = ($(document).height() - $('.payPop').height()) / 2;
		$('.payPop').css('top', top);
	} else {
		$('.mask').css('height', $('#wrapper').height());
		var top = ($('#wrapper').height() - $('.payPop').height()) / 2;
		$('.payPop').css('top', top);
	}
	var payRightNowUrl = "${payRightNowUrl}";
	var orderCode = "${orderData.code}";
	$('#payRightNowButton').click(function() {
		$('.mask,.payPop').fadeIn(300);
	});

	countdown(5);
    
});

var countdown = function(timeout) {
    var showbox = $("#countdown");
    if(showbox.length > 0) {
        $(showbox).html(timeout);
        if (timeout == 0) {
            window.opener = null;
            window.close();
        }
        else {
        	timeout--;
            setTimeout("countdown(" + timeout + ")", 1000);
        }
    }
}