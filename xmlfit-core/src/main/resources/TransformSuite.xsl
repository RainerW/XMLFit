<?xml version = "1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:date="xalan://java.util.Date" xmlns:redirect="http://xml.apache.org/xalan/redirect" 
extension-element-prefixes="redirect">
<xsl:output method="html" version ="1.0" encoding="ISO-8859-1" indent="yes"/>

   <!--
    	________________________________________________________
    	Stylesheet to Transform XMLFit Testfiles into Fit HTML Tables
    	Author: Christian Faigle
    	Date: 19.11.2008 
    	________________________________________________________
    --> 
  



<!-- ================================================================================== -->
<!--  Definition of  global Variables	-->
<!-- ================================================================================== -->
<xsl:param name="base_dir" select="'basedir'"/>
<xsl:param name="project_dir" select="'projectdir'"/>
<xsl:param name="test_dir" select="'testdir'"/>
<xsl:param name="input_dir" select="'inputDir'"/>
<xsl:param name="css_file" select="'cssfile'"/>
<xsl:variable name="newDir" select="concat($test_dir, '/')"/>
<xsl:param name="file_dir" select="'filedir'"/>
<xsl:variable name="newFileDir" select="concat('../',$file_dir)"/>
<xsl:variable name="css" select="concat('css/',$css_file)"/> 

<!-- ================================================================================== -->
<!-- Template to generate a timestamp	-->
<!-- ================================================================================== -->
<xsl:template name="currentTime" xmlns:java="http://xml.apache.org/xslt/java">
  <xsl:value-of select="java:java.util.Date.new()"/>
</xsl:template>

<!-- ================================================================================== -->
<!-- Template for the root element. Calls: 'comment' template, 'testgroup' template	-->
<!-- ================================================================================== -->
<xsl:template match ="testsuite">
<html>
	<head>
		<link><xsl:attribute name="rel">stylesheet</xsl:attribute><xsl:attribute name="href"><xsl:value-of select="$css"/></xsl:attribute><xsl:attribute name="type">text/css</xsl:attribute></link>
	</head>
	<body>
		<div class="image"></div>
		<div class="logo"></div>
		<div class="main"><div class="date">Date: <xsl:call-template name="currentTime"/></div>
		<div class="author">Author: <xsl:value-of select="@author"/></div>
		<xsl:apply-templates select="comment"/>
		<xsl:apply-templates select="testgroup">
			<xsl:with-param name="author" select="@author"/>
		</xsl:apply-templates>
		<xsl:for-each select="testgroup">
			<xsl:variable name="position" select="concat(position(),'-')"/>
			<xsl:variable name="testname" select="@name"/>
			<xsl:variable name="linkname" select="concat($testname, '.html')"/>
			<div class="test">Test: <a><xsl:attribute name="href"><xsl:value-of select="$base_dir"/>/<xsl:value-of select="$position"/><xsl:value-of select="$linkname"/></xsl:attribute><xsl:value-of select="$testname"/></a>
			Defaultdata: <a><xsl:attribute name="href"><xsl:value-of select="$project_dir"/>/<xsl:value-of select="$input_dir"/>/<xsl:value-of select="@defaultData"/></xsl:attribute><xsl:value-of select="@defaultData"/></a></div>	
		</xsl:for-each>
		</div>
	</body>
</html>
</xsl:template>

<!-- ================================================================================== -->
<!-- Template for the 'test' element. Calls: 'call' template	-->
<!-- Param List: 
	 defaultnode - Node for the DefaultData Document -->
