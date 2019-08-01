package lukfor.tables.rows.aggregators;

import java.io.IOException;

import lukfor.tables.Table;
import lukfor.tables.columns.AbstractColumn;
import lukfor.tables.columns.types.IntegerColumn;
import lukfor.tables.rows.IRowAggregator;

public class CountRowAggregator implements IRowAggregator {

	private String keyColumn;

	public CountRowAggregator(String keyColumn) {
		this.keyColumn = keyColumn;
	}

	@Override
	public Table aggregate(Object key2, Table group) throws IOException {

		Table table = new Table(group.getName() + ":reduced");
		AbstractColumn newKeyColumn = group.getColumn(keyColumn).cloneStructure();
		table.getColumns().append(newKeyColumn);
		table.getColumns().append(new IntegerColumn("count"));

		Object key = group.get(0, keyColumn);
		int count = group.getRows().getSize();
		
		table.getRows().append(key, count);

		return table;
	}

}
