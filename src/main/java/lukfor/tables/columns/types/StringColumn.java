package lukfor.tables.columns.types;

import lukfor.tables.columns.AbstractColumn;
import lukfor.tables.columns.ColumnType;

public class StringColumn extends AbstractColumn {

	public StringColumn(String name) {
		setName(name);
	}

	@Override
	public ColumnType getType() {
		return ColumnType.STRING;
	}

	@Override
	public Object valueToObject(String data) {
		if (data.isEmpty()) {
			return null;
		} else {
			return data;
		}
	}

	@Override
	public String objectToValue(Object data) {
		if (data != null) {
			return data.toString();
		} else {
			return "";
		}
	}

	@Override
	public int compare(Object value1, Object value2) {
		return value1.toString().compareTo(value2.toString());
	}

	@Override
	public boolean accepts(Object data) {
		return data instanceof String;
	}

}
