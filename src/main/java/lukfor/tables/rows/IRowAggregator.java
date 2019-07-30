package lukfor.tables.rows;

import java.io.IOException;

import lukfor.tables.Table;

public interface IRowAggregator {

	public Table aggregate(Table group) throws IOException;

}
