package net.sourceforge.xmlfit.fixture;


public class CounterHelper {

	private static int count = 0;
	
	
	public static void next()
	{
		count++;
	}
	
	public static void reset()
	{
		count = 0;
	}
	
	public static Integer getValue()
	{
		return Integer.valueOf(count);
	}
	
}
