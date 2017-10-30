<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<%@ attribute name="entry" required="true" type="de.hybris.platform.commercefacades.order.data.OrderEntryData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags"%>

<c:forEach items="${entry.configurationInfos}" var="configurationInfo">
     <div>${configurationInfo.configurationLabel}: ${configurationInfo.configurationValue}</div>
</c:forEach>
