<?xml version = "1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:date="xalan://java.util.Date" xmlns:redirect="http://xml.apache.org/xalan/redirect" xmlns:common="http://exslt.org/common"
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
<xsl:param name="output_dir" select="'outputdir'"/>
<xsl:param name="test_dir" select="'testdir'"/>
<xsl:param name="input_dir" select="'inputDir'"/>
<xsl:param name="css_file" select="'cssfile'"/>
<xsl:variable name="newDir" select="concat($test_dir, '/')"/>
<xsl:param name="file_dir" select="'filedir'"/>
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
<xsl:param name="customdata"/>
<xsl:param name="testsuitenode"/>
<xsl:choose>
	<xsl:when test="$testsuitenode">
		<xsl:apply-templates select="comment"/>
		<xsl:apply-templates select="testgroup">
			<xsl:with-param name="author" select="@author"/>
			<xsl:with-param name="testsuitenode" select="$testsuitenode"/>
			<xsl:with-param name="customdata" select="$customdata"/>
		</xsl:apply-templates>
	</xsl:when>
<xsl:otherwise>
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
			<xsl:with-param name="customdata" select="$customdata"/>
		</xsl:apply-templates>
		<table>
		<xsl:for-each select="testgroup">
			<xsl:variable name="position" select="concat(position(),'-')"/>
			<xsl:variable name="testname" select="@name"/>
			<xsl:variable name="linkname" select="concat($testname, '.html')"/>
			<tr>
				<td>
					<a><xsl:attribute name="href"><xsl:value-of select="$base_dir"/>/<xsl:value-of select="$position"/><xsl:value-of select="$linkname"/></xsl:attribute><xsl:value-of select="$testname"/></a>
				</td>
			</tr>
		</xsl:for-each>
		</table>
		</div>
	</body>
</html>
</xsl:otherwise>
</xsl:choose>
</xsl:template>

<!-- ================================================================================== -->
<!-- Template for the 'test' element. Calls: 'call' template	-->
<!-- Param List: 
	 defaultnode - Node for the DefaultData Document 
	 author - name of the test author
	 testgroupname - name of the called testgroup
	 testsuitnode - position of the called testsuite
	-->
<!-- ================================================================================== -->
<xsl:template match="testgroup">
	<xsl:param name="customdata"/>
	<xsl:param name="author"/>
	<xsl:param name="testgroupname"/>
	<xsl:param name="testsuitenode"/>
	<xsl:variable name="defaultFileName" select="@defaultData"/>
	<xsl:variable name="defaultnode" select="document($defaultFileName)"/>
	<xsl:variable name="testname" select="@name"/>
	<xsl:variable name="testID" select="@id"/>
	<xsl:variable name="outputname" select="concat($testname, '.html')"/>
	<xsl:variable name="position" select="concat(position(),'-')"/>
	<xsl:variable name="outputnamewithposition" select="concat($position, $outputname)"/>
	<xsl:variable name="outputnamewithDir" select="concat($newDir, $outputnamewithposition)"/>
	<xsl:variable name="testsuiteDir" select="concat($base_dir, $outputname)"/>
	<xsl:choose>
		<xsl:when test="$testsuitenode">
				<xsl:apply-templates select="call">
					<xsl:with-param name="defaultnode" select="$defaultnode"/>
					<xsl:with-param name="ubertestname" select="$testname"/>
					<xsl:with-param name="author" select="$author"/> 
					<xsl:with-param name="customdata" select="$customdata"/>
				</xsl:apply-templates>
		</xsl:when>
		<xsl:when test="$testgroupname">
			<xsl:apply-templates select="call">
					<xsl:with-param name="defaultnode" select="$defaultnode"/>
					<xsl:with-param name="ubertestname" select="$testname"/>
					<xsl:with-param name="author" select="$author"/> 
					<xsl:with-param name="customdata" select="$customdata"/>
				</xsl:apply-templates>
		</xsl:when>
		<xsl:otherwise>
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
					<xsl:with-param name="customdata" select="$customdata"/>
				</xsl:apply-templates>
			</body>
		</html>	
	</redirect:write>
	</xsl:otherwise>
	</xsl:choose>
 </xsl:template>


