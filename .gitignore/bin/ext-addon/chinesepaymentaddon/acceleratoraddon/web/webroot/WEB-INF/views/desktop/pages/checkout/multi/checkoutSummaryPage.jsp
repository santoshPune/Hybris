<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart"%>
<%@ taglib prefix="checkout" tagdir="/WEB-INF/tags/desktop/checkout"%>
<%@ taglib prefix="multi-checkout"
	tagdir="/WEB-INF/tags/desktop/checkout/multi"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<spring:url value="/checkout/multi/summary/placeOrder"
	var="placeOrderUrl" />
<spring:url value="/checkout/multi/termsAndConditions"
	var="getTermsAndConditionsUrl" />


<template:page pageTitle="${pageTitle}" hideHeaderLinks="true">

	<div id="globalMessages">
		<common:globalMessages />
	</div>

	<multi-checkout:checkoutProgressBar steps="${checkoutSteps}"
		progressBarId="${progressBarId}" />
	<div class="span-14 append-1">

		<div id="checkoutContentPanel" class="clearfix summaryFlow">
			<div class="headline">
				<spring:theme code="checkout.summary.reviewYourOrder" />
			</div>
			<c:set value="${deliveryAddress}" var="address"></c:set>
			<c:set var="hasShippedItems"
				value="${cartData.deliveryItemsQuantity > 0}" />

			<div class="summaryDeliveryAddress">
				<ycommerce:testId code="checkout_deliveryAddressData_text">
					<c:if test="${not hasShippedItems}">
						<spring:theme code="checkout.pickup.no.delivery.required" />
					</c:if>

					<c:if test="${hasShippedItems}">
						<strong><spring:theme
								code="checkout.summary.deliveryAddress.header"
								htmlEscape="false" /></strong>
						<%@ include
							file="/WEB-INF/views/addons/chineseprofileaddon/desktop/fragments/address/addressBookEntry.jsp"%>
					</c:if>
				</ycommerce:testId>

				<c:if test="${hasShippedItems}">
					<ycommerce:testId code="checkout_changeAddress_element">
						<c:url value="/checkout/multi/delivery-address/edit"
							var="editAddressUrl" />
						<a href="${editAddressUrl}/?editAddressCode=${deliveryAddress.id}"
							class="button positive editButton"><spring:theme
								code="checkout.summary.edit" /></a>
					</ycommerce:testId>
				</c:if>
			</div>

			<hr>
			<multi-checkout:summaryFlowDeliveryMode
				deliveryMode="${deliveryMode}" cartData="${cartData}" />
			</hr>
			<div class="summaryPayment clearfix"
				data-security-what-text="${securityWhatText}">
				<ycommerce:testId code="checkout_paymentDetails_text">
					<div class="column append-1">
						<strong><spring:theme
								code="checkout.summary.paymentMethod.name" htmlEscape="false" /></strong>
						<img src="${paymentLogo}" width="100px">
					</div>
				</ycommerce:testId>
				<ycommerce:testId code="checkout_changePayment_element">
					<c:url value="/checkout/multi/payment-method/add"
						var="addPaymentMethodUrl" />
					<a href="${addPaymentMethodUrl}" class="button positive editButton"><spring:theme
							code="checkout.summary.edit" /></a>
				</ycommerce:testId>
			</div>
		</div>





		<cart:cartPromotions cartData="${cartData}" />

		<form:form action="${placeOrderUrl}" id="placeOrderForm1"
			commandName="placeOrderForm">
			<c:if test="${requestSecurityCode}">
				<form:input type="hidden" class="securityCodeClass"
					path="securityCode" />
				<button type="submit"
					class="positive right pad_right place-order placeOrderWithSecurityCode">
					<spring:theme code="checkout.summary.placeOrder" />
				</button>
			</c:if>

			<c:if test="${not requestSecurityCode}">
				<button type="submit" class="positive right place-order">
					<spring:theme code="checkout.summary.placeOrder" />
				</button>
			</c:if>
			<div class="terms">
				<form:checkbox id="Terms1" path="termsCheck" />
				<label for="Terms1"><spring:theme
						code="checkout.summary.placeOrder.readTermsAndConditions"
						arguments="${getTermsAndConditionsUrl}" /></label>
			</div>
		</form:form>
	</div>
	<multi-checkout:checkoutOrderDetails cartData="${cartData}"
		showShipDeliveryEntries="true" showPickupDeliveryEntries="true"
		showTax="true" />

	<cms:pageSlot position="SideContent" var="feature" element="div"
		class="span-24 side-content-slot cms_disp-img_slot">
		<cms:component component="${feature}" />
	</cms:pageSlot>

</template:page>
