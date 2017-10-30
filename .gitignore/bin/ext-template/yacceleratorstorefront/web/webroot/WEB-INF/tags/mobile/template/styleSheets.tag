<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="cms" tagdir="/WEB-INF/tags/mobile/template/cms" %>

<c:choose>
	<c:when test="${wro4jEnabled}">
		<link rel="stylesheet" type="text/css" media="all" href="${contextPath}/wro/css_mobile.css" />
		<link rel="stylesheet" type="text/css" media="all" href="${contextPath}/wro/${themeName}_mobile.css" />
		<link rel="stylesheet" type="text/css" media="all" href="${contextPath}/wro/addons_mobile.css" />
	</c:when>
	<c:otherwise>
		<%-- colorbox CSS --%>
		<link type="text/css" rel="stylesheet" media="screen" href="${commonResourcePath}/css/colorbox.css"/>
		<%-- our site css --%>
		<link type="text/css" rel="stylesheet" media="screen" href="${commonResourcePath}/css/common.css"/>
		<link type="text/css" rel="stylesheet" media="screen" href="${commonResourcePath}/css/jquery.ui.stars.css"/>
		<link type="text/css" rel="stylesheet" media="screen" href="${commonResourcePath}/css/jquery.mobile.structure-1.3.0.min.css"/>
		<link type="text/css" rel="stylesheet" media="screen" href="${commonResourcePath}/css/jquery.mobile-1.3.0.accelerator-mobile.css"/>
		<link type="text/css" rel="stylesheet" media="screen" href="${commonResourcePath}/css/mobile.css"/>
		<link type="text/css" rel="stylesheet" media="screen" href="${commonResourcePath}/css/jquery.mobile.easydialog.css"/>
		<link type="text/css" rel="stylesheet" media="screen" href="${commonResourcePath}/css/jquery.mobile.collapsiblelistview.hybris.css"/>
		
		<%-- theme specific css --%>
		<link type="text/css" rel="stylesheet" media="screen" href="${themeResourcePath}/css/changes.css"/>
		
		<%--  AddOn Common CSS files --%>
		<c:forEach items="${addOnCommonCssPaths}" var="addOnCommonCss">
		    <link rel="stylesheet" type="text/css" media="all" href="${addOnCommonCss}" />
		</c:forEach>
	</c:otherwise>
</c:choose>

<%--  AddOn Theme CSS files --%>
<c:forEach items="${addOnThemeCssPaths}" var="addOnThemeCss">
    <link rel="stylesheet" type="text/css" media="all" href="${addOnThemeCss}" />
</c:forEach>

<cms:previewCSS cmsPageRequestContextData="${cmsPageRequestContextData}" />
