<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/desktop/checkout/multi" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:url value="${currentStepUrl}" var="choosePaymentMethodUrl"/>
<c:url value="/checkout/multi/payment-method/back" var="previousStepUrl"/>
<template:page pageTitle="${pageTitle}" hideHeaderLinks="true">

	<div id="globalMessages">
		<common:globalMessages/>
	</div>
	
	<multi-checkout:checkoutProgressBar steps="${checkoutSteps}" progressBarId="${progressBarId}"/>
	<div class="span-14 append-1">
		<div id="checkoutContentPanel" class="clearfix">
			<div class="headline"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentmode"/></div>
			<div class="description"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.chooseyourpaymentmode"/></div>

			<form:form method="post" action="${choosePaymentMethodUrl}" class="create_update_payment_form">
				<ul class="delivery_method-list">
					<c:forEach items="${paymentMethodsList}" var="paymentMethod">
					<li><label><input type="radio" name="paymentId"
						value="${paymentMethod.code}" ${(index.index==0 && empty cartData.chinesePaymentInfo.paymentProvider) ? "checked=checked":""} ${cartData.chinesePaymentInfo.paymentProvider eq paymentMethod.code ? "checked=checked":""}/> <img src="${paymentMethod.logoUrl}" width="100px"></label>
					</li>
					</c:forEach>
					</ul>

				<div class="form-actions">
						<a class="button" href="${previousStepUrl}"><spring:theme code="checkout.multi.cancel" text="Cancel"/></a>
					<ycommerce:testId code="editPaymentMethod_savePaymentMethod_button">
						<button class="positive right show_processing_message" tabindex="20" id="lastInTheForm" type="submit">
							<spring:theme code="checkout.multi.paymentMethod.continue"/>
						</button>
					</ycommerce:testId>
				</div>
			</form:form>
		</div>
	</div>
	<multi-checkout:checkoutOrderDetails cartData="${cartData}" showShipDeliveryEntries="true" showPickupDeliveryEntries="true" showTax="true"/>

	<cms:pageSlot position="SideContent" var="feature" element="div" class="span-24 side-content-slot cms_disp-img_slot">
		<cms:component component="${feature}"/>
	</cms:pageSlot>

</template:page>
