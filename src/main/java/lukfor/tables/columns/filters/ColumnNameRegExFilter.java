package lukfor.tables.columns.filters;

import lukfor.tables.columns.AbstractColumn;

public class ColumnNameRegExFilter implements IColumnFilter {

	private String regex;

	public ColumnNameRegExFilter(String regex) {
		this.regex = regex;
	}

	public boolean accepts(AbstractColumn column) {
		return column.getName().matches(regex);
	}

}
