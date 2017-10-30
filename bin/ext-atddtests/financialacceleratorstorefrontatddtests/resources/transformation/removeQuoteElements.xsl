<!--
 [y] hybris Platform
 
 Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 All rights reserved.
 
 This software is the confidential and proprietary information of SAP
 ("Confidential Information"). You shall not disclose such Confidential
 Information and shall use it only in accordance with the terms of the
 license agreement you entered into with SAP.
-->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output omit-xml-declaration="yes" indent="yes"/>

 <xsl:template match="node()|@*">
  <xsl:copy>
   <xsl:apply-templates select="node()|@*"/>
  </xsl:copy>
 </xsl:template>

 <xsl:template match="startDate|quoteTitle"  />
	
</xsl:stylesheet>

