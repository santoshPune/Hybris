<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<div class="checkout-success">
    <div class="checkout-success__body">
        <c:choose>
            <c:when test="${orderData.status == 'CANCELLED'}">
                <div class="checkout-success__body__headline">
                    <spring:theme code="order.quoteRequest.cancelled.confirmation" arguments="${orderData.code}"/>
                </div>
            </c:when>
            <c:otherwise>
                <div class="checkout-success__body__headline">
                    <spring:theme code="order.quoteRequest.thankYou"/>
                </div>
                <p>
                    <spring:theme code="text.account.order.orderNumberLabel"/>
                    <b> ${orderData.code}</b>
                </p>
            </c:otherwise>
        </c:choose>

        <c:if test="${not empty orderData.b2BComment && (orderData.status=='REJECTED_QUOTE' || orderData.status=='PENDING_QUOTE')}">
            <p class="checkout-success-body-quote-text">
                <b><spring:theme code="order.quoteReason" /></b>
                ${fn:escapeXml(orderData.b2BComment.comment)}
            </p>
        </c:if>

        <p>
            <spring:theme code="checkout.orderConfirmation.copySentToShort"/>
            <b> ${orderData.user.uid}</b>
        </p>
    </div>
</div>

<div class="well well-tertiary well-single-headline">
    <div class="well-headline"><spring:theme code="checkout.multi.order.summary" /></div>
</div>