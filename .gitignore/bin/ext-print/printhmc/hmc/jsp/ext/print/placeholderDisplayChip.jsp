<%@page import="java.util.*,de.hybris.platform.print.jalo.*"%>
<%@include file="../../head.inc"%>
<%--
  ~ [y] hybris Platform
  ~
  ~ Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
  ~ All rights reserved.
  ~
  ~ This software is the confidential and proprietary information of SAP
  ~ ("Confidential Information"). You shall not disclose such Confidential
  ~ Information and shall use it only in accordance with the terms of the
  ~ license agreement you entered into with SAP.
  --%>

<table>
	<tr>
		<td valign="top"><b>Placeholder Qualifier</b></td>
		<td valign="top"><b>Placeholder Value</b></td>
	</tr>
<%
	de.hybris.platform.print.hmc.attribute.PlaceholderDisplayChip theChip = (de.hybris.platform.print.hmc.attribute.PlaceholderDisplayChip) request.getAttribute(AbstractChip.CHIP_KEY);

	for( Placeholder ph : theChip.getPlaceholders() )
	{
%>
	<tr>
		<td valign="top"><%= ph.getQualifier() %></td>
		<td valign="top"><%= theChip.getValue( ph )%><br/><small><%= theChip.getPlaceholderSource( ph )%></small></td>
	</tr>
<%
	}
%>
</table>