<!-- ================================================================================== -->
<xsl:template match="testgroup">
	<xsl:param name="author"/>
	<xsl:variable name="defaultFileName" select="@defaultData"/>
	<xsl:variable name="defaultnode" select="document($defaultFileName)"/>
	<xsl:variable name="testname" select="@name"/>
	<xsl:variable name="testID" select="@id"/>
	<xsl:variable name="outputname" select="concat($testname, '.html')"/>
	<xsl:variable name="position" select="concat(position(),'-')"/>
	<xsl:variable name="outputnamewithposition" select="concat($position, $outputname)"/>
	<xsl:variable name="outputnamewithDir" select="concat($newDir, $outputnamewithposition)"/>
	<xsl:variable name="testsuiteDir" select="concat($base_dir, $outputname)"/>
	
	<xsl:if test="@type='suite'">
		<redirect:write file="{$outputname}">
			<html>
			<head>
				<link><xsl:attribute name="rel">stylesheet</xsl:attribute><xsl:attribute name="href"><xsl:value-of select="$css"/></xsl:attribute><xsl:attribute name="type">text/css</xsl:attribute></link>
			</head>
			<body>
				<div class="image"></div>
				<div class="logo"></div>
				<xsl:apply-templates select="call">
					<xsl:with-param name="defaultnode" select="$defaultnode"/>
					<xsl:with-param name="ubertestname" select="$testname"/>
					<xsl:with-param name="author" select="$author"/> 
				</xsl:apply-templates>
			</body>
		</html>	
	</redirect:write>
	</xsl:if>
	
	<redirect:write file="{$outputnamewithDir}">
		<html>
			<head>
				<link><xsl:attribute name="rel">stylesheet</xsl:attribute><xsl:attribute name="href"><xsl:value-of select="$css"/></xsl:attribute><xsl:attribute name="type">text/css</xsl:attribute></link>
			</head>
			<body>
				<div class="image"></div>
				<div class="logo"></div>
				<xsl:apply-templates select="call">
					<xsl:with-param name="defaultnode" select="$defaultnode"/>
					<xsl:with-param name="ubertestname" select="$testname"/>
					<xsl:with-param name="author" select="$author"/> 
				</xsl:apply-templates>
			</body>
		</html>	
	</redirect:write>
 </xsl:template>


<!-- ================================================================================== -->
<!-- Template for the 'call' element. Calls: 'command' template, 'column' template, 'row' template	-->
<!-- Param List: 
	 defaultnode - node for the Default Data Document 
	 customdata - node for the Attributelist given by user
	 loopmax - max. value for the loop
	 filenode - node for the Fixture Document 
	 datanode - node for the Data Document
	 ubertestname - name of the test
	 -->
