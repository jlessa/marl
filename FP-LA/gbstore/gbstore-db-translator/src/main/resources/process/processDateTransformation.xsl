<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions"
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
    xmlns:v1="http://gbstore.com.au/messages/legacysystem/storeOrderRequest/v1.0"
	xmlns:v11="http://gbstore.com.au/types/legacysystem/v1.0"
	exclude-result-prefixes="fn v1 v11">
<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
<xsl:strip-space elements="*"/>

<xsl:template match="soapenv:Body">
    <xsl:apply-templates select="v1:storeOrderRequest"/>
</xsl:template>

<xsl:template match="soapenv:Header">
    <xsl:apply-templates select="v1:storeOrderRequest"/>
</xsl:template>

<xsl:template match="soapenv:Envelope">
    <xsl:apply-templates select="soapenv:Body"/>
</xsl:template>

<xsl:template match="node()|@*">
    <xsl:copy>
        <xsl:apply-templates select="node()|@*"/>           
    </xsl:copy>
</xsl:template>

<xsl:template match="v11:processDate">
    <xsl:copy><xsl:value-of select="fn:current-dateTime()" /></xsl:copy>
</xsl:template>

</xsl:stylesheet>