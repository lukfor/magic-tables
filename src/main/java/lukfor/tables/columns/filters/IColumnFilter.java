package lukfor.tables.columns.filters;

import java.io.IOException;

import lukfor.tables.columns.AbstractColumn;

public interface IColumnFilter {

	public boolean accepts(AbstractColumn column) throws IOException;

}
