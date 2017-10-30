<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/responsive/order" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:url value="${url}" var="quoteOrderDecisionURL" htmlEscape="false" />

<c:if test="${orderData.status eq 'APPROVED_QUOTE'}">
	<order:quoteOrderDecisionForm quoteOrderDecisionURL="${quoteOrderDecisionURL}" orderCode="${orderData.code}"
								  selectedQuoteDecision="ACCEPTQUOTE"
								  decisionLabelKey="text.quotes.acceptAndOrderButton.displayName"
								  decisionButtonCSSClass="btn btn-primary btn-block" displayAsLink="false" />
</c:if>