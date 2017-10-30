<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<ul>
	<li>${fn:escapeXml(address.fullnameWithTitle)}</li>
	<c:choose>
		<c:when test="${fn:toUpperCase(address.country.isocode) eq 'CN'}">
			<li>${fn:escapeXml(address.line1)}&nbsp;${fn:escapeXml(address.line2)}</li>
			<li>${fn:escapeXml(address.city.name)}</li>
			<li>${fn:escapeXml(address.district.name)}</li>
			<li>${fn:escapeXml(address.region.name)}</li>
			<li>${fn:escapeXml(address.country.name)}</li>
            <li>${fn:escapeXml(address.cellphone)}</li>
		</c:when>
		<c:otherwise>
			<li>${fn:escapeXml(address.line1)}</li>
			<li>${fn:escapeXml(address.line2)}</li>
			<li>${fn:escapeXml(address.town)}</li>
			<li>${fn:escapeXml(address.region.name)}</li>
			<li>${fn:escapeXml(address.postalCode)}</li>
			<li>${fn:escapeXml(address.country.name)}</li>
            <li>${fn:escapeXml(address.phone)}</li>
		</c:otherwise>
	</c:choose>
</ul>
