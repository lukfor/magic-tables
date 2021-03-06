package lukfor.tables.rows.filters;

import lukfor.tables.rows.Row;

public class RowValueRegExFilter implements IRowFilter {

	private String column;

	private String regex;

	public RowValueRegExFilter(String column, String regex) {
		this.column = column;
		this.regex = regex;
	}

	public boolean accepts(Row row) {
		Object value = row.getObject(column);
		if (value != null) {
			return value.toString().matches(regex);
		} else {
			return false;
		}
	}

}
