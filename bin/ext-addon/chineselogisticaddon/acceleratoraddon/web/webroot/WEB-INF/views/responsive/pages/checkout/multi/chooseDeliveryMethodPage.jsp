<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/responsive/checkout/multi"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>


<c:url value="${nextStepUrl}" var="continueSelectDeliveryMethodUrl"/>
<c:url value="${previousStepUrl}" var="addDeliveryAddressUrl"/>
<template:page pageTitle="${pageTitle}" hideHeaderLinks="true">

	
	<div class="row">
		<div class="col-sm-6">
			<div class="checkout-headline">
		<spring:theme code="checkout.multi.secure.checkout" text="Secure Checkout"></spring:theme>
	</div>
		<multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}" progressBarId="${progressBarId}">
			<jsp:body>
				<ycommerce:testId code="checkoutStepTwo">
					<div class="checkout-shipping">
						<multi-checkout:shipmentItems cartData="${cartData}" showDeliveryAddress="true" />
						<div class="checkout-indent">
							<div class="headline"><spring:theme code="checkout.summary.deliveryMode.selectDeliveryMethodForOrder" text="Shipping Method"></spring:theme></div>
							<form:form id="selectDeliveryMethodForm" action="${request.contextPath}/checkout/multi/delivery-method/select" method="post">
								<div class="form-group">
									<multi-checkout:deliveryMethodSelector deliveryMethods="${deliveryMethods}" selectedDeliveryMethodId="${cartData.deliveryMode.code}"/>
								</div>
									<div class="headline"><spring:theme code="checkout.multi.deliveryTimeSlot.selectDeliveryTimeSlotMessage"/></div>
								<div class="form-group">
									<div class="controls">
										<select name="deliveryTimeSlot" class="form-control">
											<c:forEach items="${deliveryTimeSlots}" var="deliveryTimeSlot" varStatus="index">
												<option value="${deliveryTimeSlot.code}" ${(index.index==0 && empty cartData.deliveryTimeSlot.code) ? "selected=selected":""} ${cartData.deliveryTimeSlot.code eq deliveryTimeSlot.code ? "selected=selected":""}><spring:theme text="${deliveryTimeSlot.name}"/></option>
											</c:forEach>
										</select>
									</div>
								</div>
								
					<button id="deliveryMethodSubmit" type="button" class="btn btn-primary btn-block checkout-next"><spring:theme code="checkout.multi.deliveryMethod.continue" text="Next"/></button>
							</form:form>
							<%-- <p><spring:theme code="checkout.multi.deliveryMethod.message" text="Items will ship as soon as they are available. <br> See Order Summary for more information." /></p> --%>
						</div>
					</div>
				</ycommerce:testId>
			</jsp:body>
		</multi-checkout:checkoutSteps>
		</div>
		<div class="col-sm-6 hidden-xs">
		<multi-checkout:checkoutOrderDetails cartData="${cartData}" showDeliveryAddress="true" showPaymentInfo="false" showTaxEstimate="false" showTax="true" />
		</div>
		<div class="col-sm-12 col-lg-12">
			<cms:pageSlot position="SideContent" var="feature" element="div" class="checkout-help">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
		</div>
	</div>

</template:page>
