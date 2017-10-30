<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<c:url value="${redirectUrl}" var="continueUrl"/>
<template:page pageTitle="${pageTitle}" hideHeaderLinks="true">
	<div id="globalMessages">
		<common:globalMessages/>
	</div>
	<c:if test="${paymentStatus == 'true'}" >
		<p><spring:theme code="order.paymentStatus.successful" /></p>
	</c:if>
	<c:if test="${paymentStatus == 'false'}" >
		<p><spring:theme code="order.paymentStatus.failed" /></p>
	</c:if>
	<c:if test="${paymentStatus == 'notFound'}" >
		<p><spring:theme code="order.paymentStatus.notFound" /></p>
	</c:if>
	<div>
		<p><spring:theme code="order.paymentReturn.closeCountDown" /><span id="countdown"></span></p>
	</div>
	<cms:pageSlot position="SideContent" var="feature" element="div" class="span-24 side-content-slot cms_disp-img_slot">
		<cms:component component="${feature}"/>
	</cms:pageSlot>
</template:page>
