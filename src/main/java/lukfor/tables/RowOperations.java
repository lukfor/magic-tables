package lukfor.tables;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import lukfor.tables.columns.AbstractColumn;
import lukfor.tables.columns.ColumnSorter;
import lukfor.tables.rows.IRowProcessor;
import lukfor.tables.rows.Row;
import lukfor.tables.rows.RowSelectionProcessor;
import lukfor.tables.rows.filters.IRowFilter;
import lukfor.tables.rows.filters.RowValueRegExFilter;

public class RowOperations {

	private Table table;

	public RowOperations(Table table) {
		this.table = table;
	}

	public Row get(int index) {
		Row row = new Row();
		for (AbstractColumn column : table.storage) {
			Object value = column.get(index);
			row.set(column.getName(), value);
		}
		return row;
	}


	public List<Row> getAll(final String column, final Object value) throws IOException {

		return getAll(new IRowFilter() {
			public boolean accepts(Row row) throws IOException {
				Object valueRow = row.getObject(column);
				return valueRow.equals(value);
			}
		});

	}
	
	public List<Row> getAllByRegEx(final String column, final String value) throws IOException {

		return getAll(new IRowFilter() {
			public boolean accepts(Row row) throws IOException {
				String valueRow = row.getObject(column).toString();
				return (valueRow.matches(value));
			}
		});

	}


	public List<Row> getAll(final IRowFilter filter) throws IOException {
		final List<Row> results = new Vector<Row>();
		table.forEachRow(new IRowProcessor() {
			public void process(Row row) throws IOException {
				if (filter.accepts(row)) {
					results.add(row);
				}
			}
		});
		return results;
	}
	
	
	public void append(Row row) throws IOException {
		for (AbstractColumn column : table.storage) {
			Object object = row.getObject(column.getName());
			if (object != null) {
				if (column.accepts(object)) {
					column.add(object);
				} else {
					throw new IOException(
							"Object in column " + column.getName() + " has wrong class: " + object.getClass());

				}
			} else {
				column.add(null);
			}
		}
	}

	public void sortBy(final String column) throws IOException {
		sortBy(column, ColumnSorter.SORT_ASCEND);
	}

	public void sortAscBy(final String column) throws IOException {
		sortBy(column, ColumnSorter.SORT_ASCEND);
	}

	public void sortDescBy(final String column) throws IOException {
		sortBy(column, ColumnSorter.SORT_DESCEND);
	}

	public void sortBy(final String column, final int order) throws IOException {
		table.assertsColumnExists(column);
		ColumnSorter processor = new ColumnSorter(table.getColumns().get(column), order);

		for (AbstractColumn columnr : table.storage) {
			columnr.sort(processor.getIndices());
		}

	}

	public void dropByRegEx(String column, String regExp) throws IOException {
		drop(new RowValueRegExFilter(column, regExp));
	}

	public void drop(final IRowFilter filter) throws IOException {

		RowSelectionProcessor processor = new RowSelectionProcessor(filter);
		table.forEachRow(processor);
		drop(processor.getBitmask());

	}

	public void drop(List<Boolean> bitmask) throws IOException {

		int rowsBefore = getSize();

		for (AbstractColumn column : table.storage) {
			column.drop(bitmask);
		}

		int rowsAfter = getSize();

		System.out.println("Droped table.");
		System.out.println("  #Rows before droping: " + rowsBefore);
		System.out.println("  #Rows after droping: " + rowsAfter);

	}

	public void dropMissings() throws IOException {
		IRowFilter filter = new IRowFilter() {			
			@Override
			public boolean accepts(Row row) throws IOException {
				return row.hasMissings();
			}
		};
		RowSelectionProcessor processor = new RowSelectionProcessor(filter);
		table.forEachRow(processor);
		drop(processor.getBitmask());
	}
	
	public void dropMissings(String column) throws IOException {
		IRowFilter filter = new IRowFilter() {			
			@Override
			public boolean accepts(Row row) throws IOException {
				return (row.getObject(column) == null);
			}
		};
		RowSelectionProcessor processor = new RowSelectionProcessor(filter);
		table.forEachRow(processor);
		drop(processor.getBitmask());
	}
	
	public void selectByRegEx(String column, String regExp) throws IOException {
		select(new RowValueRegExFilter(column, regExp));
	}

	public void select(final IRowFilter filter) throws IOException {

		RowSelectionProcessor processor = new RowSelectionProcessor(filter);
		table.forEachRow(processor);
		select(processor.getBitmask());

	}

	public void select(List<Boolean> bitmask) throws IOException {

		int rowsBefore = getSize();

		for (AbstractColumn column : table.storage) {
			column.select(bitmask);
		}

		int rowsAfter = getSize();

		System.out.println("Filterd table.");
		System.out.println("  #Rows before filtering: " + rowsBefore);
		System.out.println("  #Rows after filtering: " + rowsAfter);

	}
	
	public int getSize() throws IOException {
		table.assertsNotEmpty();
		return table.storage.get(0).getSize();
	}

}
