<?xml version = "1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:redirect="http://xml.apache.org/xalan/redirect" extension-element-prefixes="redirect"
xmlns:xalan="http://xml.apache.org/xalan"
xmlns:info="xalan://org.apache.xalan.lib.NodeInfo">

<xsl:output method="html" version ="1.0" encoding="ISO-8859-1" indent="yes"/>

   <!--
    	________________________________________________________
    	Stylesheet to Transform XMLFit Testfiles into HTML
    	Author: Christian Faigle
    	Date: 19.11.2008 
    	________________________________________________________
    --> 


<!-- ================================================================================== -->
<!-- Template for the root element. Calls: 'test' template	-->
<!-- ================================================================================== -->
<xsl:template match ="/">
	<html>
		<body>
			<xsl:apply-templates select="testsuite/testgroup"/>
		</body>
	</html>
	
</xsl:template>

<!-- ================================================================================== -->
<!-- Template for the test element. Calls: 'call' template	-->
<!-- ================================================================================== -->
<xsl:template match="testsuite/testgroup">
	<xsl:apply-templates select="call">
		<xsl:with-param name="testname" select="@name"/>
	</xsl:apply-templates>
</xsl:template>


<!-- ================================================================================== -->
<!-- Template for the call element. Calls: 'command' template, 'columns' template, 'rows' template	-->
<!-- ================================================================================== -->
<xsl:template match="call" name="call">
	<xsl:param name="filenode" select="document(@test)"/>
	<xsl:param name="testname"/>
	<xsl:variable name="filename" select="@test"/>
	<xsl:variable name="tmp" select="substring-before($filename, '.')"/>
	<xsl:variable name="tmp2" select="concat($tmp, '.html')"/>	
	<xsl:variable name="tmp3" select="concat('_', $tmp2)"/>
	<xsl:variable name="newfilename" select="concat($testname, $tmp3)"/> 
	
	<xsl:variable name="filenamewithDir" select="concat('xmlfiles/', $newfilename)"/>	
		<redirect:write file="{$filenamewithDir}">
			<html>
				<head>
					<link rel="stylesheet" href="css/design.css" type="text/css"/> 
				</head>
				<body>
					<div class="desc">LN:</div><br/>
					<div class="test"><xsl:text>&lt;test&gt;</xsl:text></div>
						<div class="fixture"><xsl:text>&lt;fixture&gt;</xsl:text></div>	
							<xsl:apply-templates select="$filenode/test/fixture/command"/>
							<xsl:apply-templates select="$filenode/test/fixture/columns"/>
							<xsl:apply-templates select="$filenode/test/fixture/rows"/>
						<div class="fixture"><xsl:text>&lt;/fixture&gt;</xsl:text></div>	
					<div class="test"><xsl:text>&lt;/test&gt;</xsl:text></div>
				</body>
			</html>
		</redirect:write>
	</xsl:template>	
	

<!-- ================================================================================== -->
<!-- Template for the columns element. Calls: 'column' template	-->
<!-- ================================================================================== -->
<xsl:template match="test/fixture/columns">
	<div class="columns">
		<xsl:text>&lt;columns&gt;</xsl:text>	
			<xsl:apply-templates select="column"/>
		<xsl:text>&lt;/columns&gt;</xsl:text>	
	</div>
</xsl:template>


<!-- ================================================================================== -->
<!-- Template for the column element. Calls:	-->
<!-- ================================================================================== -->
<xsl:template match="column">
	<div class="column">
		<a><xsl:attribute name="href">#<xsl:value-of select="position()"/></xsl:attribute>
		<div class="lineNumber"><xsl:value-of select="info:lineNumber()"/></div><xsl:text>&lt;column&gt;</xsl:text><xsl:value-of select="."/><xsl:text>&lt;/column&gt;</xsl:text>
		</a>
	</div>
	
</xsl:template>


<!-- ================================================================================== -->
<!-- Template for the rows element. Calls:'row' template	-->
<!-- ================================================================================== -->
<xsl:template match="test/fixture/rows">
	<div class="rows">
		<xsl:text>&lt;rows&gt;</xsl:text>	
			<xsl:apply-templates select="row"/>
		<xsl:text>&lt;/rows&gt;</xsl:text>	
	</div>