<!-- ================================================================================== -->
<xsl:template match="call" name="call">
	<xsl:param name="author"/>
	<xsl:param name="ubertestname"/>
	<xsl:param name="defaultnode"/>
	<xsl:param name="customdata"/>
	<xsl:variable name="filename" select="@test"/>
	<xsl:variable name="testname" select="@name"/>
	<xsl:variable name="choosevalue" select="'testsuite'"/>
	<xsl:variable name="filenode" select="document($filename)"/>
	<xsl:apply-templates select="$filenode/test/comment"/>
	<xsl:apply-templates select="$filenode/test/call"/>
		<xsl:variable name="datanode" select="document(@data)"/>
		<div class="main">
		<div class="test">Testfile: <a><xsl:attribute name="href"><xsl:value-of select="$project_dir"/>/<xsl:value-of select="$input_dir"/>/<xsl:value-of select="$filename"/></xsl:attribute><xsl:value-of select="$filename"/></a>
		<xsl:if test="@data">
			Testdata: <a><xsl:attribute name="href"><xsl:value-of select="$project_dir"/>/<xsl:value-of select="$input_dir"/>/<xsl:value-of select="@data"/></xsl:attribute><xsl:value-of select="@data"/></a><br/>
		</xsl:if>
		</div>
		<div class="author">Author: <xsl:value-of select="$filenode/test/@author"/></div>	
		</div>	
			<xsl:choose>
				<xsl:when test="$filenode/testsuite">
					<xsl:apply-templates select="$filenode/testsuite"/>
				</xsl:when>
				<xsl:when test="$filenode/test/fixture[contains(@type, 'dbfit')]">
					<xsl:apply-templates select="$filenode/test/fixture/comment"/>
					<table cellpadding="0" cellspacing="0" border="1">
						<tbody>
						 <tr><td><xsl:value-of select="$filenode/test/fixture/@type"/></td></tr>
							<xsl:apply-templates select="$filenode/test/fixture/command">
									<xsl:with-param name="datanode" select="$datanode"/>
									<xsl:with-param name="defaultnode" select= "$defaultnode"/>
									<xsl:with-param name="customdata" select="@*[name()!='test' and name()!='data']"/>
									<xsl:with-param name="loopmax" select="@loopmax"/> 
									<xsl:with-param name="filename" select="$filename"/>
									<xsl:with-param name="ubertestname" select="$ubertestname"/>
								</xsl:apply-templates>
							<xsl:if test="$filenode/test/fixture/columns">
							<tr><xsl:apply-templates select="$filenode/test/fixture/columns/column">
									<xsl:with-param name="filename" select="$filename"/>
									<xsl:with-param name="ubertestname" select="$ubertestname"/>
								</xsl:apply-templates></tr>
							</xsl:if>
							<xsl:apply-templates select ="$filenode/test/fixture/rows/row">
								<xsl:with-param name="datanode" select="$datanode"/>
								<xsl:with-param name="loopnode" select="document(@loopdata)"/>
								<xsl:with-param name="defaultnode" select= "$defaultnode"/> 
								<xsl:with-param name="customdata" select="@*"/>
								<xsl:with-param name="loopmax" select="@loopmax"/> 
								<xsl:with-param name="filenode" select="document($filename)"/>
							</xsl:apply-templates>
						</tbody>
					</table>
				</xsl:when>
				<xsl:when test="$filenode/test/fixture[@type!='Selenium']">
					<xsl:apply-templates select="$filenode/test/fixture/comment"/>
					<table cellpadding="0" cellspacing="0" border="1">
						<tbody>
							<tr><td><xsl:value-of select="$filenode/test/fixture/@type"/></td></tr>
							<tr><xsl:apply-templates select="$filenode/test/fixture/columns/column">
									<xsl:with-param name="filename" select="$filename"/>
									<xsl:with-param name="ubertestname" select="$ubertestname"/>
								</xsl:apply-templates></tr>
							<xsl:apply-templates select ="$filenode/test/fixture/rows/row">
								<xsl:with-param name="datanode" select="$datanode"/>
								<xsl:with-param name="loopnode" select="document(@loopdata)"/>
								<xsl:with-param name="defaultnode" select= "$defaultnode"/> 
								<xsl:with-param name="customdata" select="@*"/>
								<xsl:with-param name="loopmax" select="@loopmax"/> 
								<xsl:with-param name="filenode" select="document($filename)"/>
							</xsl:apply-templates>
						</tbody>
					</table>
				</xsl:when>
				<xsl:when test="$filenode/test/fixture[@type='Selenium']">
				<xsl:choose>
				<xsl:when test="$datanode">
				<xsl:for-each select="$datanode/testdata/dataset">
					<xsl:choose>
						<xsl:when test="$testname">
							<div class="testname"><xsl:value-of select="$testname"/>: <xsl:value-of select="position()"/></div>
						</xsl:when>
						<xsl:otherwise>
						    <div class="testname"><xsl:value-of select="substring-before($filename, '.')"/>: <xsl:value-of select="position()"/></div>
						</xsl:otherwise>
				    </xsl:choose>
					<xsl:variable name="actualnode" select="self::*"/>
					<xsl:apply-templates select="$filenode/test/fixture/comment"/>
					<table cellpadding="0" cellspacing="0" border="1">
						<tbody>
							<tr><td colspan="3">selenium.SeleniumFixture</td></tr>
								<xsl:apply-templates select="$filenode/test/fixture/command">
									<xsl:with-param name="datanode" select="$datanode"/>
									<xsl:with-param name="defaultnode" select= "$defaultnode"/>
									<xsl:with-param name="customdata" select="@*[name()!='test' and name()!='data']"/>
									<xsl:with-param name="loopmax" select="@loopmax"/> 
									<xsl:with-param name="filename" select="$filename"/>
									<xsl:with-param name="ubertestname" select="$ubertestname"/>
									<xsl:with-param name="actualnode" select="$actualnode"/>
								</xsl:apply-templates>
						</tbody>
					</table>
					</xsl:for-each>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="$filenode/test/fixture/comment"/>
						<table cellpadding="0" cellspacing="0" border="1">
							<tbody>
								<tr><td colspan="3">selenium.SeleniumFixture</td></tr>
									<xsl:apply-templates select="$filenode/test/fixture/command">
										<xsl:with-param name="datanode" select="$datanode"/>
										<xsl:with-param name="defaultnode" select= "$defaultnode"/>
										<xsl:with-param name="customdata" select="@*[name()!='test' and name()!='data']"/>
										<xsl:with-param name="loopmax" select="@loopmax"/> 
										<xsl:with-param name="filename" select="$filename"/>
										<xsl:with-param name="ubertestname" select="$ubertestname"/>
									</xsl:apply-templates>
								</tbody>
							</table>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="not($filenode/test/fixture/@type)">
					<xsl:choose>
						<xsl:when test="$datanode">
							<xsl:for-each select="$datanode/testdata/dataset">
								<xsl:choose>
									<xsl:when test="not(@loop)">
										<xsl:choose>
											<xsl:when test="$testname">
												<div class="testname"><xsl:value-of select="$testname"/>: <xsl:value-of select="position()"/></div>
											</xsl:when>
											<xsl:otherwise>
												<div class="testname"><xsl:value-of select="substring-before($filename, '.')"/>: <xsl:value-of select="position()"/></div>
											</xsl:otherwise>
										</xsl:choose>
										<xsl:variable name="actualnode" select="self::*"/>
										<xsl:apply-templates select="$filenode/test/fixture/comment"/>
										<table cellpadding="0" cellspacing="0" border="1">
											<tbody>
												<tr><td colspan="3">fit.ActionFixture</td></tr>
													<xsl:apply-templates select="$filenode/test/fixture/command">
														<xsl:with-param name="datanode" select="$datanode"/>
														<xsl:with-param name="defaultnode" select= "$defaultnode"/>
														<xsl:with-param name="customdata" select="@*[name()!='test' and name()!='data']"/>
														<xsl:with-param name="loopmax" select="@loopmax"/> 
														<xsl:with-param name="actualnode" select="$actualnode"/>
														<xsl:with-param name="filename" select="$filename"/>
														<xsl:with-param name="ubertestname" select="$ubertestname"/>
													</xsl:apply-templates>
												</tbody>
											</table>
										</xsl:when>
									</xsl:choose>
								</xsl:for-each>
						</xsl:when>
						<xsl:otherwise>
							<xsl:apply-templates select="$filenode/test/fixture/comment"/>
							<table cellpadding="0" cellspacing="0" border="1">
										<tbody>
											<tr><td colspan="3">fit.ActionFixture</td></tr>
											<xsl:apply-templates select="$filenode/test/fixture/command">
												<xsl:with-param name="ubertestname" select="$ubertestname"/>
												<xsl:with-param name="datanode" select="$datanode"/>
												<xsl:with-param name="defaultnode" select= "$defaultnode"/>
												<xsl:with-param name="customdata" select="@*[name()!='test' and name()!='data']"/>
												<xsl:with-param name="loopmax" select="@loopmax"/> 
												<xsl:with-param name="filename" select="$filename"/>
											</xsl:apply-templates>
									</tbody>
								</table>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
		 </xsl:choose>
