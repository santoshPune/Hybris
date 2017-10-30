<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<spring:htmlEscape defaultHtmlEscape="true" />


<h1 class="cart-headline border">
	<spring:theme code="text.cart"/>
    <c:if test="${not empty cartData.code}">
        <span class="cart-id-label">
            <spring:theme code="basket.page.cartIdShort"/><span class="cart-id">${cartData.code}</span>
        </span>
    </c:if>

    <sec:authorize ifNotGranted="ROLE_ANONYMOUS">
        <c:if test="${not empty savedCartCount and savedCartCount ne 0}">
            <spring:url value="/my-account/saved-carts" var="listSavedCartUrl" htmlEscape="false"/>
            <span class="cart-headline__saved-carts">
                <spring:theme code="saved.cart.total.description"/>
                <a href="${listSavedCartUrl}" class="cart-headline__saved-carts-counter">
                    <c:choose>
                        <c:when test="${savedCartCount > 1}">
                            <spring:theme code="saved.cart.total.number" arguments="${savedCartCount}"/>
                        </c:when>
                        <c:otherwise>
                            <spring:theme code="saved.cart.total.number.one" arguments="${savedCartCount}"/>
                        </c:otherwise>
                    </c:choose>
                </a>
            </span>
        </c:if>
	</sec:authorize>
</h1>


<c:if test="${not empty cartData.entries}">
    <c:url value="/cart/checkout" var="checkoutUrl" scope="session"/>
    <c:url value="${continueUrl}" var="continueShoppingUrl" scope="session"/>
    <c:set var="showTax" value="false"/>
	
    <div class="js-cart-top-totals cart-top-totals">
        <c:choose>
            <c:when test="${fn:length(cartData.entries) > 1}">
                <spring:theme code="basket.page.totals.total.items" arguments="${fn:length(cartData.entries)}"/>
            </c:when>
            <c:otherwise>
                <spring:theme code="basket.page.totals.total.items.one" arguments="${fn:length(cartData.entries)}"/>
            </c:otherwise>
        </c:choose>
        <ycommerce:testId code="cart_totalPrice_label">
            <span class="cart-top-totals-amount">
                <c:choose>
                    <c:when test="${showTax}">
                        <format:price priceData="${cartData.totalPriceWithTax}"/>
                    </c:when>
                    <c:otherwise>
                        <format:price priceData="${cartData.totalPrice}"/>
                    </c:otherwise>
                </c:choose>
            </span>
        </ycommerce:testId>
    </div>

    <div class="row">
        <div class="col-xs-12 col-md-7 col-lg-6 pull-right cart-actions--print">
            <div class="cart-actions">
                <div class="row">
                    <div class="col-sm-4 pull-right">
                        <ycommerce:testId code="checkoutButton">
                            <button class="btn btn-primary btn-block btn--continue-checkout js-continue-checkout-button" data-checkout-url="${checkoutUrl}">
                                <spring:theme code="checkout.checkout"/>
                            </button>
                        </ycommerce:testId>
                    </div>

                    <div class="col-sm-5 pull-right">
                        <button class="btn btn-default btn-block btn--continue-shopping js-continue-shopping-button" data-continue-shopping-url="${continueShoppingUrl}">
                            <spring:theme text="Continue Shopping" code="cart.page.continue"/>
                        </button>
                    </div>

                    <cart:saveCart/>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <cart:exportCart/>
    </div>

    <cart:cartItems cartData="${cartData}"/>

    <div class="row">
        <cart:exportCart/>
    </div>
</c:if>
<cart:ajaxCartTopTotalSection/>
