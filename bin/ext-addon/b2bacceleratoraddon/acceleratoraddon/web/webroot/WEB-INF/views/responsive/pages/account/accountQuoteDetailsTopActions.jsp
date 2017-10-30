<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/responsive/order" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:url value="/my-account/quote/quoteOrderDecision" var="quoteOrderDecisionURL" htmlEscape="false" />

<order:quoteActionsItem orderData="${orderData}" quoteOrderDecisionURL="${quoteOrderDecisionURL}"/>