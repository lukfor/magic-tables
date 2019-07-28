package lukfor.tables.io;

import java.io.IOException;

import genepi.io.FileUtil;
import genepi.io.table.reader.CsvTableReader;
import genepi.io.table.reader.ExcelTableReader;
import genepi.io.table.reader.ITableReader;
import lukfor.tables.Table;
import lukfor.tables.columns.AbstractColumn;
import lukfor.tables.columns.ColumnType;
import lukfor.tables.columns.ColumnTypeDetector;
import lukfor.tables.columns.types.StringColumn;

public class TableBuilder {

	public static Table fromCsvFile(String filename, char separator) throws IOException {
		System.out.println("Reading Csv file " + filename + "...");
		ITableReader reader = new CsvTableReader(filename, separator);

		String name = FileUtil.getFilename(filename);
		return fromTableReader(name, reader);
	}

	public static Table fromXlsFile(String filename) throws IOException {

		System.out.println("Reading Excel file " + filename + "...");
		ITableReader reader = new ExcelTableReader(filename);

		String name = FileUtil.getFilename(filename);
		return fromTableReader(name, reader);
	}

	public static Table fromFile(String filename, char separator) throws IOException {

		ITableReader reader = null;
		if (filename.endsWith(".xls")) {
			System.out.println("Reading Excel file " + filename + "...");
			reader = new ExcelTableReader(filename);
		} else {
			System.out.println("Reading Csv file " + filename + "...");
			reader = new CsvTableReader(filename, separator);
		}

		String name = FileUtil.getFilename(filename);
		return fromTableReader(name, reader);

	}

	public static Table fromTableReader(String name, ITableReader reader) throws IOException {

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

		//try to find the right type		
		for (int i = 0;i< table.getColumns().getSize(); i++) {
			AbstractColumn column = table.getColumns().get(i);
			ColumnType type = ColumnTypeDetector.guessType(column);
			if (type != column.getType()) {
				System.out.println("Update type of " + column.getName() + " to " + type + "...");
				table.getColumns().setType(column, type);
			}
		}
		
		System.out.println("Loaded table " + table.getName() + " [" + table.getRows().getSize() + " x "
				+ table.getColumns().getSize() + "] into memory.");

		return table;
	}

}
