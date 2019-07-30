package lukfor.tables;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import lukfor.tables.columns.AbstractColumn;
import lukfor.tables.columns.ColumnFactory;
import lukfor.tables.columns.ColumnType;
import lukfor.tables.columns.IBuildValueFunction;
import lukfor.tables.columns.filters.ColumnNameFilter;
import lukfor.tables.columns.filters.ColumnNameRegExFilter;
import lukfor.tables.columns.filters.IColumnFilter;
import lukfor.tables.rows.IRowProcessor;
import lukfor.tables.rows.Row;

public class ColumnOperations {

	private Table table;

	private List<AbstractColumn> columns = new Vector<AbstractColumn>();

	public ColumnOperations(Table table) {
		this.table = table;
		this.columns = table.storage;
	}

	public AbstractColumn get(String name) {
		for (AbstractColumn column : columns) {
			if (column.getName().equals(name)) {
				return column;
			}
		}
		return null;
	}

	public AbstractColumn get(int index) {
		return columns.get(index);
	}

	public void rename(String oldName, String newName) throws IOException {
		table.assertsColumnExists(oldName);
		AbstractColumn column = get(oldName);
		column.setName(newName);
	}

	public void append(AbstractColumn column) throws IOException {
		append(column, new IBuildValueFunction() {
			
			@Override
			public Object buildValue(Row row) throws IOException {
				return null;
			}
		});
	}

	public void append(final AbstractColumn column, final IBuildValueFunction builder) throws IOException {

		if (get(column.getName()) != null) {
			throw new IOException("Duplicate column '" + column + ".");
		}

		System.out.println("Adding column '" + column + "'...");
		columns.add(column);
		if (builder != null) {

			table.forEachRow(new IRowProcessor() {

				public void process(Row row) throws IOException {
					Object value = builder.buildValue(row);
					if (value == null || column.accepts(value)) {
						column.add(value);
					} else {
						throw new IOException("Wrong class: " + value.getClass());
					}
				}

			});

		}
	}

	public void setType(String column, ColumnType type) throws IOException {

		table.assertsColumnExists(column);
		AbstractColumn oldColumn = get(column);
		setType(oldColumn, type);
	}

	public void setType(AbstractColumn column, ColumnType type) throws IOException {

		AbstractColumn newColumn = ColumnFactory.createColumn(column.getName(), type);
		newColumn.copyDataFrom(column);

		int index = columns.indexOf(column);
		columns.set(index, newColumn);

	}

	public String[] getNames() {
		String[] names = new String[columns.size()];
		for (int i = 0; i < columns.size(); i++) {
			names[i] = columns.get(i).getName();
		}
		return names;
	}

	public String[] getTypes() {
		String[] types = new String[columns.size()];
		for (int i = 0; i < columns.size(); i++) {
			types[i] = columns.get(i).getType().toString();
		}
		return types;
	}

	public void drop(String... names) throws IOException {
		drop(new ColumnNameFilter(names));
	}

	public void dropByRegEx(String regex) throws IOException {

		ColumnNameRegExFilter filter = new ColumnNameRegExFilter(regex);
		drop(filter);

	}

	public void drop(IColumnFilter filter) throws IOException {
		List<AbstractColumn> removedColumns = new Vector<AbstractColumn>();
		for (AbstractColumn column : columns) {
			if (filter.accepts(column)) {
				removedColumns.add(column);
			}
		}
		columns.removeAll(removedColumns);
	}

	public void select(String... names) throws IOException {
		select(new ColumnNameFilter(names));
	}

	public void selectByRegEx(String regex) throws IOException {
		select(new ColumnNameRegExFilter(regex));
	}

	public void select(IColumnFilter filter) throws IOException {
		List<AbstractColumn> removedColumns = new Vector<AbstractColumn>();
		for (AbstractColumn column : columns) {
			if (!filter.accepts(column)) {
				removedColumns.add(column);
			}
		}
		columns.removeAll(removedColumns);
	}

	public int getSize() {
		return columns.size();
	}

}
