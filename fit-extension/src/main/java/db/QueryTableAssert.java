package db;

import java.sql.SQLException;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.DataSetException;

public class QueryTableAssert extends TableAssert {

	@Override
	protected void initTable(String query) throws ClassNotFoundException,
			SQLException, DataSetException {
		DatabaseConnection conn = DatabaseContext.createDatabaseConnection();
		try
		{
			actualTable = conn.createQueryTable(null, query);
		}
		finally
		{
			conn.close();
		}
	}
	
}