<!-- ================================================================================== -->
<!-- Template for the 'call' element. Calls: 'command' template, 'column' template, 'row' template	-->
<!-- Param List: 
	 author - name of the test author
	 defaultnode - node for the Default Data Document 
	 customdata - node for the Attributelist given by user
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

	<xsl:variable name="actualcustomdata" select="@*"/>
	<xsl:variable name="filename" select="@test"/>
	<xsl:variable name="testname" select="@name"/>
	<xsl:variable name="choosevalue" select="'testsuite'"/>
	<xsl:variable name="filenode" select="document($filename)"/>
	

	
	
	<xsl:apply-templates select="$filenode/test/call">
		<xsl:with-param name="customdata" select="@*"/>
	</xsl:apply-templates>
		
	
		<xsl:variable name="datanode" select="document(@data)"/>
		<xsl:if test="not(@testsuite) and not(@testgroup)">
		</xsl:if>
			<xsl:choose>
				<xsl:when test="@testsuite and not(@testgroup)">
					<xsl:variable name="testsuitename" select="@testsuite"/>
					<xsl:variable name="testsuitenode" select="document($testsuitename)"/>
					<xsl:apply-templates select="$testsuitenode/testsuite">
						<xsl:with-param name="testsuitenode" select="$testsuitenode"/>
						<xsl:with-param name="customdata" select="@*"/>
					</xsl:apply-templates>
				</xsl:when>
				<xsl:when test="@testgroup and not(@test)">
					<xsl:variable name="testsuitename" select="@testsuite"/>
					<xsl:variable name="testgroupname" select="@testgroup"/>
					<xsl:variable name="testsuitenode" select="document($testsuitename)"/>
					<xsl:apply-templates select="$testsuitenode/testsuite/testgroup[@name=$testgroupname]">
						<xsl:with-param name="testgroupname" select="$testgroupname"/>
						<xsl:with-param name="customdata" select="@*"/>
					</xsl:apply-templates>
				</xsl:when>
				<xsl:when test="@testgroup and @test">
					<xsl:variable name="testsuitename" select="@testsuite"/>
					<xsl:variable name="testgroupname" select="@testgroup"/>
					<xsl:variable name="exttestname" select="@test"/>
					<xsl:variable name="testsuitenode" select="document($testsuitename)"/>
					<xsl:apply-templates select="$testsuitenode/testsuite/testgroup[@name=$testgroupname]/call[@test=$exttestname]">
						<xsl:with-param name="testgroupname" select="$testgroupname"/>
						<xsl:with-param name="customdata" select="@*"/>
					</xsl:apply-templates>
				</xsl:when>
				<xsl:when test="$filenode/test/fixture[@type='selenium.SeleniumFixture']">
				<xsl:apply-templates select="$filenode/test/comment"/>
				<xsl:choose>
				<xsl:when test="$datanode and $filenode/test/fixture/command and not($filenode/test/fixture/columns)">
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
								<xsl:apply-templates select="$filenode/test/fixture/command">
									<xsl:with-param name="datanode" select="$datanode"/>
									<xsl:with-param name="defaultnode" select= "$defaultnode"/>
									<xsl:with-param name="customdata" select="$customdata"/>
									<xsl:with-param name="filename" select="$filename"/>
									<xsl:with-param name="ubertestname" select="$ubertestname"/>
									<xsl:with-param name="actualnode" select="$actualnode"/>
									<xsl:with-param name="actualcustomdata" select="$actualcustomdata"/>
								</xsl:apply-templates>
							<xsl:if test="$filenode/test/fixture/columns">
								<tr><xsl:apply-templates select="$filenode/test/fixture/columns/column">
									<xsl:with-param name="filename" select="$filename"/>
									<xsl:with-param name="ubertestname" select="$ubertestname"/>
								</xsl:apply-templates></tr>
							</xsl:if>
							<xsl:apply-templates select ="$filenode/test/fixture/rows/row">
								<xsl:with-param name="datanode" select="$datanode"/>
								<xsl:with-param name="defaultnode" select= "$defaultnode"/> 
								<xsl:with-param name="customdata" select="$customdata"/>
								<xsl:with-param name="filenode" select="document($filename)"/>
								<xsl:with-param name="actualcustomdata" select="$actualcustomdata"/>
							</xsl:apply-templates>
						</tbody>
					</table>
					</xsl:for-each>
					</xsl:when>
					<xsl:otherwise>
						<xsl:variable name="actualnode" select="self::*"/>
					<xsl:apply-templates select="$filenode/test/fixture/comment"/>
					<table cellpadding="0" cellspacing="0" border="1">
						<tbody>
								<xsl:apply-templates select="$filenode/test/fixture/command">
									<xsl:with-param name="datanode" select="$datanode"/>
									<xsl:with-param name="defaultnode" select= "$defaultnode"/>
									<xsl:with-param name="customdata" select="$customdata"/>
									<xsl:with-param name="filename" select="$filename"/>
									<xsl:with-param name="ubertestname" select="$ubertestname"/>
									<xsl:with-param name="actualnode" select="$actualnode"/>
									<xsl:with-param name="actualcustomdata" select="$actualcustomdata"/>
								</xsl:apply-templates>
							<xsl:if test="$filenode/test/fixture/columns">
								<tr><xsl:apply-templates select="$filenode/test/fixture/columns/column">
									<xsl:with-param name="filename" select="$filename"/>
									<xsl:with-param name="ubertestname" select="$ubertestname"/>
								</xsl:apply-templates></tr>
							</xsl:if>
							<xsl:apply-templates select ="$filenode/test/fixture/rows/row">
								<xsl:with-param name="datanode" select="$datanode"/>
								<xsl:with-param name="defaultnode" select= "$defaultnode"/> 
								<xsl:with-param name="customdata" select="$customdata"/>
								<xsl:with-param name="filenode" select="document($filename)"/>
								<xsl:with-param name="actualcustomdata" select="$actualcustomdata"/>
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
	<xsl:param name="actualcustomdata"/>
	<xsl:param name="actualnode"/>
	<xsl:param name="filename"/>
	<xsl:variable name="commandnode" select="self::*"/>
	<xsl:variable name="tmp" select="substring-before($filename, '.')"/>
	<xsl:variable name="htmlfile" select="concat($tmp, '.html')"/>
	<xsl:variable name="tmp3" select="$ubertestname"/>
	<xsl:variable name="tmp2" select="concat($tmp3, '_')"/>
	<xsl:variable name="finalname" select="concat($tmp2, $htmlfile)"/>
	<tr>
		<td><xsl:value-of select="@name"/></td>
			<xsl:apply-templates select="target">
				<xsl:with-param name="ubertestname" select="$ubertestname"/>
				<xsl:with-param name="datanode" select="$datanode"/>
				<xsl:with-param name="defaultnode" select= "$defaultnode"/>
				<xsl:with-param name="customdata" select="$customdata"/>
				<xsl:with-param name="filename" select="$filename"/>
				<xsl:with-param name="actualnode" select="$actualnode"/>
				<xsl:with-param name="actualcustomdata" select="$actualcustomdata"/>
			</xsl:apply-templates>
			<xsl:apply-templates select="value">
				<xsl:with-param name="ubertestname" select="$ubertestname"/>
				<xsl:with-param name="datanode" select="$datanode"/>
				<xsl:with-param name="defaultnode" select= "$defaultnode"/>
				<xsl:with-param name="customdata" select="$customdata"/>
				<xsl:with-param name="filename" select="$filename"/>
				<xsl:with-param name="actualnode" select="$actualnode"/>
				<xsl:with-param name="actualcustomdata" select="$actualcustomdata"/>
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
	<xsl:param name="actualcustomdata"/>
	<xsl:variable name="target" select="var/@name"/>
	<xsl:call-template name="var" >
		<xsl:with-param name="ubertestname" select="$ubertestname" />
		<xsl:with-param name="defaultnode" select="defaultnode" />
		<xsl:with-param name="datanode" select="datanode" />
		<xsl:with-param name="customdata" select="$customdata" />
		<xsl:with-param name="actualnode" select="$actualnode" />
		<xsl:with-param name="filename" select="$filename" />
		<xsl:with-param name="varnamenode" select="$target" />
		<xsl:with-param name="actualcustomdata" select="$actualcustomdata"/>
	</xsl:call-template>	
