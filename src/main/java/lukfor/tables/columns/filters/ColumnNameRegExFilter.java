package lukfor.tables.columns.filters;

import java.io.IOException;

import lukfor.tables.columns.AbstractColumn;

public class ColumnNameRegExFilter implements IColumnFilter {

	private String regex;

	public ColumnNameRegExFilter(String regex) {
		this.regex = regex;
	}

	public boolean accepts(AbstractColumn column) throws IOException {
		return column.getName().matches(regex);
	}

}
