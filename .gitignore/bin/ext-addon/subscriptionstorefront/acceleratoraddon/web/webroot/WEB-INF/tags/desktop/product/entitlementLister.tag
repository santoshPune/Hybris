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

<c:if test="${not empty subscriptionData.entitlements}">
    <c:forEach items="${subscriptionData.entitlements}" var="entitlement" varStatus="loop">
            <b>${entitlement.name}</b>
            <c:if test="${entitlement.quantity eq null}">
                <spring:theme code="product.list.viewplans.entitlements.true" text="true"/>
            </c:if>
            <br/>
            <c:if test="${entitlement.quantity ne null}">
                <c:choose>
                    <c:when test="${entitlement.quantity lt 0}">
                        <spring:theme code="product.list.viewplans.entitlements.unlimited"/>
                    </c:when>
                    <c:when test="${entitlement.quantity eq 1}">
                        <spring:theme code="product.list.viewplans.entitlements.meteredEntitlement" arguments="${entitlement.quantity}" argumentSeparator="^" />
                    </c:when>
                </c:choose>
            </c:if>
            <c:if test="${not loop.last}">
                <br/><br/>
            </c:if>
    </c:forEach>
</c:if>