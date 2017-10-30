<%@ taglib prefix="address"
	tagdir="/WEB-INF/tags/addons/chineseaddressaddon/responsive/address"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<span>
<span id="fullName">${fn:escapeXml(deliveryAddress.fullnameWithTitle)}</span>
					<br>
					<c:choose>
						<c:when test="${fn:toUpperCase(deliveryAddress.country.isocode) eq 'CN'}">
							<c:if test="${not empty deliveryAddress.country.name}">
								${fn:escapeXml(deliveryAddress.country.name)},&nbsp;
							</c:if>
							<c:if test="${ not empty deliveryAddress.region.name }">
								${fn:escapeXml(deliveryAddress.region.name)},&nbsp;
							</c:if>
							<c:if test="${not empty deliveryAddress.city.name}">
								${fn:escapeXml(deliveryAddress.city.name)},&nbsp;
							</c:if>
							<c:if test="${not empty deliveryAddress.district.name}">
								${fn:escapeXml(deliveryAddress.district.name)},&nbsp;
							</c:if>						
							<c:if test="${ not empty deliveryAddress.line1 }">
								${fn:escapeXml(deliveryAddress.line1)}
							</c:if>
							<c:if test="${ not empty deliveryAddress.line2 }">
								,&nbsp;${fn:escapeXml(deliveryAddress.line2)}
							</c:if>						
							<c:if test="${ not empty deliveryAddress.postalCode }">
								,&nbsp;${fn:escapeXml(deliveryAddress.postalCode)}
							</c:if>
							<c:if test="${not empty deliveryAddress.cellphone }">
								,&nbsp;${fn:escapeXml(deliveryAddress.cellphone)}
							</c:if>
						</c:when>
						<c:otherwise>
							<c:if test="${ not empty deliveryAddress.line1 }">
								${fn:escapeXml(deliveryAddress.line1)},&nbsp;
							</c:if>
							<c:if test="${ not empty deliveryAddress.line2 }">
								${fn:escapeXml(deliveryAddress.line2)},&nbsp;
							</c:if>
							<c:if test="${not empty deliveryAddress.town }">
								${fn:escapeXml(deliveryAddress.town)},&nbsp;
							</c:if>
							<c:if test="${ not empty deliveryAddress.region.name }">
								${fn:escapeXml(deliveryAddress.region.name)},&nbsp;
							</c:if>
							<c:if test="${ not empty deliveryAddress.country.name }">
								${fn:escapeXml(deliveryAddress.country.name)}
							</c:if>
							<c:if test="${ not empty deliveryAddress.postalCode }">
								,&nbsp;${fn:escapeXml(deliveryAddress.postalCode)}
							</c:if>
							<c:if test="${not empty deliveryAddress.phone }">
								,&nbsp;${fn:escapeXml(deliveryAddress.phone)}
							</c:if>
						</c:otherwise>	
					</c:choose>

</span>