package lukfor.tables.io;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import genepi.io.FileUtil;
import genepi.io.table.reader.CsvTableReader;
import genepi.io.table.reader.ExcelTableReader;
import genepi.io.table.reader.ITableReader;
import lukfor.tables.Table;
import lukfor.tables.columns.AbstractColumn;
import lukfor.tables.columns.types.StringColumn;
import lukfor.tables.io.options.CsvTableOptions;
import lukfor.tables.io.options.ExcelTableOptions;

public class TableBuilder {

	public static CsvTableOptions fromCsvFile(String filename) {
		return new CsvTableOptions(filename);
	}

	public static Table fromCsvFile(CsvTableOptions options) throws IOException {
		Table.log("Reading Csv file " + options.getFilename() + "...");

		FileInputStream inputStream = new FileInputStream(options.getFilename());
		InputStream in2 = FileUtil.decompressStream(inputStream);

		ITableReader reader = new CsvTableReader(new DataInputStream(in2), options.getSeparator());

		String name = FileUtil.getFilename(options.getFilename());
		return fromTableReader(name, reader, options.isColumnTypeDetection());
	}

	public static ExcelTableOptions fromXlsFile(String filename) throws IOException {
		return new ExcelTableOptions(filename);
	}

	public static Table fromXlsFile(ExcelTableOptions options) throws IOException {

		Table.log("Reading Excel file " + options.getFilename() + "...");
		ITableReader reader = new ExcelTableReader(options.getFilename());

		String name = FileUtil.getFilename(options.getFilename());
		return fromTableReader(name, reader, options.isColumnTypeDetection());
	}

	public static Table fromFile(String filename, char separator) throws IOException {

		ITableReader reader = null;
		if (filename.endsWith(".xls")) {
			Table.log("Reading Excel file " + filename + "...");
			reader = new ExcelTableReader(filename);
		} else {
			Table.log("Reading Csv file " + filename + "...");
			reader = new CsvTableReader(filename, separator);
		}

		String name = FileUtil.getFilename(filename);
		return fromTableReader(name, reader, true);

	}

	public static Table fromTableReader(String name, ITableReader reader, boolean columnTypeDetection)
			throws IOException {

		long start = System.currentTimeMillis();
		
		Table table = new Table(name);

		for (String column : reader.getColumns()) {
			table.getColumns().append(new StringColumn(column));
		}

		while (reader.next()) {
			for (int i = 0; i < table.getColumns().getSize(); i++) {
				AbstractColumn column = table.getColumns().get(i);
				String value = reader.getString(column.getName());
				Object object = column.valueToObject(value);
				column.add(object);
			}
		}
		reader.close();

		// try to find the right type
		if (columnTypeDetection) {
			table.detectTypes();
		}

		long end = System.currentTimeMillis();
		
		Table.log("Loaded table " + table.getName() + " [" + table.getRows().getSize() + " x "
				+ table.getColumns().getSize() + "] into memory. Time: " + (end - start) + " ms");

		return table;
	}

	public static Table fromDatabase(Connection connection, String sql) throws SQLException, IOException {

		long start = System.currentTimeMillis();
		
		Table table = new Table(sql);

		Statement statement = connection.createStatement();
		ResultSet result = statement.executeQuery(sql);
		ResultSetMetaData meta = result.getMetaData();

		for (int i = 0; i < meta.getColumnCount(); i++) {
			String column = meta.getColumnName(i + 1);
			table.getColumns().append(new StringColumn(column));
		}

		while (result.next()) {
			for (int i = 0; i < meta.getColumnCount(); i++) {
				AbstractColumn column = table.getColumns().get(i);
				String value = result.getString(i + 1);
				Object object = column.valueToObject(value);
				column.add(object);
			}

		}

		long end = System.currentTimeMillis();
		
		Table.log("Loaded table " + table.getName() + " [" + table.getRows().getSize() + " x "
				+ table.getColumns().getSize() + "] into memory. Time: " + (end - start) + " ms");

		return table;
	}

}
