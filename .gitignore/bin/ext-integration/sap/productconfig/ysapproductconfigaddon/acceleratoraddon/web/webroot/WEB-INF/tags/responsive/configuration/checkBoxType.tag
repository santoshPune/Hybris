<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="config" tagdir="/WEB-INF/tags/addons/ysapproductconfigaddon/responsive/configuration"%>
<%@ taglib prefix="cssConf" uri="sapproductconfig.tld"%>

<%@ attribute name="cstic" required="true" type="de.hybris.platform.sap.productconfig.facades.CsticData"%>
<%@ attribute name="groupType" required="true" type="de.hybris.platform.sap.productconfig.facades.GroupType"%>

<%@ attribute name="csticKey" required="true" type="java.lang.String"%>
<%@ attribute name="pathPrefix" required="true" type="java.lang.String"%>
<%@ attribute name="valueStyle" required="false" type="java.lang.String"%>

<div class="cpq-label-config-link-row">
	<config:label cstic="${cstic}" csticKey="${csticKey}" typeSuffix=".checkBox" />
	<config:conflictLinkToConfig groupType="${groupType}" key="${csticKey}" />
</div>
<config:longText cstic="${cstic}" csticKey="${csticKey}" pathPrefix="${pathPrefix}" />
<config:csticImage cstic="${cstic}" csticKey="${csticKey}" />
<config:csticErrorMessages key="${csticKey}" groupType="${groupType}"
	bindResult="${requestScope['org.springframework.validation.BindingResult.config']}" path="${pathPrefix}value" />

<div class="${cssConf:valueStyleClass(cstic)} ${valueStyle}">
	<div class="checkbox">
		<form:checkbox class="${cssConf:valueStyleClass(cstic)} cpq-csticValueSelect cpq-csticValueSelect-single"
			id="${csticKey}.checkBox" path="${pathPrefix}domainvalues[0].selected" value="${cstic.domainvalues[0].selected}" />
		<c:if test="${cstic.domainvalues[0].deltaPrice.formattedValue ne '-'}">
			<c:choose>
				<c:when test="${cstic.domainvalues[0].deltaPrice.value.unscaledValue() == 0}">
					<spring:message code="sapproductconfig.deltaprcices.included" text="Included" var="formattedPrice" />
				</c:when>
				<c:otherwise>
					<c:set value="${cstic.domainvalues[0].deltaPrice.formattedValue}" var="formattedPrice" />
				</c:otherwise>
			</c:choose>
			<div id="${csticKey}.deltaPrice" title="${formattedPrice}" class="cpq-csticValueDeltaPrice cpq-csticValueLabel">${formattedPrice}</div>
		</c:if>
	</div>
</div>

