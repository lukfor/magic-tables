package lukfor.tables;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import lukfor.tables.columns.AbstractColumn;
import lukfor.tables.columns.types.StringColumn;
import lukfor.tables.rows.IRowProcessor;
import lukfor.tables.rows.Row;

public class Table {

	private String name;

	protected List<AbstractColumn> storage = new Vector<AbstractColumn>();

	private RowOperations rows;

	private ColumnOperations columns;

	public Table(String name) {
		this.name = name;
		rows = new RowOperations(this);
		columns = new ColumnOperations(this);
	}

	public Object get(int index, String column) throws IOException {
		assertsColumnExists(column);
		return columns.get(column).get(index);
	}

	public Object get(int index, int column) throws IOException {
		assertsColumnExists(column);
		return columns.get(column).get(index);
	}

	public void forEachRow(final IRowProcessor processor) throws IOException {
		assertsNotEmpty();
		for (int i = 0; i < getRows().getSize(); i++) {
			Row row = rows.get(i);
			processor.process(row);
		}
	}

	public ColumnOperations getColumns() {
		return columns;
	}

	public RowOperations getRows() {
		return rows;
	}

	public void merge(final Table table2, final String col) throws IOException {
		merge(table2, col, col);
	}

	public void merge(final Table table2, final String colTable1, final String colTable2) throws IOException {

		final List<String> columnsTable2 = new Vector<String>();

		// add all columns from table expect join column
		for (int i = 0; i < table2.getColumns().getSize(); i++) {
			AbstractColumn columTable2 = table2.getColumns().get(i);
			if (!columTable2.getName().equals(colTable2)) {
				columns.append(new StringColumn(columTable2.getName()));
				columns.setType(columTable2.getName(), columTable2.getType());
				columnsTable2.add(columTable2.getName());
			}
		}

		forEachRow(new IRowProcessor() {

			public void process(Row row) throws IOException {

				Object value = row.getObject(colTable1);
				List<Row> rowsTable2 = table2.getRows().getAll(colTable2, value);
				if (rowsTable2.size() != 1) {
					throw new IOException("simple merge not possible. No one to one mapping found.");
				}
				for (Row rowTable2 : rowsTable2) {
					for (String columTable2 : columnsTable2) {
						Object valueTable2 = rowTable2.getObject(columTable2);
						row.set(columTable2, valueTable2);
					}
				}
			}
		});

	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {

		return super.toString();
	}

	protected void assertsColumnExists(String column) throws IOException {
		if (columns.get(column) == null) {
			throw new IOException("Column '" + column + "' not found.");
		}
	}

	protected void assertsColumnExists(int column) throws IOException {
		if (column >= getColumns().getSize()) {
			throw new IOException("Column '" + column + "' not found.");
		}
	}

	protected void assertsNotEmpty() throws IOException {
		if (storage.size() == 0) {
			throw new IOException("Table is empty.");
		}
	}

	public void view() {

	}

	public void printSummary() {
		for (AbstractColumn column : storage) {
			column.printSummary();
			System.out.println();
		}
	}

}
