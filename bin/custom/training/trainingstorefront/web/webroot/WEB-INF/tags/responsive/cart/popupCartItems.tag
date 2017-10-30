<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ attribute name="entry" required="true" type="de.hybris.platform.commercefacades.order.data.OrderEntryData" %>
<%@ attribute name="quantity" required="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>

    <div class="add-to-cart-item">
		<c:url value="${product.url}" var="entryProductUrl"/>
		<div class="thumb">
			<a href="${entryProductUrl}">
				<product:productPrimaryImage product="${entry.product}" format="cartIcon"/>
			</a>
		</div>
		<div class="details">
			<a class="name" href="${entryProductUrl}"><c:out value="${product.name}" /></a>
			<div class="qty"><span><spring:theme code="popup.cart.quantity.added"/></span>&nbsp;${quantity}</div>
			<c:forEach items="${product.baseOptions}" var="baseOptions">
				<c:forEach items="${baseOptions.selected.variantOptionQualifiers}" var="baseOptionQualifier">
					<c:if test="${baseOptionQualifier.qualifier eq 'style' and not empty baseOptionQualifier.image.url}">
						<div class="itemColor">
							<span class="label"><spring:theme code="product.variants.colour"/></span>
							<img src="${baseOptionQualifier.image.url}"  alt="${baseOptionQualifier.value}" title="${baseOptionQualifier.value}"/>
						</div>
					</c:if>
					<c:if test="${baseOptionQualifier.qualifier eq 'size'}">
						<div class="itemSize">
							<span class="label"><spring:theme code="product.variants.size"/></span>
								${baseOptionQualifier.value}
						</div>
					</c:if>
				</c:forEach>
			</c:forEach>
			<c:if test="${not empty entry.deliveryPointOfService.name}">
				<div class="itemPickup"><span class="itemPickupLabel"><spring:theme code="popup.cart.pickup"/></span>&nbsp;${entry.deliveryPointOfService.name}</div>
			</c:if>
			<div class="price"><format:price priceData="${entry.basePrice}"/></div>
		</div>
    </div>