</xsl:template>


<!-- ================================================================================== -->
<!-- Template for the 'command' element.	-->
<!-- Param List: 
	 defaultnode - node for the Default Data Document 
	 customdata - node for the Attributelist given by user
	 datanode - node for the Data Document
	 actualnode - node for the actual position in Data Document
	 filename - the name of the fixture document
	 ubertestname - name of the test
	 -->
<!-- ================================================================================== -->
<xsl:template match ="command" name="command">
	<xsl:param name="ubertestname"/>
	<xsl:param name="defaultnode"/>
	<xsl:param name="datanode"/>
	<xsl:param name="customdata"/>
	<xsl:param name="actualnode"/>
	<xsl:param name="filename"/>
	<xsl:variable name="commandnode" select="self::*"/>
	<xsl:variable name="tmp" select="substring-before($filename, '.')"/>
	<xsl:variable name="htmlfile" select="concat($tmp, '.html')"/>
	<xsl:variable name="tmp3" select="$ubertestname"/>
	<xsl:variable name="tmp2" select="concat($tmp3, '_')"/>
	<xsl:variable name="finalname" select="concat($tmp2, $htmlfile)"/>
	<tr>
		<td><a><xsl:attribute name="href"><xsl:value-of select="$newFileDir"/>/<xsl:value-of select="$finalname"/>#<xsl:value-of select="position()"/></xsl:attribute><xsl:value-of select="@name"/></a></td>
			<xsl:apply-templates select="target">
				<xsl:with-param name="ubertestname" select="$ubertestname"/>
				<xsl:with-param name="datanode" select="$datanode"/>
				<xsl:with-param name="defaultnode" select= "$defaultnode"/>
				<xsl:with-param name="customdata" select="$customdata"/>
				<xsl:with-param name="loopmax" select="@loopmax"/> 
				<xsl:with-param name="filename" select="$filename"/>
				<xsl:with-param name="actualnode" select="$actualnode"/>
			</xsl:apply-templates>
			<xsl:apply-templates select="value">
				<xsl:with-param name="ubertestname" select="$ubertestname"/>
				<xsl:with-param name="datanode" select="$datanode"/>
				<xsl:with-param name="defaultnode" select= "$defaultnode"/>
				<xsl:with-param name="customdata" select="$customdata"/>
				<xsl:with-param name="loopmax" select="@loopmax"/> 
				<xsl:with-param name="filename" select="$filename"/>
				<xsl:with-param name="actualnode" select="$actualnode"/>
			</xsl:apply-templates>
		</tr>
