package lukfor.tables.io;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import genepi.io.table.writer.CsvTableWriter;
import genepi.io.table.writer.ExcelTableWriter;
import genepi.io.table.writer.ITableWriter;
import genepi.io.text.LineWriter;
import lukfor.tables.Table;
import lukfor.tables.columns.AbstractColumn;
import lukfor.tables.columns.types.DoubleColumn;
import lukfor.tables.columns.types.IntegerColumn;
import lukfor.tables.rows.IRowProcessor;
import lukfor.tables.rows.Row;

public class TableWriter {

	public static void writeToCsv(Table table, File file) throws IOException {
		writeToCsv(table, file.getAbsolutePath(), ',');
	}

	public static void writeToCsv(Table table, String filename) throws IOException {
		writeToCsv(table, filename, ',', false);
	}

	public static void writeToCsv(Table table, File file, char outputSeparator) throws IOException {
		writeToCsv(table, file.getAbsolutePath(), outputSeparator, false);
	}

	public static void writeToCsv(Table table, String filename, char outputSeparator) throws IOException {
		writeToCsv(table, filename, outputSeparator, false);
	}

	public static void writeToCsv(Table table, String filename, char outputSeparator, boolean quote)
			throws IOException {

		Table.log(table, "Writing file " + filename + "...");
		CsvTableWriter writer = new CsvTableWriter(filename, outputSeparator, quote);
		writeToTableWriter(table, writer);

	}

	public static void writeToXls(Table table, String filename) throws IOException {

		Table.log(table, "Writing file " + filename + "...");
		ExcelTableWriter writer = new ExcelTableWriter(filename);
		writeToTableWriter(table, writer);

	}

	public static void writeToTableWriter(final Table table, final ITableWriter writer) throws IOException {

		long start = System.currentTimeMillis();

		writer.setColumns(table.getColumns().getNames());

		table.forEachRow(new IRowProcessor() {

			public void process(Row row) {
				for (int i = 0; i < table.getColumns().getSize(); i++) {
					AbstractColumn column = table.getColumns().get(i);
					try {
						Object object = row.getObject(column.getName());
						if (object != null) {
							if (object instanceof Integer) {
								writer.setInteger(column.getName(), (Integer) object);
							} else if (object instanceof Double) {
								writer.setDouble(column.getName(), (Double) object);
							} else {
								String value = column.objectToValue(object);
								writer.setString(column.getName(), value);
							}
						} else {
							writer.setString(column.getName(), "");
						}

					} catch (Exception e) {
						e.printStackTrace();
						writer.setString(column.getName(), "");
					}
				}
				writer.next();
			}
		});
		writer.close();

		long end = System.currentTimeMillis();

		Table.log(table, "Wrote " + table.getRows().getSize() + " rows and " + table.getColumns().getSize()
				+ " columns. Time: " + (end - start) + " ms");

	}

	public static void writeToSasCsv(final Table table, String filename) throws IOException {

		long start = System.currentTimeMillis();

		String separator = ",";
		String quote = "\"";
		String missingNumber = ".";

		LineWriter writer = new LineWriter(filename);
		String[] columns = table.getColumns().getNames();
		StringBuffer line = new StringBuffer();
		for (int i = 0; i < columns.length; i++) {
			if (i > 0) {
				line.append(separator);
			}
			line.append(columns[i]);
		}
		writer.write(line.toString());

		table.forEachRow(new IRowProcessor() {

			public void process(Row row) {
				StringBuffer line = new StringBuffer();
				for (int i = 0; i < table.getColumns().getSize(); i++) {
					if (i > 0) {
						line.append(separator);
					}
					try {
						AbstractColumn column = table.getColumns().get(i);
						Object object = row.getObject(column.getName());
						if (object != null) {
							if (object instanceof Integer) {
								line.append(object.toString());
							} else if (object instanceof Double) {
								line.append(object.toString());
							} else if (object instanceof Date) {
								line.append(column.objectToValue(object));
							} else {
								String value = column.objectToValue(object);
								line.append(quote);
								line.append(value);
								line.append(quote);
							}
						} else {
							if (column instanceof IntegerColumn) {
								line.append(missingNumber);
							} else if (column instanceof DoubleColumn) {
								line.append(missingNumber);
							} else {
								line.append(quote);
								line.append(quote);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						line.append(quote);
						line.append(quote);
					}
				}
				try {
					writer.write(line.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		writer.close();

		long end = System.currentTimeMillis();

		Table.log(table, "Wrote " + table.getRows().getSize() + " rows and " + table.getColumns().getSize()
				+ " columns. Time: " + (end - start) + " ms");

	}

}
