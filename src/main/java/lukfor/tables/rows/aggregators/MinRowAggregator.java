package lukfor.tables.rows.aggregators;

import lukfor.tables.Table;
import lukfor.tables.columns.AbstractColumn;
import lukfor.tables.rows.IRowAggregator;
import lukfor.tables.rows.Row;

public class MinRowAggregator implements IRowAggregator {

	private String keyColumn;

	private String valueColumn;

	public MinRowAggregator(String keyColumn, String valueColumn) {
		this.keyColumn = keyColumn;
		this.valueColumn = valueColumn;
	}

	@Override
	public Table aggregate(Object key2, Table group) {

		Table table = new Table(group.getName() + ":reduced");
		AbstractColumn newKeyColumn = group.getColumn(keyColumn).cloneStructure();
		AbstractColumn newValueColumn = group.getColumn(valueColumn).cloneStructure();
		newValueColumn.setName("min");

		table.getColumns().append(newKeyColumn);
		table.getColumns().append(newValueColumn);

		Object key = group.get(0, keyColumn);
		Object min = group.getColumn(valueColumn).getMin();

		Row row = table.getRows().append();
		row.set(0, key);
		row.set(1, min);

		return table;
	}

}
