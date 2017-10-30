<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>

<%@ attribute name="url" required="true" type="java.lang.String"%>

<spring:theme code="punchout.cancel.text" var="cancelText"/>
<a class="btn btn-primary btn-block cancelButton" href="${url}"><spring:theme text="CANCEL" code="${cancelText}"/></a>