</xsl:template>

<!-- Template for VAR -->
<xsl:template name="var">
	
	<xsl:param name="ubertestname"/>
	<xsl:param name="defaultnode"/>
	<xsl:param name="datanode"/>
	<xsl:param name="customdata"/>
	<xsl:param name="actualnode"/>
	<xsl:param name="filename"/>
	<xsl:param name="varnamenode" />
	<xsl:param name="actualcustomdata"/>
	
	<xsl:variable name="prefixText" select="var/@prefixText"/>
	<xsl:variable name="suffixText" select="var/@suffixText"/>
		<td>
		<xsl:value-of select="$prefixText" />
		
		<!--  Write Content of VAR -->
		<xsl:choose>
				<xsl:when test="$actualnode">
					
					<!-- VAR Data from File -->
					<xsl:choose>
						<xsl:when test="$actualnode/child::*[name()=$varnamenode]">
							<xsl:value-of select="$actualnode/child::*[name()=$varnamenode]"/>
						</xsl:when>
						<xsl:when test="$defaultnode/defaultdata/child::*[name()=$varnamenode]">
							<xsl:value-of select="$defaultnode/defaultdata/child::*[name()=$varnamenode]"/>
						</xsl:when>
						
						<xsl:when test="not(child::*)">
							<xsl:value-of select="."/>
						</xsl:when>
						
						<xsl:when test="var/@default">
							<xsl:value-of select="var/@default"/>
						</xsl:when>
						
						<xsl:otherwise>
							&#160;
						</xsl:otherwise>
					</xsl:choose>
					
					
				</xsl:when>
				
				<!-- VAR Data from Attribute -->
				<xsl:when test="not($actualnode)">
					<xsl:choose>
				
						<xsl:when test="common:node-set($customdata)[name()=$varnamenode]">
							<xsl:value-of select="$customdata[name()=$varnamenode]"/>
						</xsl:when>
						
						<xsl:when test="common:node-set($actualcustomdata)[name()=$varnamenode]">
							<xsl:value-of select="$actualcustomdata[name()=$varnamenode]"/>
						</xsl:when>
							
						<xsl:when test="$defaultnode/defaultdata/child::*[name()=$varnamenode]">
							<xsl:value-of select="$defaultnode/defaultdata/child::*[name()=$varnamenode]"/>
						</xsl:when>
						
						<xsl:when test="not(child::*)">
							<xsl:value-of select="."/>
						</xsl:when>
						
						<xsl:when test="var/@default">
							<xsl:value-of select="var/@default"/>
						</xsl:when>
						
						<xsl:otherwise>
							&#160;
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				
		</xsl:choose>
		<xsl:value-of select="$suffixText" />
		</td>

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
	<xsl:param name="actualcustomdata"/>
	<xsl:param name="ubertestname"/>
	<xsl:param name="defaultnode"/>
	<xsl:param name="datanode"/>
	<xsl:param name="customdata"/>
	<xsl:param name="actualnode"/>
	<xsl:param name="filename"/>
	<xsl:variable name="value" select="var/@name"/>
	<xsl:call-template name="var" >
		<xsl:with-param name="ubertestname" select="$ubertestname" />
		<xsl:with-param name="defaultnode" select="defaultnode" />
		<xsl:with-param name="datanode" select="datanode" />
		<xsl:with-param name="customdata" select="$customdata" />
		<xsl:with-param name="actualnode" select="$actualnode" />
		<xsl:with-param name="filename" select="$filename" />
		<xsl:with-param name="varnamenode" select="$value" />
		<xsl:with-param name="actualcustomdata" select="$actualcustomdata"/>
	</xsl:call-template>	
	
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
	<a><xsl:attribute name="href">file://<xsl:value-of select="$project_dir"/>/<xsl:value-of select="$output_dir"/>/<xsl:value-of select="$file_dir"/>/<xsl:value-of select="$finalname"/>#<xsl:value-of select="position()"/></xsl:attribute><xsl:value-of select="."/></a>
	</td>
