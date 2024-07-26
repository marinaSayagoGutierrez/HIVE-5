<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:ns0="http://ds.hive5.com/elements" xmlns:socket="http://www.oracle.com/XSL/Transform/java/oracle.tip.adapter.socket.ProtocolTranslator" xmlns:oracle-xsl-mapper="http://www.oracle.com/xsl/mapper/schemas" xmlns:dvm="http://www.oracle.com/XSL/Transform/java/oracle.tip.dvm.LookupValue" xmlns:mhdr="http://www.oracle.com/XSL/Transform/java/oracle.tip.mediator.service.common.functions.MediatorExtnFunction" xmlns:oraxsl="http://www.oracle.com/XSL/Transform/java" xmlns:oraext="http://www.oracle.com/XSL/Transform/java/oracle.tip.pc.services.functions.ExtFunc" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xp20="http://www.oracle.com/XSL/Transform/java/oracle.tip.pc.services.functions.Xpath20" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xref="http://www.oracle.com/XSL/Transform/java/oracle.tip.xref.xpath.XRefXPathFunctions" exclude-result-prefixes=" oracle-xsl-mapper xsi xsd xsl ns0 socket dvm mhdr oraxsl oraext xp20 xref"
                xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/">
   <oracle-xsl-mapper:schema>
      <!--SPECIFICATION OF MAP SOURCES AND TARGETS, DO NOT MODIFY.-->
      <oracle-xsl-mapper:mapSources>
         <oracle-xsl-mapper:source type="WSDL">
            <oracle-xsl-mapper:schema location="../WSDLs/users.wsdl"/>
            <oracle-xsl-mapper:rootElement name="updateUserRequest" namespace="http://ds.hive5.com/elements"/>
         </oracle-xsl-mapper:source>
      </oracle-xsl-mapper:mapSources>
      <oracle-xsl-mapper:mapTargets>
         <oracle-xsl-mapper:target type="WSDL">
            <oracle-xsl-mapper:schema location="../WSDLs/users.wsdl"/>
            <oracle-xsl-mapper:rootElement name="updateUserRequest" namespace="http://ds.hive5.com/elements"/>
         </oracle-xsl-mapper:target>
      </oracle-xsl-mapper:mapTargets>
      <!--GENERATED BY ORACLE XSL MAPPER 12.2.1.4.0(XSLT Build 190828.0353.3300) AT [WED JUN 19 13:08:44 CEST 2024].-->
   </oracle-xsl-mapper:schema>
   <!--User Editing allowed BELOW this line - DO NOT DELETE THIS LINE-->
   <xsl:template match="/">
      <ns0:updateUserRequest>
         <ns0:userId>
            <xsl:value-of select="/ns0:updateUserRequest/ns0:userId"/>
         </ns0:userId>
         <ns0:firstName>
            <xsl:value-of select="/ns0:updateUserRequest/ns0:firstName"/>
         </ns0:firstName>
         <ns0:lastName>
            <xsl:value-of select="/ns0:updateUserRequest/ns0:lastName"/>
         </ns0:lastName>
         <ns0:email>
            <xsl:value-of select="/ns0:updateUserRequest/ns0:email"/>
         </ns0:email>
         <ns0:password>
            <xsl:value-of select="/ns0:updateUserRequest/ns0:password"/>
         </ns0:password>
         <ns0:roleId>
            <xsl:value-of select="/ns0:updateUserRequest/ns0:roleId"/>
         </ns0:roleId>
         <ns0:boards>
            <xsl:for-each select="/ns0:updateUserRequest/ns0:boards/ns0:boardId">
               <ns0:boardId>
                  <xsl:value-of select="."/>
               </ns0:boardId>
            </xsl:for-each>
         </ns0:boards>
      </ns0:updateUserRequest>
   </xsl:template>
</xsl:stylesheet>
