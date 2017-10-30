<!--
 [y] hybris Platform

 Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 All rights reserved.

 This software is the confidential and proprietary information of SAP
 ("Confidential Information"). You shall not disclose such Confidential
 Information and shall use it only in accordance with the terms of the
 license agreement you entered into with SAP.
-->

<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component" %>


<jsp:useBean id="random" class="java.util.Random" scope="application"/>
<c:set var="cid" value="reco${random.nextInt(1000)}"/>

<div class="carousel-component sap-product-reco" id="${cid}" data-prodcode="${productCode}" data-componentId="${componentId}" data-base-url="${request.contextPath}">
    <div class="placeholder">
        <img src="${commonResourcePath}/images/spinner.gif" />
    </div>
</div>
