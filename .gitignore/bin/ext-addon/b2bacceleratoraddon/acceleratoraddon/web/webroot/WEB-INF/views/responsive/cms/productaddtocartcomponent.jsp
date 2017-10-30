<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="b2b-product" tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/responsive/product" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<c:choose> 
  <c:when test="${product.stock.stockLevelStatus.code eq 'inStock' and empty product.stock.stockLevel}">
    <c:set var="maxQty" value="FORCE_IN_STOCK"/>
  </c:when>
  <c:otherwise>
    <c:set var="maxQty" value="${product.stock.stockLevel}"/>
  </c:otherwise>
</c:choose>
		
<div class="addtocart-component">
    <c:if test="${empty showAddToCart ? true : showAddToCart}">
        <div class="qty-selector input-group js-qty-selector">
			<span class="input-group-btn">
				<button class="btn btn-default js-qty-selector-minus" type="button"><span class="glyphicon glyphicon-minus" aria-hidden="true"></span></button>
			</span>
            <input type="text" maxlength="3" class="form-control js-qty-selector-input" size="1" value='1' data-max="${maxQty}" data-min="1" name="pdpAddtoCartInput"  id="pdpAddtoCartInput"  />
			<span class="input-group-btn">
				<button class="btn btn-default js-qty-selector-plus" type="button"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span></button>
			</span>
        </div>
    </c:if>

    <div class="stock-wrapper clearfix">
        <ycommerce:testId code="productDetails_productInStock_label">
            <b2b-product:productStockThreshold product="${product}"/>
        </ycommerce:testId>
        <product:productFutureAvailability product="${product}" futureStockEnabled="${futureStockEnabled}" />
    </div>

    <div class="actions">
        <c:if test="${multiDimensionalProduct}" >
                <c:url value="${product.url}/orderForm" var="productOrderFormUrl"/>
                <a href="${productOrderFormUrl}" class="btn btn-default btn-block btn-icon js-add-to-cart glyphicon-list-alt">
                    <spring:theme code="order.form" />
                </a>
        </c:if>
        <action:actions element="div"  parentComponent="${component}"/>
    </div>
</div>