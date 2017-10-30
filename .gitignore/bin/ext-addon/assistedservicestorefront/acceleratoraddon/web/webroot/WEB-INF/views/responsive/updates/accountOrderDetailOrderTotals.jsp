<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:if test="${not empty orderData.placedBy}">
	<div class="alert alert-info asm-placedby">
	<c:choose>
		<c:when test="${not empty agent}">
			<spring:theme code="text.account.order.placedBy" arguments="${orderData.placedBy}"/>
		</c:when>
		<c:otherwise>
			<spring:theme code="text.account.order.placedByText"/>
		</c:otherwise>
	</c:choose>
	</div>
</c:if>

<order:accountOrderDetailOrderTotals order="${orderData}"/>