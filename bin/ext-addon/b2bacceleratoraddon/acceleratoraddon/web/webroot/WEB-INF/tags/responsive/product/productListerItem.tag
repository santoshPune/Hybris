<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ attribute name="isOrderForm" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:url value="${product.url}" var="productUrl"/>
<c:set value="${not empty product.potentialPromotions}" var="hasPromotion"/>
<c:set var="showEditableGridClass" value=""/>

<c:if test="${not empty isOrderForm && isOrderForm}">
	<li class="item-list-item${hasPromotion ? ' productListItemPromotion' : ''}">
	    <ycommerce:testId code="test_searchPage_wholeProduct">
	
	        <%-- product image --%>
	        <div class="item-image">
	            <a href="${productUrl}" title="${product.name}">
	                <product:productPrimaryImage product="${product}" format="thumbnail"/>
	                <c:if test="${not empty product.potentialPromotions and not empty product.potentialPromotions[0].productBanner}">
	                    <img class="promo" src="${product.potentialPromotions[0].productBanner.url}"
	                         alt="${product.potentialPromotions[0].description}"
	                         title="${product.potentialPromotions[0].description}"/>
	                </c:if>
	            </a>
	        </div>
	
	        <%-- product name, code, promotions --%>
	        <div class="item-info">
	            <ycommerce:testId code="searchPage_productName_link_${product.code}">
	                <a href="${productUrl}" title="${product.name}">
	                    <div class="item-name">${product.name}</div>
	                </a>
	            </ycommerce:testId>
	
	            <div class="item-code">
	            	${product.code}
	            </div>
	
	            <%-- Future Availability --%>            
	            <div class="productFutureAvailability">
	                <product:productFutureAvailability product="${product}" futureStockEnabled="${futureStockEnabled}"/>
	            </div>            
	        </div>
	
	        <%-- price --%>
	        <div class="item-price">
	            <ycommerce:testId code="searchPage_price_label_${product.code}">
	                <span class="visible-xs visible-sm"><spring:theme code="basket.page.itemPrice"/>: </span>
	                <product:productListerItemPrice product="${product}"/>
	            </ycommerce:testId>
	        </div>
	
	        <%-- description --%>
	        <div class="item-description">            
	            <c:if test="${not empty product.summary}">
	                ${product.summary}
	            </c:if>
	            <product:productListerClassifications product="${product}"/>
	        </div>
	
	        <%-- quantity --%>
	        <div class="item-quantity">                                         	                      	        
	        	<c:choose>
	        		<c:when test="${product.stock.stockLevelStatus.code eq 'outOfStock' }">			        	
		            	<spring:theme code="product.grid.outOfStock" />				               				        
		        	</c:when>
		        	<c:otherwise>
			            <label class="visible-xs visible-sm"><spring:theme code="basket.page.qty"/>:</label>
			            <span data-variant-price="${product.price.value}" id="productPrice[${sessionScope.skuIndex}]"
			                    class="price hidden">${product.price.value}</span>
			            <input type=hidden id="productPrice[${sessionScope.skuIndex}]"
			                    value="${product.price.value}"/>
			            <input type="hidden" class="${product.code} sku"
			                    name="cartEntries[${sessionScope.skuIndex}].sku"
			                    id="cartEntries[${sessionScope.skuIndex}].sku" value="${product.code}"/>
			            <input type="text" maxlength="3" size="1"
			                    id="cartEntries[${sessionScope.skuIndex}].quantity"
			                    name="cartEntries[${sessionScope.skuIndex}].quantity"
			                    data-product-selection='{"product":${product.code}}'
			                    class="sku-quantity form-control" value="0">
			            <c:set var="skuIndex" scope="session" value="${sessionScope.skuIndex + 1}"/>
					</c:otherwise>
	           	</c:choose>     
	        </div>
	
	        <%-- total --%>
	        <div class="item-total">
	        </div>
	    </ycommerce:testId>
	</li>
</c:if>