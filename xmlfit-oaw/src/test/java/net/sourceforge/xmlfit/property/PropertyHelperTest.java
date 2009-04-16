package net.sourceforge.xmlfit.property;

import static org.junit.Assert.*;

import net.sourceforge.xmlfit.property.PropertyHelper;
import net.sourceforge.xmlfit.property.PropertyNotFoundException;

import org.junit.Before;
import org.junit.Test;

public class PropertyHelperTest {

	@Before
	public void setUp()
	{
		PropertyHelper.clearProperties();
		PropertyHelper.addProperty("username", "baranowski");
		PropertyHelper.addProperty("password", "pwd");
	}
	
	@Test
	public void testExtractProperties_OneProperty() 
	{
		String value = PropertyHelper.extractProperties("Test${username}Test");
		assertEquals("TestbaranowskiTest", value);
	}
	
	@Test
	public void testExtractProperties_OnePropertyWithoutText() 
	{
		String value = PropertyHelper.extractProperties("${username}");
		assertEquals("baranowski", value);
	}
	
	@Test
	public void testExtractProperties_TwoProperty() 
	{
		String value = PropertyHelper.extractProperties("${username} ${password}");
		assertEquals("baranowski pwd", value);
	}
	
	@Test(expected=PropertyNotFoundException.class)
	public void testExtractProperties_UnknownProperty() 
	{
		PropertyHelper.extractProperties("Test${testproperty}Test");
	}

}
