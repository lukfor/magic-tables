package lukfor.tables;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import lukfor.tables.columns.AbstractColumn;
import lukfor.tables.columns.ColumnSorter;
import lukfor.tables.rows.IRowProcessor;
import lukfor.tables.rows.Row;
import lukfor.tables.rows.filters.IRowFilter;
import lukfor.tables.rows.filters.RowValueRegExFilter;
import lukfor.tables.rows.processors.RowDuplicateProcessor;
import lukfor.tables.rows.processors.RowSelectionProcessor;

public class RowOperations {

	private Table table;

	public RowOperations(Table table) {
		this.table = table;
	}

	public Row get(int index) {
		Row row = new Row(table, index);
		return row;
	}

	public List<Row> getAll(final String column, final Object value) {

		return getAll(new IRowFilter() {
			public boolean accepts(Row row) {
				Object valueRow = row.getObject(column);
				return valueRow.equals(value);
			}
		});

	}

	public List<Row> getAllByRegEx(final String column, final String value) {

		return getAll(new IRowFilter() {
			public boolean accepts(Row row) {
				String valueRow = row.getObject(column).toString();
				return (valueRow.matches(value));
			}
		});

	}

	public List<Row> getAll(final IRowFilter filter) {
		final List<Row> results = new Vector<Row>();
		table.forEachRow(new IRowProcessor() {
			public void process(Row row) {
				if (filter.accepts(row)) {
					results.add(row);
				}
			}
		});
		return results;
	}

	public Row append() {
		for (AbstractColumn column : table.storage) {
			column.add(null);
		}
		int index = table.getRows().getSize() - 1;
		return new Row(table, index);
	}

	public void sortBy(final String column) {
		sortBy(column, ColumnSorter.SORT_ASCEND);
	}

	public void sortAscBy(final String column) {
		sortBy(column, ColumnSorter.SORT_ASCEND);
	}

	public void sortDescBy(final String column) {
		sortBy(column, ColumnSorter.SORT_DESCEND);
	}

	public void sortBy(final String column, final int order) {
		table.assertsColumnExists(column);
		ColumnSorter processor = new ColumnSorter(table.getColumns().get(column), order);

		for (AbstractColumn columnr : table.storage) {
			columnr.sort(processor.getIndices());
		}

	}

	public void dropByRegEx(String column, String regExp) throws RuntimeException {
		drop(new RowValueRegExFilter(column, regExp));
	}

	public void drop(final IRowFilter filter) throws RuntimeException {

		RowSelectionProcessor processor = new RowSelectionProcessor(filter);
		table.forEachRow(processor);
		drop(processor.getBitmask());

	}

	public void drop(List<Boolean> bitmask) throws RuntimeException {

		Table.log(table, "Droping rows...");

		int rowsBefore = getSize();

		for (AbstractColumn column : table.storage) {
			column.drop(bitmask);
		}

		int rowsAfter = getSize();

		Table.log(table, "#Rows before: " + rowsBefore);
		Table.log(table, "#Rows after: " + rowsAfter);
		Table.log(table, "Droped rows.");

	}

	public void dropDuplicates() {
		RowDuplicateProcessor processor = new RowDuplicateProcessor();
		table.forEachRow(processor);
		drop(processor.getBitmask());
	}

	public void dropMissings() {
		IRowFilter filter = new IRowFilter() {
			@Override
			public boolean accepts(Row row) {
				return row.hasMissings();
			}
		};
		RowSelectionProcessor processor = new RowSelectionProcessor(filter);
		table.forEachRow(processor);
		drop(processor.getBitmask());
	}

	public void dropMissings(String column) {
		IRowFilter filter = new IRowFilter() {
			@Override
			public boolean accepts(Row row) {
				return (row.getObject(column) == null);
			}
		};
		RowSelectionProcessor processor = new RowSelectionProcessor(filter);
		table.forEachRow(processor);
		drop(processor.getBitmask());
	}

	public void selectByRegEx(String column, String regExp) {
		select(new RowValueRegExFilter(column, regExp));
	}

	public void select(final IRowFilter filter) {

		RowSelectionProcessor processor = new RowSelectionProcessor(filter);
		table.forEachRow(processor);
		select(processor.getBitmask());

	}

	public void select(List<Boolean> bitmask) {

		Table.log(table, "Filtering rows...");

		int rowsBefore = getSize();

		for (AbstractColumn column : table.storage) {
			column.select(bitmask);
		}

		int rowsAfter = getSize();

		Table.log(table, "#Rows before: " + rowsBefore);
		Table.log(table, "#Rows after: " + rowsAfter);
		Table.log(table, "Filtered table.");

	}

	public int getSize() {
		table.assertsNotEmpty();
		return table.storage.get(0).getSize();
	}

	public String[][] data() {
		return data(0, getSize() - 1);
	}

	public String[][] data(int start, int end) throws RuntimeException {

		int height = (end - start) + 1;

		int width = table.getColumns().getSize() + 1;
		String[][] data = new String[height][];
		for (int i = 0; i < height; i++) {
			data[i] = new String[width];
			data[i][0] = (i + start + 1) + "";
			for (int j = 0; j < width - 1; j++) {
				Object value = table.getColumn(j).get(i + start);
				String string = table.getColumn(j).objectToValue(value);
				data[i][j + 1] = string;
			}
		}
		return data;
	}

	public void clear() {
		// currently nothing to clear.
	}

}
