<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/desktop/order"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="orderBoxes clearfix">
	<order:deliveryAddressItem order="${orderData}" />
	<order:deliveryMethodItem order="${orderData}" />
	<c:if test="${not empty orderData.chinesePaymentInfo}">
		<div class="orderBox payment">
			<div class="headline">
				<spring:theme code="text.paymentDetails" text="Payment Details" />
			</div>
			<ul>
				<li><spring:theme code="checkout.summary.paymentMethod.name" htmlEscape="false"/></li>
				<li><spring:theme text="${orderData.chinesePaymentInfo.paymentProvider}"/></li>
			</ul>
		</div>
	</c:if>
</div>