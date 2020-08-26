package lukfor.tables.rows.aggregators;

import lukfor.tables.Table;
import lukfor.tables.columns.AbstractColumn;
import lukfor.tables.rows.IRowAggregator;
import lukfor.tables.rows.Row;

public class SumRowAggregator implements IRowAggregator {

	private String keyColumn;

	private String valueColumn;
	
	public SumRowAggregator(String keyColumn, String valueColumn) {
		this.keyColumn = keyColumn;
		this.valueColumn = valueColumn;
	}

	@Override
	public Table aggregate(Object key2, Table group) {

		Table table = new Table(group.getName() + ":reduced");
		AbstractColumn newKeyColumn = group.getColumn(keyColumn).cloneStructure();
		AbstractColumn newValueColumn = group.getColumn(valueColumn).cloneStructure();
		newValueColumn.setName("sum");

		table.getColumns().append(newKeyColumn);
		table.getColumns().append(newValueColumn);

		Object key = group.get(0, keyColumn);
		Object sum = group.getColumn(valueColumn).getSum();
		
		Row row = table.getRows().append();
		row.set(0, key);
		row.set(1, sum);

		return table;
	}

}
