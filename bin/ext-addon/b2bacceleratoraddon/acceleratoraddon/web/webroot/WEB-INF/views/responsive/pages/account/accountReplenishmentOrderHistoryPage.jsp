<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="b2b-order" tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/responsive/order" %>

<spring:htmlEscape defaultHtmlEscape="true"/>

<c:if test="${not empty orderData}">
    <div class="account-orderdetail well well-tertiary well-single-headline replenishment-order-history">
        <div class="well-headline">
            <spring:theme code="text.account.replenishment.history"/>
        </div>
    </div>

    <c:set var="searchUrl"
           value="/my-account/my-replenishment/${orderData.jobCode}?sort=${searchPageData.pagination.sort}&show=${param.show}"/>
    <b2b-order:orderListing searchUrl="${searchUrl}"
                            messageKey="text.account.orderHistory.page"></b2b-order:orderListing>
</c:if>