</xsl:template>


<!-- ================================================================================== -->
<!--Template for the 'target' element.	-->
<!-- Param List: 
	 defaultnode - node for the Default Data Document 
	 customdata - node for the Attributelist given by user
	 datanode - node for the Data Document
	 actualnode - node for the actual position in Data Document
	 filename - the name of the fixture document
	 ubertestname - name of the test
	 -->
<!-- ================================================================================== -->
<xsl:template match="target">
	<xsl:param name="ubertestname"/>
	<xsl:param name="defaultnode"/>
	<xsl:param name="datanode"/>
	<xsl:param name="customdata"/>
	<xsl:param name="actualnode"/>
	<xsl:param name="filename"/>
	<xsl:variable name="target" select="var/@*"/>
		<xsl:choose>
				<xsl:when test="$actualnode">
					<xsl:choose>
						<xsl:when test="$actualnode/child::*[name()=$target]">
							<td><xsl:value-of select="$actualnode/child::*[name()=$target]"/></td>
						</xsl:when>
						<xsl:when test="$defaultnode/defaultdata/child::*[name()=$target]">
							<td><xsl:value-of select="$defaultnode/defaultdata/child::*[name()=$target]"/></td>
						</xsl:when>
						<xsl:when test="not(child::*)">
							<td><xsl:value-of select="."/></td>
						</xsl:when>
						<xsl:otherwise>
							<td>&#160;</td>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="not($actualnode)">
					<xsl:choose>
						<xsl:when test="$customdata[name()=$target]">
							<td><xsl:value-of select="$customdata[name()=$target]"/></td>
						</xsl:when>
						<xsl:when test="$defaultnode/defaultdata/child::*[name()=$target]">
							<td><xsl:value-of select="$defaultnode/defaultdata/child::*[name()=$target]"/></td>
						</xsl:when>
						<xsl:when test="not(child::*)">
							<td><xsl:value-of select="."/></td>
						</xsl:when>
						<xsl:otherwise>
							<td>&#160;</td>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
		</xsl:choose>
</xsl:template>




