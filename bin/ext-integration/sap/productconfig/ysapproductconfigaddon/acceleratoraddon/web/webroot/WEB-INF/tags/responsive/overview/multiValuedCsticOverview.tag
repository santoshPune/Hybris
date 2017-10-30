<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ attribute name="cstic" required="true" type="de.hybris.platform.sap.productconfig.facades.overview.CharacteristicValue"%>

<c:if test="${cstic.valuePositionType eq 'FIRST'}">
	<div class="cpq-overview-cstic-row">
</c:if>
	<div class="cpq-overview-multi-valued-cstic-row">
		<div class="col-xs-9">
			<div class="cpq-overview-cstic-value">
				<c:out value="${cstic.value}" />
			</div>			
			<c:if test="${cstic.valuePositionType eq 'FIRST'}">
			<div class="cpq-overview-cstic-label hidden-xs hidden-sm">
				<label>${cstic.characteristic}</label>
			</div>
		</c:if>
		<c:if test="${cstic.valuePositionType eq 'LAST'}">
			<div class="cpq-overview-cstic-label visible-xs visible-sm">
				<label>${cstic.characteristic}</label>
			</div>
		</c:if>
		</div>		
		<div class="col-xs-3">
			<div class="cpq-overview-cstic-price">
				<c:if test="${not empty cstic.priceDescription}">
					<c:out value="${cstic.priceDescription}" />
				</c:if>
			</div>
		</div>
	</div>
<c:if test="${cstic.valuePositionType eq 'LAST'}">
	</div>
</c:if>
