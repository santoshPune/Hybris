<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/responsive/order" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:url value="/my-account/quote/quoteOrderDecision" var="quoteOrderDecisionURL" htmlEscape="false" />

<div class="clearfix">
	<div class="col-sm-6 col-sm-offset-6 col-md-5 col-md-offset-7 col-lg-4 col-lg-offset-8">
		<action:actions element="div" parentComponent="${component}"/>
	</div>
</div>

<order:quoteActionsItem orderData="${orderData}" quoteOrderDecisionURL="${quoteOrderDecisionURL}" />