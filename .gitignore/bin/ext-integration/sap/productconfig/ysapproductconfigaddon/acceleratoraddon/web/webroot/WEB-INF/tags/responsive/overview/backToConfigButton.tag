<%@ tag language="java" pageEncoding="ISO-8859-1" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:url value="/${product.code}/config" var="configUrl" />

<button type="submit" id="cpqBackToConfigBtn" class="cpq-btn-backToConfig" data-back-to-config="${configUrl}">
	<spring:message code="sapproductconfig.overview.backtoconfiguration" text="Back To Configuration" />
</button>



