package lukfor.tables.columns;

import lukfor.tables.columns.types.DoubleColumn;
import lukfor.tables.columns.types.IntegerColumn;

public class ColumnTypeDetector {

	public static ColumnType guessType(AbstractColumn column) {

		boolean doubles = true;

		boolean integers = true;

		DoubleColumn doubleColumn = new DoubleColumn("");

		IntegerColumn integerColumn = new IntegerColumn("");

		for (int i = 0; i < column.getSize(); i++) {
			Object value = column.get(i);
			if (value != null) {
				if (doubles) {
					try {
						doubleColumn.valueToObject(value.toString());
					} catch (Exception e) {
						doubles = false;
					}
				}
				if (integers) {
					try {
						integerColumn.valueToObject(value.toString());
					} catch (Exception e) {
						integers = false;
					}
				}
			}
		}

		if (integers) {
			return ColumnType.INTEGER;
		}

		if (doubles) {
			return ColumnType.DOUBLE;
		}

		return ColumnType.STRING;
	}

}
