<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<div class="well well-tertiary well-lg">
    <ycommerce:testId code="orderDetail_overview_section">
        <order:accountOrderDetailsOverview order="${orderData}"/>
    </ycommerce:testId>
</div>