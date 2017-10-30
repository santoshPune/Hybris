<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="entry" required="true" type="de.hybris.platform.commercefacades.order.data.OrderEntryData"%>
<%@ attribute name="index" required="true" type="java.lang.Integer"%>
<%@ attribute name="targetUrl" required="true" type="java.lang.String" %>
<%@ attribute name="styleClass" required="false" type="java.lang.String"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<div id="ajaxGrid${index}" class="${styleClass}"></div>
<c:if test="${entry.product.multidimensional}">
	<c:forEach items="${entry.entries}" var="currentEntry" varStatus="stat">
		<c:set var="subEntries" value="${stat.first ? '' : subEntries}${currentEntry.product.code}:${currentEntry.quantity},"/>
		<c:set var="productName" value="${currentEntry.product.name}"/>
	</c:forEach>

	<div style="display:none" id="grid${index}" data-sub-entries="${subEntries}" 
		data-product-name="${fn:escapeXml(productName)}" data-target-url="${targetUrl}"></div>
</c:if>
