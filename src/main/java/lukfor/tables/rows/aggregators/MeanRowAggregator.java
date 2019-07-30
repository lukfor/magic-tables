package lukfor.tables.rows.aggregators;

import java.io.IOException;

import lukfor.tables.Table;
import lukfor.tables.columns.AbstractColumn;
import lukfor.tables.columns.types.DoubleColumn;
import lukfor.tables.rows.IRowAggregator;

public class MeanRowAggregator implements IRowAggregator {

	private String keyColumn;

	private String valueColumn;

	public MeanRowAggregator(String keyColumn, String valueColumn) {
		this.keyColumn = keyColumn;
		this.valueColumn = valueColumn;
	}

	@Override
	public Table aggregate(Table group) throws IOException {

		Table table = new Table(group.getName() + ":reduced");
		AbstractColumn newKeyColumn = group.getColumn(keyColumn).cloneStructure();
		AbstractColumn newValueColumn = new DoubleColumn("mean");

		table.getColumns().append(newKeyColumn);
		table.getColumns().append(newValueColumn);

		Object key = group.get(0, keyColumn);
		Object mean = group.getColumn(valueColumn).getMean();

		table.getRows().append(key, mean);

		return table;
	}

}
