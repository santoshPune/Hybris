<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>

<spring:theme code="text.addToCart" var="addToCartText" text="Add To Cart" />
<spring:theme code="sapproductconfig.addtocart.title" var="addedToCartText" text="Added to Your Shopping Cart" />
<spring:theme code="sapproductconfig.updateCart" var="updateCartText" text="Update Cart" />
<spring:theme code="sapproductconfig.addtocart.update.title" var="updatedInCartText" text="Shopping Cart Updated" />

<template:page pageTitle="${pageTitle}">

	<jsp:body>
		<div class="cpq-overview-page">
			<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
			
			<div id="product-details-header">				
				<div class="row">
					<div class="cpq-page col-xs-12">
						<cms:pageSlot position="ConfigOverviewTitle" var="comp" element="div">
							<cms:component component="${comp}" />	
						</cms:pageSlot>		
					</div>
				</div>
			</div>
			
			<cms:pageSlot position="Section1" var="comp" element="div">
				<cms:component component="${comp}" />
			</cms:pageSlot>
			
			<c:set var="contentColStyle" value="cpq-page col-xs-12 col-lg-8 col-md-8" />
			
			<div id="overviewContent">
				
				<div class="row">		
					<div class="${contentColStyle}">				
						<cms:pageSlot position="ConfigOverviewHeader" var="comp" element="div">
							<cms:component component="${comp}" />
						</cms:pageSlot>
		            <div class="cpq-overview-separator">
		            	<div class="col-sm-6 cpq-overview-refine">
			            	<spring:theme code="search.nav.selectRefinements.title" var="selectRefinements"/>
		   	            <button class="hidden-md hidden-lg js-show-facets cpq-btn-refine" data-select-refinements-title="${selectRefinements}"><spring:theme code="search.nav.refine.button" text="Refine"/></button>
		            	</div>
		            	<div class="col-sm-6 cpq-overview-skip">
				            <button type="submit" id="cpqSkipBtn"
									class="cpq-btn-skip">
									<spring:theme code="sapproductconfig.overview.skipThisStep" text="Skip This Step" />
								</button>
		            	</div>
		            </div>
						<cms:pageSlot id="overviewContentSlot" position="ConfigOverviewContent" var="feature" element="div">
							<cms:component component="${feature}" />
						</cms:pageSlot>
						<cms:pageSlot id="overviewBottombarSlot" position="ConfigOverviewBottombar" var="feature" element="div">
							<cms:component component="${feature}" />
						</cms:pageSlot>
					</div>
					
					<div class="cpq-page hidden-xs hidden-sm col-md-4 col-lg-4">
						<cms:pageSlot id="overviewSidebarSlot" position="ConfigOverviewSidebar" var="feature" element="div">
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
	CPQ.core.baseUrl = '${baseUrl}';
</script>
