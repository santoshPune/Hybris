<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/desktop/checkout/multi" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement" %>

<c:url value="/checkout/multi/tax-invoice/add" var="submitUrl"/>

<template:page pageTitle="${pageTitle}" >

	<div id="globalMessages">
		<common:globalMessages/>
	</div>

	<multi-checkout:checkoutProgressBar steps="${checkoutSteps}" progressBarId="${progressBarId}"/>
	<div class="span-14 append-1">
		<div id="checkoutContentPanel" class="clearfix">
			<div class="headline">
				<spring:theme code="checkout.multi.taxInvoice.stepHeader"/>
			</div>
			<div class="description">
				<spring:theme code="checkout.multi.addInvoice" />
			</div>
			<div class="clearfix">
				<label class="control-label" for="invoiceType">
						<spring:theme code="invoice.required"/>
						<span class="mandatory">
							<spring:theme code="login.required" var="loginRequiredText" />
							<img width="5" height="6" alt="${loginRequiredText}" title="${loginRequiredText}" src="${commonResourcePath}/images/mandatory.gif">
						</span>
				</label>
				<div class="controls">
					<label>
						<input type="radio" id="invoiceRequiredYes" name="invoiceProvide" ${invoiceForm.invoiceRequired ? 'checked="checked"' : ''}/>
						<spring:theme code="invoice.required.yes" />
					</label>
					<label>
						<input type="radio" id="invoiceRequiredNo" name="invoiceProvide" ${invoiceForm.invoiceRequired ? '' : 'checked="checked"'}/>
						<spring:theme code="invoice.required.no" />
					</label>
				</div>
			</div>
			<div class="clearfix invoiceForm">
				<form:form method="post" commandName="invoiceForm" action="${submitUrl}">
					<input type="hidden" name="id" value="${invoiceForm.id}"/>
					<input type="hidden" name="invoiceRequired" id="invoiceRequired" value="${invoiceForm.invoiceRequired}"/>
					
					<template:errorSpanField path="recipientType">
						<ycommerce:testId code="LoginPage_Item_invoiceTitle">
							<label class="control-label ${labelCSS}" for="recipientType">
								<spring:theme code="invoice.recipientType"/>
								<span class="mandatory">
									<spring:theme code="login.required" var="loginRequiredText" />
									<img width="5" height="6" alt="${loginRequiredText}" title="${loginRequiredText}" src="${commonResourcePath}/images/mandatory.gif">
								</span>
								<span class="skip"><form:errors path="recipientType"/></span>
							</label>
							<div class="controls">
								<select ${invoiceForm.invoiceRequired ? '' : 'disabled="disabled"'} name="recipientType" id="invoiceTitle">
									<c:forEach items="${recipientTypes}" var="recipientType">
										<option value="${recipientType.code}" ${(recipientType.code eq "INDIVIDUAL" && empty invoiceForm.recipientType) ? 'selected="selected"' : ''} ${recipientType.code eq invoiceForm.recipientType ? 'selected="selected"' : ''}><spring:theme code='type.enum.invoice.recipientType.${recipientType.code}.code'/></option>
									</c:forEach>	
								</select>
							</div>
						</ycommerce:testId>
					</template:errorSpanField>
					
					<template:errorSpanField path="recipient">
						<ycommerce:testId code="LoginPage_Item_invoiceName">
							<div id="invoiceNamePanel">
								<label class="control-label" for="recipient">
									<span class="skip"><form:errors path="recipient"/></span>
								</label>
								<div class="controls">
									<input type="text" value="${invoiceForm.recipient}" ${invoiceForm.invoiceRequired ? '' : 'disabled="disabled"'}
										 id="invoiceName" name="recipient" placeholder="<spring:theme code='invoice.recipient.placeholder.value'/>">
								</div>
							</div>
						</ycommerce:testId>
					</template:errorSpanField>
					
					<template:errorSpanField path="category">
						<ycommerce:testId code="LoginPage_Item_category">
							<label class="control-label ${labelCSS}" for="category">
								<spring:theme code="invoice.category"/>
								<span class="mandatory">
									<spring:theme code="login.required" var="loginRequiredText" />
									<img width="5" height="6" alt="${loginRequiredText}" title="${loginRequiredText}" src="${commonResourcePath}/images/mandatory.gif">
								</span>
								<span class="skip"><form:errors path="category"/></span>
							</label>
							<div class="controls">
								<select name="category" ${invoiceForm.invoiceRequired ? '' : 'disabled="disabled"'}  id="invoiceCategory">
									<c:forEach items="${invoiceCategories}" var="category">
										<option value="${category.code}" ${(category.code eq "GENERAL" && empty invoiceForm.category) ? 'selected="selected"' : ''} ${category.code eq invoiceForm.category ? 'selected="selected"' : ''}><spring:theme code='type.enum.invoice.category.${category.code}.code'/></option>
									</c:forEach>	
								</select>
							</div>
						</ycommerce:testId>
					</template:errorSpanField>
					<div class="form-actions">
						<a class="button" href="<c:url value='${previousStepUrl}'/>">
							<spring:theme code="checkout.multi.cancel" text="Cancel" />
						</a> 
						<button id="chooseDeliveryMethod_continue_button" class="positive right show_processing_message">
							<spring:theme code="checkout.multi.deliveryMethod.continue" text="Continue"/>
						</button>
					</div>
				</form:form>
			</div>
		</div>
	</div>
	<multi-checkout:checkoutOrderDetails cartData="${cartData}" showShipDeliveryEntries="true" showPickupDeliveryEntries="true" showTax="false"/>
	<cms:pageSlot position="SideContent" var="feature" element="div" class="span-24 side-content-slot cms_disp-img_slot">
		<cms:component component="${feature}"/>
	</cms:pageSlot>

</template:page>