<!-- ================================================================================== -->
<!--Template for the 'value' element.	-->
<!-- Param List: 
	 defaultnode - node for the Default Data Document 
	 customdata - node for the Attributelist given by user
	 datanode - node for the Data Document
	 actualnode - node for the actual position in Data Document
	 filename - the name of the fixture document
	 ubertestname - name of the test
	 -->
<!-- ================================================================================== -->
<xsl:template match="value">
	<xsl:param name="ubertestname"/>
	<xsl:param name="defaultnode"/>
	<xsl:param name="datanode"/>
	<xsl:param name="customdata"/>
	<xsl:param name="actualnode"/>
	<xsl:param name="filename"/>
	<xsl:variable name="value" select="var/@*"/>
		<xsl:choose>
				<xsl:when test="$actualnode">
					<xsl:choose>
						<xsl:when test="$actualnode/child::*[name()=$value]">
							 <td><xsl:value-of select="$actualnode/child::*[name()=$value]"/></td>
						</xsl:when>
						<xsl:when test="$defaultnode/defaultdata/child::*[name()=$value]">
							<td><xsl:value-of select="$defaultnode/defaultdata/child::*[name()=$value]"/></td>
						</xsl:when>
						<xsl:when test="not(child::*)">
							<td><xsl:value-of select="."/></td>
						</xsl:when>
						<xsl:otherwise>
							<td>&#160;</td>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="not($actualnode)">
					<xsl:choose>
						<xsl:when test="$customdata[name()=$value]">
							<td><xsl:value-of select="$customdata[name()=$value]"/></td>
						</xsl:when>
						<xsl:when test="$defaultnode/defaultdata/child::*[name()=$value]">
							<td><xsl:value-of select="$defaultnode/defaultdata/child::*[name()=$value]"/></td>
						</xsl:when>
						<xsl:when test="not(child::*)">
							<td><xsl:value-of select="."/></td>
						</xsl:when>
						<xsl:otherwise>
							<td>&#160;</td>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
		</xsl:choose>
</xsl:template>


<!-- ================================================================================== -->
<!--Template for the 'column' element.	-->
<!--Param list:
	filename - the name of the fixture document 
	ubertestname - name of the test -->
<!-- ================================================================================== -->
<xsl:template match="test/fixture/columns/column">
<xsl:param name="ubertestname"/>
<xsl:param name="filename"/>
<xsl:variable name="tmp" select="substring-before($filename, '.')"/>
<xsl:variable name="htmlfile" select="concat($tmp, '.html')"/>
<xsl:variable name="tmp3" select="$ubertestname"/>
<xsl:variable name="tmp2" select="concat($tmp3, '_')"/>
<xsl:variable name="finalname" select="concat($tmp2, $htmlfile)"/>	
	<td>
	<a><xsl:attribute name="href"><xsl:value-of select="$newFileDir"/>/<xsl:value-of select="$finalname"/>#<xsl:value-of select="position()"/></xsl:attribute><xsl:value-of select="."/></a>
	</td>
</xsl:template>


<!-- ================================================================================== -->
<!-- Template for the 'row' element. Calls: 'child::*' template	-->
<!-- Param List: 
	 defaultnode - node for the Default Data Document 
	 customdata - node for the Attributelist given by user
	 loopmax - max. value for the loop
	 filenode - node for the Fixture Document 
	 datanode - node for the Data Document
	 actualnode - node for the actual position in Data Document
	 -->
