package lukfor.tables.rows;

import java.io.IOException;
import java.util.List;

import lukfor.tables.columns.AbstractColumn;

public interface IRowReducer {

	public List<AbstractColumn> getColumns() throws IOException;
	
	public Row reduce(Object key, List<Row> rows) throws IOException;
	
}
