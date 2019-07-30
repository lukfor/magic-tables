package lukfor.tables;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.jakewharton.fliptables.FlipTable;

import lukfor.tables.columns.AbstractColumn;
import lukfor.tables.columns.types.StringColumn;
import lukfor.tables.rows.IRowAggregator;
import lukfor.tables.rows.IRowMapper;
import lukfor.tables.rows.IRowProcessor;
import lukfor.tables.rows.Row;
import lukfor.tables.rows.processors.RowCopyProcessor;
import lukfor.tables.rows.processors.RowGroupProcessor;
import lukfor.tables.utils.GroupByBuilder;

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

	public AbstractColumn getColumn(String name) {
		return columns.get(name);
	}

	public AbstractColumn getColumn(int index) {
		return columns.get(index);
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

	public GroupByBuilder groupBy(final String column) throws IOException {

		IRowMapper mapper = new IRowMapper() {
			@Override
			public Object getKey(Row row) throws IOException {
				return row.getObject(column);
			}
		};

		return new GroupByBuilder(this, mapper, column);
	}

	public Table groupBy(final String column, IRowAggregator aggregator) throws IOException {

		return groupBy(new IRowMapper() {
			@Override
			public Object getKey(Row row) throws IOException {
				return row.getObject(column);
			}
		}, aggregator);

	}

	public Table groupBy(IRowMapper mapper, IRowAggregator aggregator) throws IOException {
		RowGroupProcessor processor = new RowGroupProcessor(mapper);
		forEachRow(processor);
		Table result = null;
		Map<Object, List<Integer>> groups = processor.getGroups();
		for (Object key : groups.keySet()) {
			List<Integer> indices = groups.get(key);
			Table groupedTable = this.cloneStructure(name + ":" + key);
			for (Integer index : indices) {
				groupedTable.getRows().append(getRows().get(index));
			}
			Table reducedTable = aggregator.aggregate(groupedTable);
			if (result == null) {
				result = reducedTable;
			} else {
				result.append(reducedTable);
			}
		}
		return result;
	}

	public void append(Table table) throws IOException {
		table.forEachRow(new RowCopyProcessor(this));
	}

	public int getMissings() {
		int missings = 0;
		for (AbstractColumn column : storage) {
			missings += column.getMissings();
		}
		return missings;
	}

	public void fillMissings(Object value) {
		for (AbstractColumn column : storage) {
			column.fillMissings(value);
		}
	}

	public int getUniqueValues() {
		int uniques = 0;
		for (AbstractColumn column : storage) {
			uniques += column.getUniqueValues();
		}
		return uniques;
	}

	public void replaceValue(Object oldValue, Object newValue) throws IOException {
		replaceValue(new Object[] { oldValue }, new Object[] { newValue });
	}

	public void replaceValue(Object[] oldValues, Object[] newValues) throws IOException {
		for (AbstractColumn column : storage) {
			column.replaceValue(oldValues, newValues);
		}
	}

	public void merge(final Table table2, final String column) throws IOException {
		merge(table2, column, column);
	}

	public void merge(final Table table2, final String columnTable1, final String columnTable2) throws IOException {

		final List<String> columnsTable2 = new Vector<String>();

		// add all columns from table expect join column
		for (int i = 0; i < table2.getColumns().getSize(); i++) {
			AbstractColumn columTable2 = table2.getColumns().get(i);
			if (!columTable2.getName().equals(columnTable2)) {
				columns.append(new StringColumn(columTable2.getName()));
				columns.setType(columTable2.getName(), columTable2.getType());
				columnsTable2.add(columTable2.getName());
			}
		}

		forEachRow(new IRowProcessor() {

			public void process(Row row) throws IOException {

				Object value = row.getObject(columnTable1);
				List<Row> rowsTable2 = table2.getRows().getAll(columnTable2, value);
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

	public Table cloneStructure(String name) throws IOException {
		Table table = new Table(getName() + ":" + name);
		for (AbstractColumn column : storage) {
			table.getColumns().append(column.cloneStructure());
		}
		return table;
	}

	public Table clone() {

		Table table = null;
		try {
			table = cloneStructure("cloned");
			for (AbstractColumn column : storage) {
				table.getColumn(column.getName()).copyDataFrom(column);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return table;
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

	public void printFirst(int n) throws IOException {
		n = Math.min(n, getRows().getSize());
		printBetween(0, n-1);
	}

	public void printLast(int n) throws IOException {
		int height = getRows().getSize() - 1;
		int start = Math.max(height - n, 0);
		printBetween(start, height);
	}

	public void printBetween(int start, int end) throws IOException {

		int height = getRows().getSize() - 1;
		start = Math.max(start, 0);
		end = Math.min(end, height);

		
		String[] columns = getColumns().getNames();
		String[] header = new String[columns.length + 1];
		header[0] = "";
		for (int i = 0; i < columns.length; i++) {
			header[i + 1] = columns[i];
		}

		String[][] data = getRows().data(start, end);
		System.out.print(FlipTable.of(header, data));
		System.out.println("Showing " + (start + 1) + " to " + (end + 1) + " of " + getRows().getSize() + " entries, "
				+ getColumns().getSize() + " total columns");
	}

	public void print() throws IOException {
		printFirst(25);
	}

	public void printAll() throws IOException {
		printFirst(getRows().getSize());
	}

	public void printSummary() throws IOException {

		System.out.println(name + " [" + getRows().getSize() + " x " + getColumns().getSize() + "]");

		Table table = new Table(name + ":summary");
		table.getColumns().append(new StringColumn("column"));
		table.getColumns().append(new StringColumn("type"));
		table.getColumns().append(new StringColumn("min"));
		table.getColumns().append(new StringColumn("mean"));
		table.getColumns().append(new StringColumn("max"));
		table.getColumns().append(new StringColumn("missings"));
		table.getColumns().append(new StringColumn("n"));
		for (AbstractColumn column : storage) {
			Row row = new Row();
			row.setString("column", column.getName());
			row.setString("type", column.getType());
			row.setString("min", column.getMin());
			row.setString("mean", column.getMax());
			row.setString("max", column.getMax());
			row.setString("missings", column.getMissings());
			row.setString("n", (column.getSize() - column.getMissings()));
			table.getRows().append(row);
		}
		table.printAll();
	}

}