<!-- ================================================================================== -->
<xsl:template match = "test/fixture/rows/row">
	<xsl:param name="loopmax"/>
	<xsl:param name="loopnode"/>
	<xsl:param name="datanode"/>
	<xsl:param name="defaultnode"/>
	<xsl:param name="filenode"/>
	<xsl:choose>
		<xsl:when test="$datanode">
			<xsl:for-each select="$datanode/testdata/dataset">
				<xsl:variable name="actualnode" select="self::*"/>
					<tr>
					<xsl:apply-templates select="$filenode/test/fixture/rows/row/child::*">
						<xsl:with-param name="datanode" select="$datanode"/>
						<xsl:with-param name="defaultnode" select="$defaultnode"/>
						<xsl:with-param name="loopmax" select="$loopmax"/>
						<xsl:with-param name="loopnode" select="$loopnode"/>
						<xsl:with-param name="actualnode" select="$actualnode"/>
					</xsl:apply-templates>
				</tr>
			</xsl:for-each>
		</xsl:when>
		<xsl:otherwise>
			<tr>
				<xsl:apply-templates select="child::*">
					<xsl:with-param name="datanode" select="$datanode"/>
					<xsl:with-param name="defaultnode" select="$defaultnode"/>
					<xsl:with-param name="loopmax" select="$loopmax"/>
					<xsl:with-param name="loopnode" select="$loopnode"/>
				</xsl:apply-templates>
			</tr>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>


<!-- ================================================================================== -->
<!-- Template for the 'child::*' element. Calls: 'loop' template	-->
<!-- Param List: 
	 defaultnode - node for the Default Data Document 
	 customdata - node for the Attributelist given by user
	 loopmax - max. value for the loop
	 filenode - node for the Fixture Document 
	 datanode - node for the Data Document
	 actualnode - node for the actual position in Data Document
	 -->
 <!-- ================================================================================== -->
<xsl:template match = "child::*">
<xsl:param name="datanode"/>
<xsl:param name="defaultnode"/>
<xsl:param name="maxrows"/>
<xsl:param name="loopmax"/>
<xsl:param name="loopnode"/>
<xsl:param name="actualnode"/>
<xsl:variable name="varname" select="var/@*"/>
<xsl:variable name="value" select="self::*"/>
<xsl:choose>
	<xsl:when test="$varname">
		<xsl:choose>
			<xsl:when test="$actualnode/child::*[name()=$varname]">
				<td><xsl:value-of select="$actualnode/child::*[name()=$varname]"/></td>
			</xsl:when>
			<xsl:when test="$defaultnode/defaultdata/child::*[name()=$varname]">
				<td><xsl:value-of select="$defaultnode/defaultdata/child::*[name()=$varname]"/></td>
			</xsl:when>
			<xsl:otherwise>
				<td><xsl:value-of select="."/></td>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:when>
	<xsl:otherwise>
		<td><xsl:value-of select="."/></td>
	</xsl:otherwise>
	</xsl:choose>
</xsl:template>


<!-- ================================================================================== -->
<!--Template for the 'loop' element. Calls: 'loop' template	-->
<!--Param List: 
	index - index value
	loopmax - max. value for the loop
	loopnode - node for the loop
	defaultnode - node for the Default Data Document 
	customdata - node for the Attributelist given by user
	loopmax - max. value for the loop
	filenode - node for the Fixture Document 
	datanode - node for the Data Document
	actualnode - node for the actual position in Data Document
	-->
<!-- ================================================================================== -->
<xsl:template name="loop">
<xsl:param name="index"/>
<xsl:param name="loopmax"/>
<xsl:param name="loopnode"/>
<xsl:param name="defaultnode"/>
<xsl:param name="datanode"/>
<xsl:param name="customdata"/>
<xsl:param name="actualnode"/>
<xsl:param name="filename"/>
<xsl:param name="filenode"/>
<xsl:param name="commandnode"/>
<xsl:value-of select="$commandnode"/>
<tr>
<xsl:if test="$index &lt; $loopmax">
	<table cellpadding="1" cellspacing="1" border="1">
			<tbody>
			<tr><td>fit.ActionFixture</td></tr>
		</tbody>
	</table>
	<xsl:call-template name="loop">
			<xsl:with-param name="index"><xsl:value-of select="$index + 1"/></xsl:with-param>
			<xsl:with-param name="loopmax" select="$loopmax"/>
	</xsl:call-template>
</xsl:if>
</tr>
</xsl:template>


	
<!-- ================================================================================== -->
<!-- Template for the 'comment' element. -->
<!-- ================================================================================== -->	
<xsl:template match="comment">
	<xsl:copy-of select="*"/>
</xsl:template>


</xsl:stylesheet>