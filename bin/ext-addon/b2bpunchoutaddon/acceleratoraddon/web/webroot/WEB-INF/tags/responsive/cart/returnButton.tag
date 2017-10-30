<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>

<%@ attribute name="url" required="true" type="java.lang.String"%>

<spring:theme code="punchout.return" var="returnText"/>
<a class="btn btn-primary btn-block returnButton" href="${url}"><spring:theme text="RETURN TO REQUISITION" code="${returnText}"/></a>