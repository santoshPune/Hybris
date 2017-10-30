<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="config" tagdir="/WEB-INF/tags/addons/ysapproductconfigaddon/responsive/configuration"%>
<%@ taglib prefix="cssConf" uri="sapproductconfig.tld"%>

<%@ attribute name="group" required="true" type="de.hybris.platform.sap.productconfig.facades.UiGroupData"%>

<%@ attribute name="pathPrefix" required="true" type="java.lang.String"%>
<%@ attribute name="hideGroupTitle" required="false" type="java.lang.Boolean"%>
<%@ attribute name="hideExpandCollapse" required="false" type="java.lang.Boolean"%>

<c:if test="${!hideGroupTitle}">
	<jsp:useBean id="cons"
		class="de.hybris.platform.sap.productconfig.frontend.util.impl.ConstantHandler"
		scope="session" />
	<jsp:useBean id="uiSupport"
		class="de.hybris.platform.sap.productconfig.frontend.util.impl.ConfigUISupport"
		scope="session" />
	<div tabindex="0" id="${group.id}_title"
		class="${cssConf:groupStyleClasses(group, hideExpandCollapse)}">

		<c:choose>
			<c:when test="${group.name eq cons.getGeneralGroupName()}">
				<spring:message code="sapproductconfig.group.general.title"
					text="General (Default)" var="groupTitle" />
			</c:when>
			<c:when test="${group.groupType eq 'CONFLICT'}">
				<spring:message code="sapproductconfig.group.conflict.prefix" text="Conflict for {0}" var="groupTitle" arguments="${group.name}" />
			</c:when>
			<c:otherwise>
				<c:set var="groupTitle" value="${group.description}" />
			</c:otherwise>
		</c:choose>

		<div id="cpqGroupTitle" class="cpq-group-title"
			title="${groupTitle}">
			<c:out value="${groupTitle}" />
		</div>
		<c:set var="groupTooltipKey" value="${cssConf:groupStatusTooltipKey(group)}" />
		<c:choose>
			<c:when test="${groupTooltipKey eq ''}">
				<c:set var="groupTooltip" value="" />
			</c:when>
			<c:otherwise>
				<spring:message code="${groupTooltipKey}" var="groupTooltip" />
				<c:set var="groupTooltip" value="title=\"${groupTooltip}\"" />
			</c:otherwise>
		</c:choose>		
		<div id="cpqGroupStatusIcon" class="cpq-status-icon" ${groupTooltip} >
			<c:if test="${group.numberErrorCstics != 0}">${group.numberErrorCstics}</c:if>
		</div>		
	</div>
</c:if>

<c:if test="${!group.collapsed}">
	<c:if test="${group.groupType ne 'CONFLICT_HEADER'}">
		<div class="cpq-group">	
			<config:conflictMessage group="${group}" />
			<c:set value="${fn:length(group.cstics)}" var="noOfsugg" />
			<c:if test="${uiSupport.hasRequiredCstic(group.cstics)}">
				<config:legend/>
			</c:if>	
			
			<c:forEach var="cstic" items="${group.cstics}"
				varStatus="csticStatus">				
				<c:set value="${pathPrefix}cstics[${csticStatus.index}]." var="path" />
				<config:cstics cstic="${cstic}" groupType="${group.groupType}"
					pathPrefix="${path}" csticNo="${csticStatus.index+1}" numCstic="${noOfsugg}" />
			</c:forEach>
			<form:input type="hidden" path="${pathPrefix}id" />
		</div>
	</c:if>

	<c:if test="${group.groupType eq 'CONFLICT_HEADER'}">
		<c:forEach var="subgroup" items="${group.subGroups}"
			varStatus="groupStatus">
			<c:set value="${pathPrefix}subGroups[${groupStatus.index}]."
				var="path" />
			<config:group group="${subgroup}" pathPrefix="${path}"
				hideExpandCollapse="true" />
		</c:forEach>
		<form:input type="hidden" path="${pathPrefix}id" />
	</c:if>
</c:if>
