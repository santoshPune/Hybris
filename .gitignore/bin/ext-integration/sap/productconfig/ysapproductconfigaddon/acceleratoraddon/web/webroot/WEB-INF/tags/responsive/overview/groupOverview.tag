<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="overview" tagdir="/WEB-INF/tags/addons/ysapproductconfigaddon/responsive/overview"%>

<%@ attribute name="group" required="true" type="de.hybris.platform.sap.productconfig.facades.overview.CharacteristicGroup"%>
<%@ attribute name="level" required="true" type="Integer"%>

<jsp:useBean id="cons"
	class="de.hybris.platform.sap.productconfig.frontend.util.impl.ConstantHandler"
	scope="session" />
<div class="cpq-overview-group-row">
	<div class="cpq-overview-group-hierarchy-bar" data-level="${level}"></div>
	<div class="cpq-overview-group-content">
		<div id="${group.groupDescription}_title" class="cpq-overview-group-header">
		
			<c:choose>
				<c:when test="${group.id eq cons.getGeneralGroupName()}">
					<spring:message code="sapproductconfig.group.general.title" text="General (Default)" var="groupTitle" />
				</c:when>
				<c:otherwise>
					<c:set var="groupTitle" value="${group.groupDescription}" />
				</c:otherwise>
			</c:choose>
			<div id="cpqGroupTitle" class="cpq-overview-group-title" title="${groupTitle}">
				<c:out value="${groupTitle}" />
			</div>
		</div>
		
		<c:if test="${not empty group.characteristicValues}">
			<div class="cpq-group">
				<c:forEach var="cstic" items="${group.characteristicValues}" varStatus="csticStatus">
					<c:choose>
						<c:when test="${cstic.valuePositionType eq 'ONLY_VALUE'}">
							<overview:csticOverview cstic="${cstic}"/>
						</c:when>
						<c:otherwise>
							<overview:multiValuedCsticOverview cstic="${cstic}"/>
						</c:otherwise>
					</c:choose>				
				</c:forEach>
			</div>
		</c:if>
	</div>
</div>

<c:if test="${not empty group.subGroups}">
	<c:forEach var="subgroup" items="${group.subGroups}" varStatus="groupStatus">			
		<overview:groupOverview group="${subgroup}" level="${level + 1}"/>
	</c:forEach>
</c:if>
