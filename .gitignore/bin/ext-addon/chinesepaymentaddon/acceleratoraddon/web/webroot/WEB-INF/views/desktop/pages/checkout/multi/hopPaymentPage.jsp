<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart" %>
<%@ taglib prefix="checkout" tagdir="/WEB-INF/tags/desktop/checkout" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/desktop/checkout/multi" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>




<spring:url value="/checkout/multi/summary/payRightNow" var="payRightNowUrl"/>
<spring:url value="/checkout/multi/summary/checkPaymentResult" var="checkPaymentResultUrl"/>


<template:page pageTitle="${pageTitle}" hideHeaderLinks="true">
	<div id="globalMessages">
		<common:globalMessages/>
	</div>

	<multi-checkout:checkoutProgressBar steps="${checkoutSteps}" progressBarId="${progressBarId}"/>
	<div class="span-14 append-1">
		
	<div id="doPaymentPanel" class="clearfix summaryFlow">
			<spring:theme code="checkout.multi.clicktopay"/><br>
			<div>
				<spring:theme code="order.submit.order.code" /><strong><spring:theme text="${orderData.code}"/></strong>
			</div>
			<div>
				<spring:theme code="order.submit.due.payment" /><strong><format:price priceData="${orderData.totalPrice}"/></strong>
			</div>
			<div>
				<a id="payRightNowButton" class="button positive left place-order" href="${payRightNowUrl}/${orderData.code}" target="_blank"><spring:theme code="checkout.multi.payrightnow"/></a>
			</div>
	</div>		
	
	<div class="mask"></div>
    <div class="payPop">
    	<p><spring:theme code="order.payment.remark" /></p>
        <div class="payPopBtn clearfix">
        	<a href="${checkPaymentResultUrl}/${orderData.code}"  class="button positive payPopBtn_left"><spring:theme code="order.payment.successfully" /></a>
            <a href="${checkPaymentResultUrl}/${orderData.code}" class="button positive payPopBtn_right"><spring:theme code="order.payment.rencontre.problem" /></a>
        </div>	
    </div>

	<cms:pageSlot position="SideContent" var="feature" element="div" class="span-24 side-content-slot cms_disp-img_slot">
		<cms:component component="${feature}"/>
	</cms:pageSlot>
</template:page>