</xsl:template>


<!-- ================================================================================== -->
<!-- Template for the 'row' element. Calls: 'child::*' template	-->
<!-- Param List: 
	 defaultnode - node for the Default Data Document 
	 filenode - node for the Fixture Document 
	 datanode - node for the Data Document
	 actualnode - node for the actual position in Data Document
	 -->
<!-- ================================================================================== -->
<xsl:template match = "test/fixture/rows/row">
	<xsl:param name="datanode"/>
	<xsl:param name="defaultnode"/>
	<xsl:param name="filenode"/>
	<xsl:param name="customdata"/>
	<xsl:param name="actualcustomdata"/>
	<xsl:choose>
		<xsl:when test="$datanode and child::*/var">
			<xsl:variable name="actualfilenode" select="self::*"/>
			<xsl:for-each select="$datanode/testdata/dataset">
				<xsl:variable name="actualnode" select="self::*"/>
					<tr>
						<xsl:apply-templates select="$actualfilenode/child::*">
						<xsl:with-param name="datanode" select="$datanode"/>
						<xsl:with-param name="defaultnode" select="$defaultnode"/>
						<xsl:with-param name="actualnode" select="$actualnode"/>
						<xsl:with-param name="actualfilenode" select="$actualfilenode"/>
					</xsl:apply-templates>
				</tr>
			</xsl:for-each>
		</xsl:when>
		<xsl:otherwise>
			<tr>
				<xsl:variable name="actualfilenode" select="self::*"/>
				<xsl:apply-templates select="child::*">
					<xsl:with-param name="datanode" select="$datanode"/>
					<xsl:with-param name="customdata" select="$customdata"/>
					<xsl:with-param name="defaultnode" select="$defaultnode"/>
					<xsl:with-param name="actualfilenode" select="$actualfilenode"/>
					<xsl:with-param name="actualcustomdata" select="$actualcustomdata"/>
				</xsl:apply-templates>
			</tr>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>


