<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/b2bpunchoutaddon/responsive/template" %>
<%@ taglib prefix="addoncart" tagdir="/WEB-INF/tags/addons/b2bpunchoutaddon/responsive/cart"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>

<template:page pageTitle="${pageTitle}">
	<jsp:attribute name="pageScripts">
	<script  type="text/javascript">
		/*<![CDATA[*/
		$(document).ready(function() {
			$('#procurementForm').submit();
		});
		/*]]>*/
		</script>
	</jsp:attribute>
	<jsp:body>
	<form id="procurementForm" id="procurementForm" method="post" action="${browseFormPostUrl}">
		<input type="hidden" name="cxml-base64" value="${orderAsCXML}">
	</form>
	</jsp:body>
</template:page>
