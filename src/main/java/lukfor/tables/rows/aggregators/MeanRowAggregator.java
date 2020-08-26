package lukfor.tables.rows.aggregators;

import lukfor.tables.Table;
import lukfor.tables.columns.AbstractColumn;
import lukfor.tables.columns.types.DoubleColumn;
import lukfor.tables.rows.IRowAggregator;
import lukfor.tables.rows.Row;

public class MeanRowAggregator implements IRowAggregator {

	private String keyColumn;

	private String valueColumn;

	public MeanRowAggregator(String keyColumn, String valueColumn) {
		this.keyColumn = keyColumn;
		this.valueColumn = valueColumn;
	}

	@Override
	public Table aggregate(Object key2, Table group) {

		Table table = new Table(group.getName() + ":reduced");
		AbstractColumn newKeyColumn = group.getColumn(keyColumn).cloneStructure();
		AbstractColumn newValueColumn = new DoubleColumn("mean");

		table.getColumns().append(newKeyColumn);
		table.getColumns().append(newValueColumn);

		Object key = group.get(0, keyColumn);
		Object mean = group.getColumn(valueColumn).getMean();

		Row row = table.getRows().append();
		row.set(0, key);
		row.set(1, mean);

		return table;
	}

}
