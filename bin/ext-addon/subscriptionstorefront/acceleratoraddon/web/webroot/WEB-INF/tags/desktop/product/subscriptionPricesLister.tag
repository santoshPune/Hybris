<!--
 [y] hybris Platform

 Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 All rights reserved.

 This software is the confidential and proprietary information of SAP
 ("Confidential Information"). You shall not disclose such Confidential
 Information and shall use it only in accordance with the terms of the
 license agreement you entered into with SAP.

-->

<%@ attribute name="subscriptionData" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>

<c:if test="${not empty subscriptionData.price.recurringChargeEntries}">
    <c:set var="recurringChargeCount" value="${fn:length(subscriptionData.price.recurringChargeEntries)}"/>
    <c:forEach items="${subscriptionData.price.recurringChargeEntries}" var="recurringPrice" varStatus="recurringPricesCounter">
        <c:choose>
            <c:when test="${recurringPrice.cycleEnd == '-1'}">
                <c:if test="${recurringChargeCount gt 1}">
                    <spring:theme code="product.list.viewplans.price.interval.unlimited" arguments="${recurringPrice.cycleStart}"/>
                </c:if>
                <c:if test="${recurringChargeCount eq 1 and recurringPrice.cycleStart gt 1}">
                    <spring:theme code="product.list.viewplans.price.interval.unlimited" arguments="${recurringPrice.cycleStart}"/>
                </c:if>
                <format:price priceData="${recurringPrice.price}"/>
            </c:when>
            <c:otherwise>
                <spring:theme code="product.list.viewplans.price.interval" arguments="${recurringPrice.cycleStart}, ${recurringPrice.cycleEnd}"/>
                <format:price priceData="${recurringPrice.price}"/>
            </c:otherwise>
        </c:choose>
        <br>
    </c:forEach>
    <div class="pay">${subscriptionData.subscriptionTerm.billingPlan.billingTime.name}</div>
</c:if>
<c:if test="${not empty subscriptionData.price.oneTimeChargeEntries}">
    <c:if test="${not empty subscriptionData.price.recurringChargeEntries}">
        <br>
    </c:if>
    <c:forEach items="${subscriptionData.price.oneTimeChargeEntries}" var="oneTimePrice" varStatus="oneTimePricesCounter">
        <c:if test="${not oneTimePricesCounter.first}">
            <br/>
        </c:if>
        <spring:theme code="product.list.viewplans.price.onetime" arguments="${oneTimePrice.name}"/>
        <format:price priceData="${oneTimePrice.price}"/>
        <div class="pay">${oneTimePrice.billingTime.name}</div>
    </c:forEach>
</c:if>

