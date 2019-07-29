package lukfor.tables.io.options;

import java.io.IOException;

import lukfor.tables.Table;
import lukfor.tables.io.TableBuilder;

public class CsvTableOptions {

	private String filename;

	private char separator = ',';

	private String encoding = "";

	private String missingValue = "";

	private String dateFormat = "";

	private boolean header = true;

	private boolean columnTypeDetection = true;

	public CsvTableOptions(String filename) {
		this.filename = filename;
	}
	
	public String getFilename() {
		return filename;
	}

	public char getSeparator() {
		return separator;
	}

	public CsvTableOptions withSeparator(char separator) {
		this.separator = separator;
		return this;
	}

	public String getEncoding() {
		return encoding;
	}

	public CsvTableOptions withEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	public String getMissingValue() {
		return missingValue;
	}

	public CsvTableOptions withMissingValue(String missingValue) {
		this.missingValue = missingValue;
		return this;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public CsvTableOptions withDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
		return this;
	}

	public boolean isHeader() {
		return header;
	}

	public CsvTableOptions withHeader(boolean header) {
		this.header = header;
		return this;
	}

	public boolean isColumnTypeDetection() {
		return columnTypeDetection;
	}

	public CsvTableOptions withColumnTypeDetection(boolean columnTypeDetection) {
		this.columnTypeDetection = columnTypeDetection;
		return this;
	}

	public Table load() throws IOException {
		return TableBuilder.fromCsvFile(this); 
	}

}