package lukfor.tables.columns.filters;

import lukfor.tables.columns.AbstractColumn;

public interface IColumnFilter {

	public boolean accepts(AbstractColumn column);

}
