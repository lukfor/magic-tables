package lukfor.tables.io;

import java.io.File;
import java.io.IOException;

import genepi.io.table.writer.CsvTableWriter;
import genepi.io.table.writer.ExcelTableWriter;
import genepi.io.table.writer.ITableWriter;
import lukfor.tables.Table;
import lukfor.tables.columns.AbstractColumn;
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
	
	public static void writeToCsv(Table table, String filename, char outputSeparator, boolean quote) throws IOException {

		System.out.println("Writing file " + filename + "...");
		CsvTableWriter writer = new CsvTableWriter(filename, outputSeparator, quote);
		writeToTableWriter(table, writer);

	}

	public static void writeToXls(Table table, String filename) throws IOException {

		System.out.println("Writing file " + filename + "...");
		ExcelTableWriter writer = new ExcelTableWriter(filename);
		writeToTableWriter(table, writer);

	}

	public static void writeToTableWriter(final Table table, final ITableWriter writer) throws IOException {

		writer.setColumns(table.getColumns().getNames());

		table.forEachRow(new IRowProcessor() {

			public void process(Row row) {
				for (int i = 0; i < table.getColumns().getSize(); i++) {
					AbstractColumn column = table.getColumns().get(i);
					try {
						Object object = row.getObject(column.getName());
						String value = column.objectToValue(object);
						writer.setString(column.getName(), value);
					} catch (Exception e) {
						e.printStackTrace();
						writer.setString(column.getName(), "");
					}
				}
				writer.next();
			}
		});
		writer.close();

		System.out.println(
				"Wrote " + table.getRows().getSize() + " rows and " + table.getColumns().getSize() + " columns.");

	}

}
