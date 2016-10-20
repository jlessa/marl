<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions"
   xmlns:dbr="http://gbstore.com.au/messages/dbtranslator/saveStoreOrderReply/v1.0"
    xmlns:dbe="http://gbstore.com.au/messages/dbtranslator/enrichmentRequest/v1.0"
    xmlns:tp="http://gbstore.com.au/types/dbtranslator/v1.0"
    exclude-result-prefixes="fn dbr">
<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

<xsl:template match="dbr:saveStoreOrderReply">
   <dbe:EnrichmentRequest xmlns:dbe="http://gbstore.com.au/messages/dbtranslator/enrichmentRequest/v1.0" xmlns:tp="http://gbstore.com.au/types/dbtranslator/v1.0">
<dbe:storeOrderList>
<xsl:for-each select="dbr:entry/tp:storeOrderList//tp:storeOrder">
    <tp:storeOrder>
        <tp:storeOrderId><xsl:value-of select="tp:storeOrderId"/> </tp:storeOrderId>
 <tp:itemStoreOrderList>
            <xsl:for-each select="tp:itemStoreOrderList//tp:itemStoreOrder">
             <tp:itemStoreOrder>
                <tp:itemStoreOrderId><xsl:value-of select="tp:itemStoreOrderId"/></tp:itemStoreOrderId>
                     <tp:product>
                        <tp:internalId><xsl:value-of select="tp:product/tp:internalId"/></tp:internalId>
                        <tp:name><xsl:value-of select="tp:product/tp:name"/></tp:name>
                     </tp:product>
                     <tp:quantity><xsl:value-of select="tp:quantity"/></tp:quantity>
             </tp:itemStoreOrder>
            </xsl:for-each>
            </tp:itemStoreOrderList>
    </tp:storeOrder>
    </xsl:for-each>
    </dbe:storeOrderList>
</dbe:EnrichmentRequest>    
</xsl:template>

<xsl:template match="*">
    <xsl:copy>
        <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
    
</xsl:template>

</xsl:stylesheet>