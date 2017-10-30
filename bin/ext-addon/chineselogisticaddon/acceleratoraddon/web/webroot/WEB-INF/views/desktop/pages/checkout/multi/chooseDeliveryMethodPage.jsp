<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="checkout" tagdir="/WEB-INF/tags/desktop/checkout" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/desktop/checkout/multi" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<c:url value="${nextStepUrl}" var="continueSelectDeliveryMethodUrl"/>
<c:url value="${previousStepUrl}" var="addDeliveryAddressUrl"/>
<template:page pageTitle="${pageTitle}" hideHeaderLinks="true">

	<div id="globalMessages">
		<common:globalMessages/>
	</div>

	<multi-checkout:checkoutProgressBar steps="${checkoutSteps}" progressBarId="${progressBarId}"/>
	<div class="span-14 append-1">
		<div id="checkoutContentPanel" class="clearfix">
			<div class="headline"><spring:theme code="checkout.multi.deliveryMethod.stepHeader"/></div>
			<div class="description"><p><spring:theme code="checkout.multi.deliveryMethod.selectDeliveryMethodMessage"/></p></div>
			<form:form id="selectDeliveryMethodForm" action="${request.contextPath}/checkout/multi/delivery-method/select" method="post">
				<multi-checkout:deliveryMethodSelector deliveryMethods="${deliveryMethods}" selectedDeliveryMethodId="${cartData.deliveryMode.code}"/>
				
				<br>
				<br>
				<div class="description"><p><spring:theme code="checkout.multi.deliveryTimeSlot.selectDeliveryTimeSlotMessage"/></p></div>
				<ul class="delivery_method-list">
					<c:forEach items="${deliveryTimeSlots}" var="deliveryTimeSlot" varStatus="index">
					<li><label><input type="radio" name="deliveryTimeSlot"
						value="${deliveryTimeSlot.code}" ${(index.index==0 && empty cartData.deliveryTimeSlot.code) ? "checked=checked":""} ${cartData.deliveryTimeSlot.code eq deliveryTimeSlot.code ? "checked=checked":""}/> <spring:theme text="${deliveryTimeSlot.name}"/></label>
					</li>
					</c:forEach>
				</ul>
				
				
				
				<c:if test="${not empty cartData.deliveryMode.code}">
					<div class="form-actions">
						<a class="button" href="${addDeliveryAddressUrl}"><spring:theme code="checkout.multi.cancel" text="Cancel"/></a>
						<button id="chooseDeliveryMethod_continue_button" class="positive right show_processing_message">
							<spring:theme code="checkout.multi.deliveryMethod.continue" text="Continue"/>
						</button>
					</div>
				</c:if>
			</form:form>
		</div>
	</div>
	<multi-checkout:checkoutOrderDetails cartData="${cartData}" showShipDeliveryEntries="true" showPickupDeliveryEntries="false" showTax="false"/>
	<cms:pageSlot position="SideContent" var="feature" element="div" class="span-24 side-content-slot cms_disp-img_slot">
		<cms:component component="${feature}"/>
	</cms:pageSlot>

</template:page>