<!-- ================================================================================== -->
<!-- Template for the 'child::*' element.-->
<!-- Param List: 
	 defaultnode - node for the Default Data Document 
	 filenode - node for the Fixture Document 
	 datanode - node for the Data Document
	 actualnode - node for the actual position in Data Document
	 -->
 <!-- ================================================================================== -->
<xsl:template match ="child::*">
<xsl:param name="datanode"/>
<xsl:param name="defaultnode"/>
<xsl:param name="actualnode"/>
<xsl:param name="customdata"/>
<xsl:param name="actualcustomdata"/>
<xsl:variable name="varnamenode" select="var/@name"/>
<xsl:variable name="value" select="self::*"/>
	<xsl:call-template name="var" >
		<xsl:with-param name="defaultnode" select="defaultnode" />
		<xsl:with-param name="datanode" select="datanode" />
		<xsl:with-param name="customdata" select="$customdata" />
		<xsl:with-param name="actualnode" select="$actualnode" />
		<xsl:with-param name="varnamenode" select="$varnamenode" />
		<xsl:with-param name="actualcustomdata" select="$actualcustomdata"/>
	</xsl:call-template>	
</xsl:template>

	
<!-- ================================================================================== -->
<!-- Template for the 'comment' element. -->
<!-- ================================================================================== -->	
<xsl:template match="comment">
	<xsl:copy-of select="*"/>
</xsl:template>


</xsl:stylesheet>