<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="config" tagdir="/WEB-INF/tags/addons/ysapproductconfigaddon/responsive/configuration"%>
<%@ taglib prefix="overview" tagdir="/WEB-INF/tags/addons/ysapproductconfigaddon/responsive/overview"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="conf" uri="/WEB-INF/tags/addons/ysapproductconfigaddon/responsive/configuration/sapproductconfig.tld"%>


<div class="cpq-overview-filter">
	<div class="yComponentWrapper">
		<div id="cpq-overview-facet" class="col-md-12 hidden-sm hidden-xs product__facet cpq-overview-facet cpq-js-overview-facet">
			<form:form id="filterform" method="POST" modelAttribute="overviewUiData">
						<c:if test="${conf:hasAppliedFilters(overviewUiData)}">
							<overview:appliedFilters csticFilterList="${overviewUiData.csticFilterList}" groupFilterList="${overviewUiData.groupFilterList}"/>
						</c:if>
						<overview:filterCstics csticFilterList="${overviewUiData.csticFilterList}"/>
						<overview:filterGroups groupFilterList="${overviewUiData.groupFilterList}"/>
				<form:input type="hidden" value="${overviewUiData.configId}" path="configId" />
				<form:input type="hidden" value="${overviewUiData.cpqAction}" path="cpqAction" id="filterCPQAction"/>
			</form:form>
		</div>
	</div>
</div>