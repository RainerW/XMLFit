package net.sourceforge.xmlfit.property;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.xmlfit.data.DataSet;


public class PropertyHelper {

	private static HashMap<String, String> properties = 
		new HashMap<String, String>();
	
	private static DataSet actualDataSet;
	
	private static DataSet defaultDataSet;
	
	public static void addPropertiesFromDataSet(DataSet dataSet)
	{
		actualDataSet = dataSet;
	}
	
	public static void addProperty(String name, String value)
	{
		properties.put(name, value);
	}
	
	public static String getPropertyValue(String name)
	{
		String value = null;
		if(actualDataSet != null)
		{
			value = actualDataSet.getPropertyValue(name);
		}
		if(value == null)
		{
			value = properties.get(name);
			if(value == null)
			{
				throw new PropertyNotFoundException(name);
			}
		}
		return value;
	}
	
	public static void clearProperties()
	{
		properties = new HashMap<String, String>();
		actualDataSet = null;
	}
	
	public static String extractProperties(String value)
	{
		if(value == null)
		{
			return null;
		}
		String result = value;
		Pattern pattern = Pattern.compile("\\$\\{[^}]*\\}");
		Matcher matcher = pattern.matcher(value);
		
		while(matcher.find())
		{
			int start = matcher.start();
			int end = matcher.end();
			String propertyName = value.substring(start, end);
			String name = value.substring(start+2,end-1);
			result = result.replace(propertyName, getPropertyValue(name));
		}
		return result;
	}
	
}
