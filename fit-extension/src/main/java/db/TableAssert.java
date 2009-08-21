package db;

import java.sql.SQLException;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;

import fit.Fixture;
import fit.Parse;

public class TableAssert extends Fixture {
	
	private boolean queryExecuted = false;
	
	private boolean errorFound = false;
	
	protected ITable actualTable;
	
	private String[] columns;
	
	private int row = 0;
	
	@Override
	public void doTable(Parse table) {
		super.doTable(table);
//		tearDown(table);
	}
	
	private void tearDown(Parse cells) {
		if(row != actualTable.getRowCount())
		{
			error(cells, "Actual row count is :" 
					+ actualTable.getRowCount()+1 + " expected row cont is :" + row+1);
		}
	}

	@Override
	public void doCells(Parse cells) {
		if(!queryExecuted)
		{
			try {
				initTable(cells.more.text());
				queryExecuted = true;
			} 
			catch (Exception e) 
			{
				errorFound = true;
				exception(cells, e);
			}
		}
		else if(errorFound)
		{
			ignore(cells);
		}
		else
		{
			try {
				checkCells(cells);
			} catch (DataSetException e) {
				exception(cells, e);
			}
		}
	}

	protected void initTable(String tableName) throws ClassNotFoundException, SQLException, DataSetException {
		DatabaseConnection conn = DatabaseContext.createDatabaseConnection();
		try
		{
			actualTable = conn.createDataSet().getTable(tableName);
		}
		finally
		{
			conn.close();
		}
	}


	private void checkCells(Parse cells) throws DataSetException 
	{
		if(columns == null)
		{
			int size = cells.size();
			columns = new String[size];
			for (int index = 0; index < size; index++) {
				columns[index] = cells.at(index).text();
			}
		}
		else
		{
			int size = cells.size();
			for (int index = 0; index < size; index++) {
				Parse cell = cells.at(index);
				Object actual = actualTable.getValue(row, columns[index]);
				String expected = cell.text();
				if(expected.equals("[NULL]"))
				{
					if(actual == null)
					{
						right(cell);
					}
					else
					{
						wrong(cell, actual.toString());
					}
				}
				else if(actual.toString().equals(expected)){
					right(cell);
				}
				else
				{
					wrong(cell, actual.toString());
				}
			}
			row++;
		}
	}
	
	
	
}
