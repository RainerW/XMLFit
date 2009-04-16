package net.sourceforge.xmlfit.fixture;

import org.eclipse.emf.ecore.EObject;

public class TableHelper {

	public static String maxCellCount(EObject table)
	{
		int maxCount = 0;
		for (EObject row : table.eContents()) {
			int cellCount = 0;
			for (EObject cell : row.eContents()) {
				cellCount++;
			}
			if(cellCount > maxCount)
			{
				maxCount = cellCount;
			}
		}
		return String.valueOf(maxCount);
	}
	
}
