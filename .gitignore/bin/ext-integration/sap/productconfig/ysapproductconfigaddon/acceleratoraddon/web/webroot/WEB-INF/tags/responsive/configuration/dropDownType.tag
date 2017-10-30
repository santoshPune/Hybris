<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="config" tagdir="/WEB-INF/tags/addons/ysapproductconfigaddon/responsive/configuration"%>
<%@ taglib prefix="cssConf" uri="sapproductconfig.tld"%>

<%@ attribute name="cstic" required="true" type="de.hybris.platform.sap.productconfig.facades.CsticData"%>
<%@ attribute name="groupType" required="true" type="de.hybris.platform.sap.productconfig.facades.GroupType"%>

<%@ attribute name="csticKey" required="true" type="java.lang.String"%>
<%@ attribute name="pathPrefix" required="true" type="java.lang.String"%>
<%@ attribute name="valueStyle" required="false" type="java.lang.String"%>

<div class="cpq-label-config-link-row">
	<config:label cstic="${cstic}" csticKey="${csticKey}" typeSuffix=".ddlb" />
	<config:conflictLinkToConfig groupType="${groupType}" key="${csticKey}" />
</div>
<config:longText cstic="${cstic}" csticKey="${csticKey}" pathPrefix="${pathPrefix}" />
<config:csticImage cstic="${cstic}" csticKey="${csticKey}" />
<config:csticErrorMessages key="${csticKey}" groupType="${groupType}"
	bindResult="${requestScope['org.springframework.validation.BindingResult.config']}"
	path="${pathPrefix}value" />
<config:csticErrorMessages key="${csticKey}.additionalValue"
	groupType="${groupType}"
	bindResult="${requestScope['org.springframework.validation.BindingResult.config']}"
	path="${pathPrefix}additionalValue" />
	
<div class="${cssConf:valueStyleClass(cstic)} ${valueStyle}">
	<form:select id="${csticKey}.ddlb" class="form-control"
		path="${pathPrefix}value">
		<c:if test="${empty cstic.value}">
			<form:option value="NULL_VALUE"
				class="${cssConf:valueStyleClass(cstic)}">
				<spring:message code="sapproductconfig.ddlb.select.text"
					text="Please select" />
			</form:option>
		</c:if>
		<c:forEach var="value" items="${cstic.domainvalues}"
			varStatus="status">
			<c:set var="optionText" value="${value.langdepname}" />
			<c:if test="${value.deltaPrice.formattedValue ne '-'}">
				<c:choose>
					<c:when test="${value.deltaPrice.value.unscaledValue() == 0}">
						<spring:message code="sapproductconfig.deltaprcices.included"
							text="Included" var="formattedPrice" />
					</c:when>
					<c:otherwise>
						<c:set value="${value.deltaPrice.formattedValue}"
							var="formattedPrice" />
					</c:otherwise>
				</c:choose>
				<c:set var="optionText" value="${optionText}  [${formattedPrice}]" />
			</c:if>
			<form:option id="${csticKey}.${value.key}.option"
				class="${cssConf:valueStyleClass(cstic)}" value="${value.name}"
				label="${optionText}" />
		</c:forEach>
	</form:select>
</div>

<c:if test="${cstic.type == 'DROPDOWN_ADDITIONAL_INPUT'}">
	<config:additionalValue cstic="${cstic}" csticKey="${csticKey}" pathPrefix="${pathPrefix}" />
</c:if>
