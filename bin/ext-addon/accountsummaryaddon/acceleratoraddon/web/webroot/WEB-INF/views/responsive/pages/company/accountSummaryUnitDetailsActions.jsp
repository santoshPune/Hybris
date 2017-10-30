<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url value="/my-company/organization-management/accountsummary-unit" var="accountSummaryUnitUrl"/>
<div class="row">
	<div class="col-xs-12 col-sm-5 col-md-3">
		<div class="accountActions-bottom">
			<button type="button" class="form btn-default btn btn-block accountSummaryUnitBackBtn" data-back-to-account-summary="${accountSummaryUnitUrl}">
				<spring:theme code="text.company.accountsummary.unit.details.backToAccountSummary"/>
			</button>
		</div>
	</div>
</div>