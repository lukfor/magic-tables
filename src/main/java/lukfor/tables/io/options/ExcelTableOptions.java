package lukfor.tables.io.options;

import java.io.IOException;

import lukfor.tables.Table;
import lukfor.tables.io.TableBuilder;

public class ExcelTableOptions {

	private String filename;

	private String encoding = "";

	private String missingValue = "";

	private String dateFormat = "";

	private boolean header = true;

	private boolean columnTypeDetection = true;

	public ExcelTableOptions(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public String getEncoding() {
		return encoding;
	}

	public ExcelTableOptions withEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	public String getMissingValue() {
		return missingValue;
	}

	public ExcelTableOptions withMissingValue(String missingValue) {
		this.missingValue = missingValue;
		return this;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public ExcelTableOptions withDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
		return this;
	}

	public boolean isHeader() {
		return header;
	}

	public ExcelTableOptions withHeader(boolean header) {
		this.header = header;
		return this;
	}

	public boolean isColumnTypeDetection() {
		return columnTypeDetection;
	}

	public ExcelTableOptions withColumnTypeDetection(boolean columnTypeDetection) {
		this.columnTypeDetection = columnTypeDetection;
		return this;
	}

	public Table load() throws IOException {
		return TableBuilder.fromXlsFile(this);
	}

}
