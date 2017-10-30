<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:theme code="punchout.footer.notice" var="notice"/>


<footer class="main-footer">
	<cms:pageSlot position="Footer" var="feature">
		<cms:component component="${feature}"/>
	</cms:pageSlot>
</footer>