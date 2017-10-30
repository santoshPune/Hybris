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

<spring:url value="/checkout/multi/summary/payRightNow" var="payRightNowUrl"/>
<spring:url value="/checkout/multi/summary/checkPaymentResult" var="checkPaymentResultUrl"/>
<spring:url value="/my-account/order/cancel/${orderData.code}" var="orderCancelUrl"/>

<div class="form-actions">
	<c:if test="${orderData.status.code != 'CANCELLED' and orderData.paymentStatus.code eq 'NOTPAID'}">
		<a class="button positive left show_processing_message" href="${orderCancelUrl}">
			<spring:theme code="order.detail.button.pay.cancel" text="CancelOrder"/>
		</a>
		<a id="payRightNowButton" class="button positive right show_processing_message" href="${payRightNowUrl}/${orderData.code}" target="_blank">
			<spring:theme code="order.detail.button.pay.immediately" text="PayImmediately"/>
		</a>
	</c:if>
</div>

<div class="mask"></div>
<div class="payPop">
	<p><spring:theme code="order.payment.remark" /></p>
    <div class="payPopBtn clearfix">
    	<a href="${checkPaymentResultUrl}/${orderData.code}?result=success"  class="button positive payPopBtn_left"><spring:theme code="order.payment.successfully" /></a>
        <a href="${checkPaymentResultUrl}/${orderData.code}?result=failed" class="button positive payPopBtn_right"><spring:theme code="order.payment.rencontre.problem" /></a>
    </div>	
</div>