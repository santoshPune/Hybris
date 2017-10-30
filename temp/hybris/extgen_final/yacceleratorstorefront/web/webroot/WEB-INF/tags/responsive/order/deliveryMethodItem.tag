<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.AbstractOrderData" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:htmlEscape defaultHtmlEscape="true"/>
<div>
    <div class="label-order">
        <spring:theme code="text.shippingMethod"/>
    </div>
    ${order.deliveryMode.name}
    <br>
    ${order.deliveryMode.description}
</div>