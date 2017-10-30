<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="overview" tagdir="/WEB-INF/tags/addons/ysapproductconfigaddon/responsive/overview"%>

<div id="configOverview" class="cpq-form cpq-overview">
	<form:form id="overviewform" method="POST" modelAttribute="overviewUiData">
      
		 <div class="cpq-groups">					
	
			<c:set value="${fn:length(overviewUiData.configurationOverviewData.groups)}"  var="numberOfCsticGroups"/>
			<c:if test="${numberOfCsticGroups == 0}">
				<overview:noResult/>
			</c:if>	
				
			<c:forEach var="group" items="${overviewUiData.configurationOverviewData.groups}" varStatus="groupStatus">
			<c:set value="groups[${groupStatus.index}]." var="path" />
				<overview:groupOverview group="${group}" level="1"/>
			</c:forEach>				
		</div>
		
		<form:input type="hidden" value="${overviewUiData.configId}" path="configId" />
		<form:input type="hidden" value="${overviewUiData.cpqAction}" path="cpqAction" />		
	</form:form>
</div>

