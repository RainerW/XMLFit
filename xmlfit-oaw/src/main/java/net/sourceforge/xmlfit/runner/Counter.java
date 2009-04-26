package net.sourceforge.xmlfit.runner;

public class Counter {

	public int success = 0;
	
	public int failed = 0;
	
	public void add(Counter counter)
	{
		success += counter.success;
		failed += counter.failed;
	}
	
}
