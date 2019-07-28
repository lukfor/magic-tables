package lukfor.tables.rows.filters;

import java.io.IOException;

import lukfor.tables.rows.Row;

public interface IRowFilter {

	public boolean accepts(Row row) throws IOException;
	
}
