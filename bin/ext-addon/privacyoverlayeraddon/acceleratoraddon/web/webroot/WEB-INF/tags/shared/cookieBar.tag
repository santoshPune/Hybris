<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



<script type="text/javascript">
	$(document).ready(function(){
		$.cookieBar({
			message:"${content}",
			acceptText:"${acceptText}",
			acceptFunction:function(cookieValue){

				var acceptUrl = "${ACCEPT_ENDPOINT_HTTPS_URL}"; // Use HTTPS Endpoint by default

				if(location.protocol != 'https:')	// if the current page does not use https protocol, use the normal HTTP endpoint:
				{
					acceptUrl = "${ACCEPT_ENDPOINT_URL}";
				}

				if(acceptUrl!='') {
					$.get(acceptUrl);
				}
			},
			declineButton:${declineButton},
			declineText:"${declineText}"
		});
	});

	var i,cookieValue='',aCookie,aCookies=document.cookie.split('; ');
	for (i=0;i<aCookies.length;i++){
		aCookie = aCookies[i].split('=');
		if(aCookie[0]=='cb-enabled'){
			cookieValue = aCookie[1];
		}
	}

</script>

