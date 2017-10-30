<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>

<spring:url value="/my-account/order/cancel/${orderData.code}" var="orderCancelUrl" />

<c:if test="${orderData.status.code != 'CANCELLED' and orderData.paymentStatus.code eq 'NOTPAID'}">
	<div class="label-order">
		<a id="orderCancelButton" href="${orderCancelUrl}" class="payment-action"> 
			<spring:theme code="order.detail.button.pay.cancel" text="CancelOrder" />
		</a>
	</div>
</c:if>
