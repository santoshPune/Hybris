<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${not empty orderData.deliveryTimeSlot}">
	<div class="account-orderdetail well well-tertiary account-consignment">
		<div class="well-headline well-headline-deliverytimeslot">
			<spring:theme code="text.account.order.orderDetails.DeliveryTimeSlotMessage" />
			<span class="well-headline-sub">
				${orderData.deliveryTimeSlot.name}
			</span>
		</div>
	</div>
</c:if>
