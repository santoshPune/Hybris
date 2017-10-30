<%--
 [y] hybris Platform
 
 Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 All rights reserved.
 
 This software is the confidential and proprietary information of SAP
 ("Confidential Information"). You shall not disclose such Confidential
 Information and shall use it only in accordance with the terms of the
 license agreement you entered into with SAP.
 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
   <head>
   <title>Heartbeat</title>
   </head>
   <body>
   
   		<h1>E2E Heartbeat Statistics</h1>
   		<h2>[<%= new java.util.Date()%>]</h2>
   		
   		<p>
   			<c:forEach items="${stats}" var="stats">
	   		[${stats.key}:${stats.value}]
			</c:forEach>
   		</p>
   </body>
</html>