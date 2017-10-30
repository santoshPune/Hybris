<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>

<template:page pageTitle="${pageTitle}">

	<jsp:body>
		<div class="cpq-config-page">
			<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">	
			
			<div id="product-details-header">
				<c:if test="${!config.singleLevel}"> 
					<div id="cpqMenuArea" class="hidden-md hidden-lg cpq-menu-icon-area">
						<div id="menuIcon" class="cpq-menu-icon"></div>
					</div>
				</c:if>
				<div class="row">
					<div class="cpq-page col-xs-12">
						<cms:pageSlot position="ConfigTitle" var="comp" element="div">
							<cms:component component="${comp}" />	
						</cms:pageSlot>		
					</div>
				</div>
				<cms:pageSlot position="ConfigHeader" var="comp" element="div">
					<cms:component component="${comp}" />
				</cms:pageSlot>
			</div>
			
			<cms:pageSlot position="Section1" var="comp" element="div">
				<cms:component component="${comp}" />
			</cms:pageSlot>
			
			<c:set var="contentColStyle" value="cpq-page col-xs-12 col-lg-8" />
			<c:if test="${!config.singleLevel}"> 
				<c:set var="contentColStyle" value="${contentColStyle} col-sm-12 col-md-8" />
			</c:if>
			
			<div id="dynamicConfigContent">
				<div class="row">
					<div class="col-xs-12">
						<div id="cpq-message-area" >
	   	 			  <common:globalMessages />
						</div>
					</div>
				</div>
				
				<div class="row">		
					<div class="${contentColStyle}">				
						<cms:pageSlot id="configContentSlot" position="ConfigContent" var="feature" element="div">
							<cms:component component="${feature}" />
						</cms:pageSlot>
						<cms:pageSlot id="configBottombarSlot" position="ConfigBottombar" var="feature" element="div">
							<cms:component component="${feature}" />
						</cms:pageSlot>
					</div>
					
					<div class="cpq-page hidden-xs hidden-sm col-md-4 col-lg-4">
						<cms:pageSlot id="configSidebarSlot" position="ConfigSidebar" var="feature" element="div">
							<cms:component component="${feature}" />
						</cms:pageSlot>
					</div>		
				</div>
				
			</div>
		
	
			<cms:pageSlot position="Section2" var="feature" element="div">
				<cms:component component="${feature}" />
			</cms:pageSlot>
			<cms:pageSlot position="Section3" var="feature" element="div">
				<cms:component component="${feature}" />
			</cms:pageSlot>
			
			<cms:pageSlot position="Section4" var="feature" element="div">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</div>
	</jsp:body>
</template:page>
<spring:url value="${product.code}" var="baseUrl" />

<script type="text/javascript">
	//ysapproductconfigaddonConstants.init('${baseUrl}', '${addToCartText}',
	//		'${updateCartText}', '${addedToCartText}', '${updatedInCartText}',
	//		'${noStock}');
	//ant initConfigPage();
	CPQ.core.baseUrl = '${baseUrl}';	
</script>
