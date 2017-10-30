<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<jsp:useBean id="cons"
		class="de.hybris.platform.sap.productconfig.frontend.util.impl.ConstantHandler"
		scope="session" />

<%@ attribute name="cstic" required="true" type="de.hybris.platform.sap.productconfig.facades.overview.CharacteristicValue"%>

<div class="cpq-overview-cstic-row">
	<div class="col-xs-9">
		<div class="cpq-overview-cstic-value">
			<c:choose>
				<c:when test="${cstic.value eq cons.getNotImplemented()}">
					<spring:message
					code="sapproductconfig.cstic.type.notimplemented"
					text="Not implemented" />
				</c:when>
				<c:otherwise>
					<c:out value="${cstic.value}" />
				</c:otherwise>
			</c:choose>			
		</div>								
		<div class="cpq-overview-cstic-label hidden-xs hidden-sm">
			<label>${cstic.characteristic}</label>
		</div>			
		<div class="cpq-overview-cstic-label visible-xs visible-sm">
			<label>${cstic.characteristic}</label>
		</div>
	</div>
	
	<div class="col-xs-3">
		<div class="cpq-overview-cstic-price">
			<c:if test="${not empty cstic.priceDescription}">
				<c:out value="${cstic.priceDescription}" />
			</c:if>
		</div>
	</div>
</div>