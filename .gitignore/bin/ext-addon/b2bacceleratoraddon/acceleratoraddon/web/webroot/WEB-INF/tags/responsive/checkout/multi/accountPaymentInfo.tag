<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<ul class="checkout-order-summary-list">
    <li class="checkout-order-summary-list-heading">
        <div class="title">
            <spring:theme code="checkout.multi.payment"/>
        </div>
        <div class="address">
            <spring:theme code="text.company.user.${cartData.b2bCustomerData.titleCode}.name" text="N/A"/>&nbsp;${fn:escapeXml(cartData.b2bCustomerData.firstName)}&nbsp;${fn:escapeXml(cartData.b2bCustomerData.lastName)}
            <c:if test="${(not empty cartData.costCenter) and (not empty cartData.costCenter.code)}">
                <br>
                <spring:theme code="checkout.multi.costCenter.label" htmlEscape="false"/>:&nbsp;${fn:escapeXml(cartData.costCenter.name)}
            </c:if>
            <br>
            <c:choose>
                <c:when test="${not empty cartData.purchaseOrderNumber}">
                    <spring:theme code="checkout.multi.purchaseOrderNumber.label" htmlEscape="false"/>:&nbsp;${fn:escapeXml(cartData.purchaseOrderNumber)}
                </c:when>
                <c:otherwise>
                    <spring:theme code="checkout.multi.purchaseOrderNumber.label" htmlEscape="false"/>:&nbsp;-
                </c:otherwise>
            </c:choose>
        </div>
    </li>
</ul>
