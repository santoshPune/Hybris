<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="config" tagdir="/WEB-INF/tags/addons/ysapproductconfigaddon/responsive/configuration"%>
<%@ taglib prefix="cssConf" uri="sapproductconfig.tld"%>

<%@ attribute name="cstic" required="true" type="de.hybris.platform.sap.productconfig.facades.CsticData"%>
<%@ attribute name="groupType" required="true" type="de.hybris.platform.sap.productconfig.facades.GroupType"%>

<%@ attribute name="csticKey" required="true" type="java.lang.String"%>
<%@ attribute name="pathPrefix" required="true" type="java.lang.String"%>
<%@ attribute name="valueStyle" required="false" type="java.lang.String"%>

<div class="cpq-label-config-link-row">
	<config:label cstic="${cstic}" csticKey="${csticKey}" typeSuffix=".readOnly" />
	<config:conflictLinkToConfig groupType="${groupType}" key="${csticKey}" />
</div>
<config:longText cstic="${cstic}" csticKey="${csticKey}" pathPrefix="${pathPrefix}" />
<config:csticImage cstic="${cstic}" csticKey="${csticKey}" />
<config:csticErrorMessages key="${csticKey}" groupType="${groupType}"
	bindResult="${requestScope['org.springframework.validation.BindingResult.config']}" path="${pathPrefix}value" />

<div id="${csticKey}.readOnly" class="${cssConf:valueStyleClass(cstic)} ${valueStyle}">
	<c:choose>
		<c:when test="${fn:length(cstic.domainvalues) gt 0}">
			<c:forEach var="value" items="${cstic.domainvalues}">
				<c:if test="${value.selected}">
					<div class="cpq-csticValueLabelWithoutSelect">
						<c:out value="${value.langdepname}" />
						<config:deltaPrice deltaPrice="${value.deltaPrice}" csticKey="${csticKey}.${value.key}" />		
					</div>
				</c:if>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<div class="cpq-csticValueLabelWithoutSelect">
				<c:out value="${cstic.value}" />
			</div>
		</c:otherwise>
	</c:choose>
</div>
<config:retractAction groupType="${groupType}" key="${csticKey}" pathPrefix="${pathPrefix}" />
