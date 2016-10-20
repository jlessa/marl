<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions"
   xmlns:dbe="http://gbstore.com.au/messages/dbtranslator/saveStoreOrderRequest/v1.0"
    xmlns:dbr="http://gbstore.com.au/messages/dbtranslator/saveStoreOrderReply/v1.0"
    exclude-result-prefixes="fn dbe">
<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

<xsl:template match="dbe:saveStoreOrderRequest">
    <dbr:saveStoreOrderReply xmlns:dbr="http://gbstore.com.au/messages/dbtranslator/saveStoreOrderReply/v1.0">
    <dbr:entry>
    	<xsl:for-each select="dbe:entry">
        	<xsl:apply-templates select="@*|node()"/>
        </xsl:for-each>
    </dbr:entry>
</dbr:saveStoreOrderReply>
</xsl:template>
    
<xsl:template match="*">
    <xsl:element name="{local-name(.)}">
      <xsl:apply-templates select="@* | node()"/>
    </xsl:element>
  </xsl:template>
  
<xsl:template match="@*">
    <xsl:attribute name="{local-name(.)}">
      <xsl:value-of select="."/>
    </xsl:attribute>
 </xsl:template>

</xsl:stylesheet> 