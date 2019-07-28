package lukfor.tables.columns;

import lukfor.tables.columns.types.DoubleColumn;
import lukfor.tables.columns.types.IntegerColumn;
import lukfor.tables.columns.types.StringColumn;

public class ColumnFactory {

	public static AbstractColumn createColumn(String name, ColumnType type) {

		switch (type) {
		case INTEGER:
			return new IntegerColumn(name);
		case DOUBLE:
			return new DoubleColumn(name);
		default:
			return new StringColumn(name);
		}

	}

}
