package lukfor.tables.rows.filters;

import lukfor.tables.rows.Row;

public interface IRowFilter {

	public boolean accepts(Row row);
	
}
