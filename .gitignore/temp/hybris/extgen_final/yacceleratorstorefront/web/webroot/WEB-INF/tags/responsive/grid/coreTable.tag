<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="variants" required="true" type="java.util.List"%>
<%@ attribute name="inputTitle" required="true" type="java.lang.String"%>
<%@ attribute name="loopIndex" required="true" type="java.lang.Integer"%>
<%@ attribute name="readOnly" required="false" type="java.lang.Boolean"%>
<%@ attribute name="product" required="false" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ attribute name="skusId" required="true" type="java.lang.String"%>
<%@ attribute name="firstVariant" required="true" type="de.hybris.platform.commercefacades.product.data.VariantMatrixElementData" %>
<%@ attribute name="showName" required="false" type="java.lang.Boolean"%>
<%@ attribute name="showLastDimensionTitle" required="false" type="java.lang.Boolean"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="grid" tagdir="/WEB-INF/tags/responsive/grid" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>

<c:if test="${not empty sessionScope.skuIndex}">
	<c:set var="loopIndex" value="${sessionScope.skuIndex}"/>
</c:if>

<c:set var="inputDisabled" value=""/>

<c:if test="${readOnly == true}">
    <c:set var="inputDisabled" value="disabled"/>
</c:if>

<tr class="hidden-size">
    <th>${firstVariant.parentVariantCategory.name}
        <c:if test="${firstVariant.elements ne null and firstVariant.elements.size() > 0}">
            /${firstVariant.elements[0].parentVariantCategory.name}
        </c:if>
    </th>
    <c:forEach items="${variants}" var="variant">
        <th>${variant.variantValueCategory.name}</th>
    </c:forEach>
</tr>

<tr>
    <td class="variant-detail" data-variant-property="${variants[0].parentVariantCategory.name}">
        <product:productImage product="${product}" code="${skusId}" format="cartIcon"/>
        <div class="description">
            <c:if test="${showName == true}">
                <div >${fn:escapeXml(product.name)} - ${firstVariant.variantValueCategory.name}</div>
            </c:if>
            <c:if test="${showName != true}">
                <div>${firstVariant.variantValueCategory.name}</div>
            </c:if>
            <c:if test="${showLastDimensionTitle == true}">
                ${inputTitle}
            </c:if>
        </div>
    </td>

    <c:forEach items="${variants}" var="variant">
        <c:set var="cssStockClass" value=""/>
        <c:if test="${variant.variantOption.stock.stockLevel <= 0}">
            <c:set var="cssStockClass" value="out-of-stock"/>
        </c:if>

        <td class="${cssStockClass} widthReference hidden-xs">

            <div class="variant-prop hidden-sm hidden-md hidden-lg" data-variant-prop="${variant.variantValueCategory.name}">
                <span>${variants[0].parentVariantCategory.name}:</span>
                ${variant.variantValueCategory.name}
            </div>

            <span class="price" data-variant-price="${variant.variantOption.priceData.value}">
                <c:set var="disableForOutOfStock" value="${inputDisabled}"/>
                <format:price priceData="${variant.variantOption.priceData}"/>
            </span>
            <input type=hidden id="productPrice[${loopIndex}]" value="${variant.variantOption.priceData.value}" />

            <c:if test="${variant.variantOption.stock.stockLevel == 0}">
                <c:set var="disableForOutOfStock" value="disabled"/>
            </c:if>

            <input type="hidden" class="${skusId} sku" name="cartEntries[${loopIndex}].sku" id="cartEntries[${loopIndex}].sku" value="${variant.variantOption.code}" />
            <br/>
            <input type="textbox" maxlength="3" class="sku-quantity" data-variant-id="variant_${loopIndex}" name="cartEntries[${loopIndex}].quantity" data-product-selection='{"product":"${variant.variantOption.code}"}' id="cartEntries[${loopIndex}].quantity" value="0" ${disableForOutOfStock} data-parent-id="${product.code}"/>

            <grid:coreTableStockRow variant="${variant}" />

            <span class="data-grid-total" data-grid-total-id="total_value_${loopIndex}"></span>

            <c:set var="loopIndex" value="${loopIndex +1}"/>
            <c:set var="skuIndex" scope="session" value="${sessionScope.skuIndex + 1}"/>
        </td>
    </c:forEach>

    <td class="mobile-cart-actions hide">
        <a href="#" class="btn btn-primary closeVariantModal"><spring:theme code="popup.done"/></a>
    </td>

    <td class="variant-select hidden-sm hidden-md hidden-lg">
        <a href="#" class="variant-select-btn">
            <span class="selectSize"><spring:theme code="product.grid.selectSize"/>&nbsp;${variants[0].parentVariantCategory.name}</span>
            <span class="editSize"><spring:theme code="product.grid.editSize"/></span>
        </a>
    </td>
</tr>