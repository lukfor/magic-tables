package lukfor.tables.rows;

import lukfor.tables.Table;
import lukfor.tables.columns.types.DoubleColumn;

public class Row {

	private Table table;

	private int index;

	public Row(Table table, int index) {
		this.table = table;
		this.index = index;
	}

	public void updateView(Table table, int index) {
		this.table = table;
		this.index = index;
	}

	public void fill(Row row) {
		for (String column : row.table.getColumns().getNames()) {
			set(column, row.getObject(column));
		}
	}

	public Object getObject(String column) {
		return table.get(index, column);
	}

	public String getString(String column) {
		return (String) getObject(column);
	}

	public int getInteger(String column) {
		return (Integer) getObject(column);
	}

	public double getDouble(String column) {
		return (Double) getObject(column);
	}

	public Object getObject(int column) {
		return table.get(index, column);
	}

	public String getString(int column) {
		return (String) getObject(column);
	}

	public int getInteger(int column) {
		return (Integer) getObject(column);
	}

	public double getDouble(int column) {
		return (Double) getObject(column);
	}

	public void set(String column, Object value) {
		table.getColumn(column).set(index, value);
	}

	public void set(int column, Object value) {
		table.getColumn(column).set(index, value);
	}

	public void set(Object[] values) {
		for (int i = 0; i < values.length; i++) {
			table.getColumn(i).set(index, values[i]);
		}
	}

	public void setString(String column, Object value) {
		if (value != null) {

			if (value instanceof Double) {
				value = DoubleColumn.FORMAT.format(value);
			}
			table.getColumn(column).set(index, value.toString());

		} else {
			table.getColumn(column).set(index, null);
		}
	}

	public boolean hasMissings() {
		for (int i = 0; i < table.getColumns().getSize(); i++) {
			if (table.get(index, i) == null) {
				return true;
			}
		}
		return false;
	}

	public int getHashCode() {

		// use same hashcode as in Objects.Util but without any additional memory
		// hashcode is used to check for duplicate rows

		int result = 1;
		for (int i = 0; i < table.getColumns().getSize(); i++) {
			Object element = table.get(index, i);
			result = 31 * result + (element == null ? 0 : element.hashCode());
		}

		return result;

	}

	public int getIndex() {
		return index;
	}

}
