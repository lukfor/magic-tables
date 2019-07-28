package lukfor.tables.columns.filters;

import java.io.IOException;

import lukfor.tables.columns.AbstractColumn;

public class ColumnNameFilter implements IColumnFilter{

	private String[] names;
	
	public ColumnNameFilter(String...names) {
		this.names = names;
	}
	
	public boolean accepts(AbstractColumn column) throws IOException {
		for (String n : names) {
			if (column.getName().equals(n)) {
				return true;
			}
		}
		return false;
	}
	
}
