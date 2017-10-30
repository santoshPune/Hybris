<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/responsive/order" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:url value="/my-account/quote/quoteOrderDecision" var="quoteOrderDecisionURL" htmlEscape="false" />

<div class="quote-header pre-table no-margin-top">
	<spring:theme code="text.quotes.negotiationActivity.label" />
</div>
<order:quoteNegotiationActivityItem orderData="${orderData}" orderHistoryEntries="${orderHistoryEntryData}"
									quoteOrderDecisionURL="${quoteOrderDecisionURL}"/>