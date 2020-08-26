package lukfor.tables.rows;

import java.util.HashMap;
import java.util.Map;

import lukfor.tables.Table;
import lukfor.tables.columns.AbstractColumn;
import lukfor.tables.exceptions.TableException;

public class TableIndex {

	private Table table;

	private Map<Object, Integer> index;

	public TableIndex(Table table) {
		this.table = table;
	}

	public void build(AbstractColumn column) {
		index = new HashMap<Object, Integer>();
		for (int i = 0; i < column.getSize(); i++) {
			Object value = column.get(i);
			if (index.containsKey(value)) {
				throw new TableException("Index creation not possible. Values in column '" + column
						+ "' are not unique. For example '" + value + "' found twice.");
			}
			index.put(value, i);
		}
	}

	public Row getRow(Object value) {
		Integer position = index.get(value);
		if (position != null) {
			return table.getRows().get(position);
		} else {
			return null;
		}
	}

}
