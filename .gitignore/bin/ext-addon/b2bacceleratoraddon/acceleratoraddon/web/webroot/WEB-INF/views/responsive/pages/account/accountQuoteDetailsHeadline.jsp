<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:url value="/my-account/my-quotes" var="quotesURL" htmlEscape="false"/>
<common:headline url="${quotesURL}" labelKey="text.quotes.orderDetails.label" />