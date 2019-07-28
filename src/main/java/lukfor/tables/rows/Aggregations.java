package lukfor.tables.rows;

import java.util.List;
import java.util.Vector;

import lukfor.tables.columns.AbstractColumn;
import lukfor.tables.columns.types.IntegerColumn;
import lukfor.tables.columns.types.StringColumn;

public class Aggregations {

	
	public static IRowReducer COUNT = new IRowReducer() {

		@Override
		public Row reduce(Object key, List<Row> rows) {
			Row row = new Row();
			row.set("key", key.toString());
			row.set("count", rows.size());
			return row;
		}

		@Override
		public List<AbstractColumn> getColumns() {
			List<AbstractColumn> columns = new Vector<AbstractColumn>();
			columns.add(new StringColumn("key"));
			columns.add(new IntegerColumn("count"));
			return columns;
		}
	};
	
	//TODO: SUM, MIN, MAX. Datatype support?
	
}
