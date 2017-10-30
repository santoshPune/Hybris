<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<div class="col-sm-3 pull-right">
    <a href="#" class="save__cart--link js-save-cart-link">
        <spring:theme code="basket.save.cart" />
    </a>
</div>
    
<spring:url value="/cart/save" var="actionUrl" htmlEscape="false"/>
<cart:saveCartModal titleKey="text.save.cart.title" actionUrl="${actionUrl}" messageKey="basket.save.cart.info.msg"/>