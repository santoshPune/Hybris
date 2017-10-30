<%@ page trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url value="/my-company/organization-management/accountsummary-unit" var="accountSummaryUnitUrl"/>
<div class="back-link">
    <a href="${accountSummaryUnitUrl}"><span class="glyphicon glyphicon-chevron-left"></span></a>
    <span class="label"><spring:theme code="text.company.accountsummary.details"/></span>
</div>
