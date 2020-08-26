package lukfor.tables.columns.filters;

import lukfor.tables.columns.AbstractColumn;

public class ColumnNameFilter implements IColumnFilter{

	private String[] names;
	
	public ColumnNameFilter(String...names) {
		this.names = names;
	}
	
	public boolean accepts(AbstractColumn column) {
		for (String n : names) {
			if (column.getName().equals(n)) {
				return true;
			}
		}
		return false;
	}
	
}
