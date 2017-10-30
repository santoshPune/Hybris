<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:if test="${not config.consistent or not config.complete}">
	<div class="cpq-overview-error-panel">
		<div class="cpq-overview-error-sign">&#xe101;</div>
		<div class="cpq-overview-error-message">
			<span><spring:message code="sapproductconfig.cart.entrytext.conflicts.responsive"
					text="{0} issues must be resolved before checkout" arguments="${errorCount}" />&nbsp; <spring:url
					value="${config.cartItemPK}/ ${config.kbKey.productCode}/configCartEntry" var="resolveConfigUrl"></spring:url> <a href="${resolveConfigUrl}"><spring:message
						code="sapproductconfig.addtocart.resolve.button" text="Resolve Issues Now" /></a> </span>
		</div>
	</div>
</c:if>

<spring:url value="/${product.code}/config" var="configUrl" />
<div class="back-link border">
	<button type="button" class="cpq-back-button" data-back-to-config="${configUrl}">
		<span class="glyphicon glyphicon-chevron-left"></span>
	</button>
	<span class="label"><spring:theme code="sapproductconfig.overview.title" text="Review your Selections (Default)" /></span>
</div>
