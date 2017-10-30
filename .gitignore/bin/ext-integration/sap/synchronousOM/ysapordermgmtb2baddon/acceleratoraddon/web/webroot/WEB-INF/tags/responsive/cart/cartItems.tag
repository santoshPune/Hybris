<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="grid" tagdir="/WEB-INF/tags/responsive/grid" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/responsive/storepickup" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>


 <ul class="item-list cart-list">
    <li class="hidden-xs hidden-sm">
        <ul class="item-list-header">
            <li class="item-toggle"></li>
            <li class="item-image"></li>
            <li class="item-info"><spring:theme code="basket.page.item"/></li>
            <li class="item-price"><spring:theme code="basket.page.price"/></li>
            <li class="item-quantity"><spring:theme code="basket.page.qty"/></li>
            <li class="item-total-column"><spring:theme code="basket.page.total"/></li>
            <li class="item-remove"></li>
        </ul>
    </li>

    <c:forEach items="${cartData.entries}" var="entry" varStatus="loop">
        <c:set var="showEditableGridClass" value=""/>
        <c:url value="${entry.product.url}" var="productUrl"/>
    
        <li class="item-list-item">
            <!-- chevron for multi-d products -->
            <div class="hidden-xs hidden-sm item-toggle">
                <c:if test="${entry.product.multidimensional}" >
                    <div class="js-show-editable-grid" data-index="${loop.index}" data-read-only-multid-grid="${not entry.updateable}">
                        <ycommerce:testId code="cart_product_updateQuantity">
                            <span class="glyphicon glyphicon-chevron-down"></span>
                        </ycommerce:testId>
                    </div>
                </c:if>
            </div>

            <!-- product image -->
            <div class="item-image">
                <a href="${productUrl}"><product:productPrimaryImage product="${entry.product}" format="thumbnail"/></a>
            </div>

            <!-- product name, code, promotions -->
            <div class="item-info">
                <ycommerce:testId code="cart_product_name">
                    <a href="${productUrl}"><span class="item-name">${entry.product.name}</span></a>
                </ycommerce:testId>

                <div class="item-code">${entry.product.code}</div>

                <!-- availability -->
                <div class="item-stock">
                    <c:set var="entryStock" value="${entry.product.stock.stockLevelStatus.code}"/>
                    <c:forEach items="${entry.product.baseOptions}" var="option">
                        <c:if test="${not empty option.selected and option.selected.url eq entry.product.url}">
                            <c:forEach items="${option.selected.variantOptionQualifiers}" var="selectedOption">
                                <div>
                                    <strong>${selectedOption.name}:</strong>
                                    <span>${selectedOption.value}</span>
                                </div>
                                <c:set var="entryStock" value="${option.selected.stock.stockLevelStatus.code}"/>
                            </c:forEach>
                        </c:if>
                    </c:forEach>

                    <div>
                        <c:choose>
                            <c:when test="${not empty entryStock and entryStock ne 'outOfStock' or entry.product.multidimensional}">
                                <span class="stock"><spring:theme code="product.variants.in.stock"/></span>
                            </c:when>
                            <c:otherwise>
                                <span class="out-of-stock"><spring:theme code="product.variants.out.of.stock"/></span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div>
						<spring:theme code="basket.page.availability"/>: 
	                      	<c:if test="${not empty entry.scheduleLines}">
	                      	 	<span class="schedlin">
	                       	 	<c:forEach items="${entry.scheduleLines}" var="scheduleLine">
									${scheduleLine.formattedValue}
									<br>
								</c:forEach>
	                      	 	</span>
						</c:if>
                     </div>
                </div>
                <c:if test="${ycommerce:doesPotentialPromotionExistForOrderEntry(cartData, entry.entryNumber)}">
                    <c:forEach items="${cartData.potentialProductPromotions}" var="promotion">
                        <c:set var="displayed" value="false"/>
                        <c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
                            <c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber && not empty promotion.description}">
                                <c:set var="displayed" value="true"/>

                                    <div class="promo">
                                         <ycommerce:testId code="cart_potentialPromotion_label">
                                             ${promotion.description}
                                         </ycommerce:testId>
                                    </div>
                            </c:if>
                        </c:forEach>
                    </c:forEach>
                </c:if>
                <c:if test="${ycommerce:doesAppliedPromotionExistForOrderEntry(cartData, entry.entryNumber)}">
                    <c:forEach items="${cartData.appliedProductPromotions}" var="promotion">
                        <c:set var="displayed" value="false"/>
                        <c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
                            <c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
                                <c:set var="displayed" value="true"/>
                                <div class="promo">
                                    <ycommerce:testId code="cart_appliedPromotion_label">
                                        ${promotion.description}
                                    </ycommerce:testId>
                                </div>
                            </c:if>
                        </c:forEach>
                    </c:forEach>
                </c:if>
            </div>

            <!-- price -->
            <div class="item-price">
                <span class="visible-xs visible-sm"><spring:theme code="basket.page.itemPrice"/>: </span>
                <format:price priceData="${entry.basePrice}" displayFreeForZero="true"/>
            </div> 
          
          	<!-- quantity -->
            <div class="item-quantity hidden-xs hidden-sm">
                <c:choose>
                    <c:when test="${not entry.product.multidimensional}" >
                        <c:url value="/cart/update" var="cartUpdateFormAction" />
                        <form:form id="updateCartForm${loop.index}" action="${cartUpdateFormAction}" method="post" commandName="updateQuantityForm${entry.entryNumber}"
                                    data-cart='{"cartCode" : "${cartData.code}","productPostPrice":"${entry.basePrice.value}","productName":"${entry.product.name}"}'>
                            <input type="hidden" name="entryNumber" value="${entry.entryNumber}"/>
                            <input type="hidden" name="productCode" value="${entry.product.code}"/>
                            <input type="hidden" name="initialQuantity" value="${entry.quantity}"/>
                            <ycommerce:testId code="cart_product_quantity">
                                <form:label cssClass="visible-xs visible-sm" path="quantity" for="quantity${entry.entryNumber}"></form:label>
                                <form:input cssClass="form-control js-update-entry-quantity-input" disabled="${not entry.updateable}" type="text" size="1" id="quantity_${loop.index}" path="quantity" />
                            </ycommerce:testId>
                        </form:form>
                    </c:when>
                    <c:otherwise>
                        <c:url value="/cart/updateMultiD" var="cartUpdateMultiDFormAction" />
                        <form:form id="updateCartForm${loop.index}" action="${cartUpdateMultiDFormAction}" method="post" commandName="updateQuantityForm${loop.index}">
                            <input type="hidden" name="entryNumber" value="${entry.entryNumber}"/>
                            <input type="hidden" name="productCode" value="${entry.product.code}"/>
                            <input type="hidden" name="initialQuantity" value="${entry.quantity}"/>
                            <label class="visible-xs visible-sm"><spring:theme code="basket.page.qty"/>:</label>
                            <span class="qtyValue"><c:out value="${entry.quantity}" /></span>
                            <%--<input type="text" value="${entry.quantity}" class="form-control qtyValue" name="quantity" readonly>--%>
                            <input type="hidden" name="quantity" value="0"/>
                            <ycommerce:testId code="cart_product_updateQuantity">
                                <div id="QuantityProduct${loop.index}" class="updateQuantityProduct"></div>
                            </ycommerce:testId>
                        </form:form>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- total -->
            <ycommerce:testId code="cart_totalProductPrice_label">
                <div class="item-total js-item-total hidden-xs hidden-sm">
                    <format:price priceData="${entry.totalPrice}" displayFreeForZero="true"/>
                </div>
            </ycommerce:testId>

            <!-- remove icon -->
            <div class="item-remove">
                <c:if test="${entry.updateable}" >
                    <ycommerce:testId code="cart_product_removeProduct">
                        <c:choose>
                            <c:when test="${not entry.product.multidimensional}" >
                                <button class="btn js-remove-entry-button" id="removeEntry_${loop.index}">
                                    <span class="glyphicon glyphicon-remove"></span>
                                </button>
                            </c:when>
                            <c:otherwise>
                                <button class="btn js-submit-remove-product-multi-d" data-index="${loop.index}"  id="removeEntry_${loop.index}">
                                    <span class="glyphicon glyphicon-remove"></span>
                                </button>
                            </c:otherwise>
                        </c:choose>
                    </ycommerce:testId>
                </c:if>
            </div>

            <div class="item-quantity-total visible-xs visible-sm">
                <c:if test="${entry.product.multidimensional}" >
                    <ycommerce:testId code="cart_product_updateQuantity">
                        <c:set var="showEditableGridClass" value="js-show-editable-grid"/>
                    </ycommerce:testId>
                </c:if>
                <div class="details ${showEditableGridClass}" data-index="${loop.index}" data-read-only-multid-grid="${not entry.updateable}">
                    <div class="qty">
                        <c:choose>
                            <c:when test="${not entry.product.multidimensional}" >
                                <c:url value="/cart/update" var="cartUpdateFormAction" />
                                <form:form id="updateCartForm${loop.index}" action="${cartUpdateFormAction}" method="post" commandName="updateQuantityForm${entry.entryNumber}"
                                            data-cart='{"cartCode" : "${cartData.code}","productPostPrice":"${entry.basePrice.value}","productName":"${entry.product.name}"}'>
                                    <input type="hidden" name="entryNumber" value="${entry.entryNumber}"/>
                                    <input type="hidden" name="productCode" value="${entry.product.code}"/>
                                    <input type="hidden" name="initialQuantity" value="${entry.quantity}"/>
                                    <ycommerce:testId code="cart_product_quantity">
                                        <form:label cssClass="" path="quantity" for="quantity${entry.entryNumber}"><spring:theme code="basket.page.qty"/>:</form:label>
                                        <form:input cssClass="form-control js-update-entry-quantity-input" disabled="${not entry.updateable}" type="text" size="1" id="quantity_${loop.index}" path="quantity" />
                                    </ycommerce:testId>
                                </form:form>
                            </c:when>
                            <c:otherwise>
                                <c:url value="/cart/updateMultiD" var="cartUpdateMultiDFormAction" />
                                <form:form id="updateCartForm${loop.index}" action="${cartUpdateMultiDFormAction}" method="post" commandName="updateQuantityForm${loop.index}">
                                    <input type="hidden" name="entryNumber" value="${entry.entryNumber}"/>
                                    <input type="hidden" name="productCode" value="${entry.product.code}"/>
                                    <input type="hidden" name="initialQuantity" value="${entry.quantity}"/>
                                    <label><spring:theme code="basket.page.qty"/>:</label>
                                    <span class="qtyValue"><c:out value="${entry.quantity}" /></span>
                                    <%--<input type="text" value="${entry.quantity}" class="form-control qtyValue" name="quantity" readonly>--%>
                                    <input type="hidden" name="quantity" value="0"/>
                                    <ycommerce:testId code="cart_product_updateQuantity">
                                        <div id="QuantityProduct${loop.index}" class="updateQuantityProduct"></div>
                                    </ycommerce:testId>
                                </form:form>
                            </c:otherwise>
                        </c:choose>
                        <c:if test="${entry.product.multidimensional}" >
                            <ycommerce:testId code="cart_product_updateQuantity">
                                <span class="glyphicon glyphicon-chevron-right"></span>
                            </ycommerce:testId>
                        </c:if>
                        <ycommerce:testId code="cart_totalProductPrice_label">
                            <div class="item-total">
                                <format:price priceData="${entry.totalPrice}" displayFreeForZero="true"/>
                            </div>
                        </ycommerce:testId>
                        
                        <c:if test="${entry.configurationAttached}">
					      	<div class="cpq-cart-config">
						   		<spring:url	value="${entry.itemPK}/ ${entry.product.code} /configCartEntry"	var="configUrl"></spring:url>
								<br><a href="${configUrl}"><spring:theme code="configure.product.link"/></a>
					        </div>
					    </c:if>
                    </div>
                </div>
            </div>
        </li>
        
        <li>
        	<spring:url value="/cart/getProductVariantMatrix" var="targetUrl"/>
			<grid:gridWrapper entry="${entry}" index="${loop.index}" styleClass="add-to-cart-order-form-wrap display-none" 
				targetUrl="${targetUrl}"/>
		</li>
    </c:forEach>
</ul>

<product:productOrderFormJQueryTemplates />
<storepickup:pickupStorePopup />
