package lukfor.tables.rows.aggregators;

import java.io.IOException;

import lukfor.tables.Table;
import lukfor.tables.columns.AbstractColumn;
import lukfor.tables.rows.IRowAggregator;

public class SumRowAggregator implements IRowAggregator {

	private String keyColumn;

	private String valueColumn;
	
	public SumRowAggregator(String keyColumn, String valueColumn) {
		this.keyColumn = keyColumn;
		this.valueColumn = valueColumn;
	}

	@Override
	public Table aggregate(Table group) throws IOException {

		Table table = new Table(group.getName() + ":reduced");
		AbstractColumn newKeyColumn = group.getColumn(keyColumn).cloneStructure();
		AbstractColumn newValueColumn = group.getColumn(valueColumn).cloneStructure();
		newValueColumn.setName("sum");

		table.getColumns().append(newKeyColumn);
		table.getColumns().append(newValueColumn);

		Object key = group.get(0, keyColumn);
		Object sum = group.getColumn(valueColumn).getSum();
		
		table.getRows().append(key, sum);

		return table;
	}

}
