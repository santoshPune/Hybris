<!--
 [y] hybris Platform

 Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 All rights reserved.

 This software is the confidential and proprietary information of SAP
 ("Confidential Information"). You shall not disclose such Confidential
 Information and shall use it only in accordance with the terms of the
 license agreement you entered into with SAP.
-->

<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component" %>


	<jsp:useBean id="random" class="java.util.Random" scope="application" />
	<c:set var="cid" value="reco${random.nextInt(1000)}"/>

	<div class="scroller" id="${cid}"  data-prodcode="${productCode}" data-componentId="${componentId}" data-base-url="${request.contextPath}"/>
	
	<script type="text/javascript">
	   function loadData() {
			function getElementsByClassName(node, classname) {
				var a = [];
				var re = new RegExp('(^| )' + classname + '( |$)');
				var els = node.getElementsByTagName("*");
				for ( var i = 0, j = els.length; i < j; i++)
					if (re.test(els[i].className))
						a.push(els[i]);
				return a;
			}

			divs = getElementsByClassName(document.body, 'scroller');

		   for(var i = 0; i < divs.length; i++){
			      if(divs[i].id.search("reco")> -1){
			    	  var productCode =$("#"+divs[i].id).attr("data-prodcode");
					  var componentId = $("#"+divs[i].id).attr("data-componentId");
					  
					  retrieveRecommendations(divs[i].id, productCode, componentId);
			      }
			   }		   
	   }
	   
	   window.onload = loadData;
	</script>