package lukfor.tables.utils;

import lukfor.tables.Table;
import lukfor.tables.rows.IRowAggregator;
import lukfor.tables.rows.IRowMapper;
import lukfor.tables.rows.aggregators.CountRowAggregator;
import lukfor.tables.rows.aggregators.MaxRowAggregator;
import lukfor.tables.rows.aggregators.MeanRowAggregator;
import lukfor.tables.rows.aggregators.MinRowAggregator;
import lukfor.tables.rows.aggregators.SumRowAggregator;

public class GroupByBuilder {

	private Table table;

	private IRowMapper mapper;

	private String keyColumn;

	public GroupByBuilder(Table table, IRowMapper mapper, String keyColumn) {
		this.table = table;
		this.mapper = mapper;
		this.keyColumn = keyColumn;
	}

	public Table count() {
		return table.groupBy(mapper, new CountRowAggregator(keyColumn));
	}

	public Table sum(String column) {
		return table.groupBy(mapper, new SumRowAggregator(keyColumn, column));
	}

	public Table mean(String column) {
		return table.groupBy(mapper, new MeanRowAggregator(keyColumn, column));
	}

	public Table min(String column) {
		return table.groupBy(mapper, new MinRowAggregator(keyColumn, column));
	}

	public Table max(String column) {
		return table.groupBy(mapper, new MaxRowAggregator(keyColumn, column));
	}

	public Table aggregate(IRowAggregator aggregator) {
		return table.groupBy(mapper, aggregator);
	}

}
