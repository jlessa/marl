<xsl:stylesheet version="1.0"
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
 xmlns:dbr="http://gbstore.com.au/messages/dbtranslator/saveOrderNumber/v1.0">
 <xsl:output omit-xml-declaration="yes" indent="yes"/>

<xsl:template match="node()|@*">
  <xsl:copy>
   <xsl:apply-templates select="node()|@*"/>
  </xsl:copy>
 </xsl:template>
 <xsl:template match="*">
  <xsl:element name="dbr:{name()}" namespace="http://gbstore.com.au/messages/dbtranslator/saveOrderNumber/v1.0">
    <xsl:copy-of select="namespace::*"/>
    <xsl:apply-templates select="node()|@*"/>
  </xsl:element>
 </xsl:template>
</xsl:stylesheet>