package db;

import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.datatype.DataType;

import fit.Fixture;
import fit.Parse;

public class Table extends Fixture {

	private String tableName;
	
	private DefaultTable table;
	
	private boolean columnBinding = false;
	
	private int columnSize;
	
	@Override
	public void doTable(Parse parse) {
		super.doTable(parse);
		DefaultDataSet dataSet = DatabaseContext.getDataSet();
		dataSet.addTable(table);
	}
	
	private void bindColumn(Parse cells) {
		columnSize = cells.size();
		Column[] columns = new Column[columnSize];
		for (int index = 0; index < cells.size(); index++) {
			Parse cell = cells.at(index);
			columns[index] = new Column(cell.text(), DataType.UNKNOWN);
		}
		table = new DefaultTable(tableName, columns);
	}

	public void doCells(Parse cells) {
		if(!columnBinding){
			if(tableName == null)
			{
				tableName = cells.more.text();
				table = new DefaultTable(tableName);
			}
			else
			{
				bindColumn(cells);
				columnBinding = true;
			}
		}
		else{
			Object[] row = new Object[columnSize];
			for (int index = 0; index < columnSize; index++) {
				String text = cells.at(index).text();
				if(text.equals("[NULL]")){
					row[index] = null;
				}
				else
				{
					row[index] = text;
				}
			}
			try {
				table.addRow(row);
			} catch (DataSetException e) {
				exception(cells, e);
			}
		}
	}
	
}
