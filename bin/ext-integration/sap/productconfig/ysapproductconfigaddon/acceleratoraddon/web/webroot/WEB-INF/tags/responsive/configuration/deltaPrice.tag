<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="config" tagdir="/WEB-INF/tags/addons/ysapproductconfigaddon/responsive/configuration"%>

<%@ attribute name="deltaPrice" required="true" type="de.hybris.platform.commercefacades.product.data.PriceData"%>
<%@ attribute name="csticKey" required="true" type="java.lang.String"%>

<c:if test="${deltaPrice.formattedValue ne '-'}">
	<c:choose>
		<c:when test="${deltaPrice.value.unscaledValue() == 0}">
			<spring:message code="sapproductconfig.deltaprcices.included" text="Included" var="formattedPrice" />
		</c:when>
		<c:otherwise>
			<c:set value="${deltaPrice.formattedValue}" var="formattedPrice" />
		</c:otherwise>
	</c:choose>
	<div id="${csticKey}.deltaPrice" title="${formattedPrice}"
		class="cpq-csticValueDeltaPrice cpq-csticValueLabel">${formattedPrice}</div>
</c:if>