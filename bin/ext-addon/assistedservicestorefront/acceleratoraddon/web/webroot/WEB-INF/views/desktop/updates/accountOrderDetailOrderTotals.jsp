<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="account-orderdetail clearfix well well-tertiary well-lg">
    <ycommerce:testId code="orderDetail_overview_section">
        <order:accountOrderDetailsOverview order="${orderData}"/>
    </ycommerce:testId>
	<c:if test="${not empty orderData.placedBy}">
		<c:choose>
			<c:when test="${not empty agent}">
				<spring:theme code="text.account.order.placedBy" arguments="${orderData.placedBy}"/>
			</c:when>
			<c:otherwise>
				<spring:theme code="text.account.order.placedByText"/>
			</c:otherwise>
		</c:choose>
	</c:if>
</div>
<div class="account-orderdetail">
    <div class="account-orderdetail-item-section-footer">
        <order:receivedPromotions order="${orderData}"/>
        <order:orderTotalsItem order="${orderData}"/>
    </div>
</div>