</xsl:template>


<!-- ================================================================================== -->
<!-- Template for the row element. Calls:'child::*' template	-->
<!-- ================================================================================== -->
<xsl:template match="row">
	<div class="row">
		<xsl:text>&lt;row&gt;</xsl:text>	
			<xsl:apply-templates select="child::*"/>
		<xsl:text>&lt;/row&gt;</xsl:text>	
	</div>
</xsl:template>

	
<!-- ================================================================================== -->
<!-- Template for the child::* element. Calls:	-->
<!-- ================================================================================== -->
<xsl:template match="child::*">
	<div class="rowentry">
		<div class="lineNumber"><xsl:value-of select="info:lineNumber()"/></div><xsl:text>&lt;</xsl:text><xsl:value-of select="name(self::*)"/><xsl:text>&gt;</xsl:text><xsl:value-of select="."/><xsl:text>&lt;</xsl:text>/<xsl:value-of select="name(self::*)"/><xsl:text>&gt;</xsl:text>
	</div>
</xsl:template>



<!-- ================================================================================== -->
<!-- Template for the command element. Calls:	-->
<!-- ================================================================================== -->
<xsl:template match ="command">
	<xsl:variable name="position" select="position()"/>
	<a><xsl:attribute name="id"><xsl:value-of select="$position"/></xsl:attribute>
		<a><xsl:attribute name="href">#<xsl:value-of select="$position"/></xsl:attribute>
		<div class="command">
			<div class="lineNumber"><xsl:value-of select="info:lineNumber()"/></div><xsl:text>&lt;command name="</xsl:text><xsl:value-of select="@name"/><xsl:text>"&gt;</xsl:text>
		</div>
		</a>
	</a>
	<xsl:apply-templates select="target"/>
	<xsl:apply-templates select="value"/>
	<div class="command"><xsl:text>&lt;/command&gt;</xsl:text></div>
</xsl:template>


<!-- ================================================================================== -->
<!-- Template for the target element.	-->
<!-- ================================================================================== -->
<xsl:template match="target">
	<xsl:variable name="target" select="var/@*"/>
	<div class="target">
	<xsl:choose>
		<xsl:when test="$target">
			<div class="lineNumber"><xsl:value-of select="info:lineNumber(.)"/></div><xsl:text>&lt;target&gt;</xsl:text><xsl:text>&lt;var name="</xsl:text><xsl:value-of select="$target"/><xsl:text>"/&gt;</xsl:text><xsl:text>&lt;/target&gt;</xsl:text>
		</xsl:when>
		<xsl:otherwise>
			<xsl:variable name="lineNumber" select="info:lineNumber(.)"/>
			<xsl:if test="$lineNumber != '-1'">
				<div class="lineNumber"><xsl:value-of select="info:lineNumber(.)"/></div><xsl:text>&lt;target&gt;</xsl:text><xsl:value-of select="."/><xsl:text>&lt;/target&gt;</xsl:text>
			</xsl:if>
		</xsl:otherwise>
	</xsl:choose>
	</div>
</xsl:template>

<!-- ================================================================================== -->
<!-- Template for the value element.	-->
<!-- ================================================================================== -->
<xsl:template match="value">
	<xsl:variable name="value" select="var/@*"/>
	<div class="value">
	<xsl:choose>
		<xsl:when test="$value">
			<div class="lineNumber"><xsl:value-of select="info:lineNumber(.)"/></div><xsl:text>&lt;value&gt;</xsl:text><xsl:text>&lt;var name="</xsl:text><xsl:value-of select="$value"/><xsl:text>"/&gt;</xsl:text><xsl:text>&lt;/value&gt;</xsl:text>
		</xsl:when>
		<xsl:otherwise>
			<xsl:variable name="lineNumber" select="info:lineNumber(.)"/>
			<xsl:if test="$lineNumber != '-1'">
				<div class="lineNumber"><xsl:value-of select="info:lineNumber(.)"/></div><xsl:text>&lt;value&gt;</xsl:text><xsl:value-of select="."/><xsl:text>&lt;/value&gt;</xsl:text>
			</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	 	</div>
</xsl:template>


</xsl:stylesheet>