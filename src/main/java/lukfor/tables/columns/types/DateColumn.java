package lukfor.tables.columns.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lukfor.tables.columns.AbstractColumn;
import lukfor.tables.columns.ColumnType;

public class DateColumn extends AbstractColumn {

	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

	public String pattern = "yyyy.MM.dd";

	public DateColumn(String name) {
		super(100);
		setName(name);
	}

	public DateColumn(String name, String pattern) {
		super(100);
		setName(name);
		this.dateFormat = new SimpleDateFormat(pattern);
		this.pattern = pattern;
	}

	public DateColumn(String name, int capacity, String pattern) {
		super(capacity);
		setName(name);
		this.dateFormat = new SimpleDateFormat(pattern);
		this.pattern = pattern;
	}

	@Override
	public ColumnType getType() {
		return ColumnType.DATE;
	}

	@Override
	public Object valueToObject(String data) {
		if (data == null || data.isEmpty()) {
			return null;
		} else {
			try {
				return dateFormat.parse(data);
			} catch (ParseException e) {
				System.out.println("Column " + getName() + ": Error parsing '" + data + "' to date.");
				return null;
			}
		}
	}

	@Override
	public String objectToValue(Object data) {
		if (data != null) {
			return dateFormat.format(data);
		} else {
			return "";
		}
	}

	@Override
	public int compare(Object value1, Object value2) {
		if (value1 == null) {
			value1 = "";
		}
		if (value2 == null) {
			value2 = "";
		}
		return ((Date) value1).compareTo((Date) value2);
	}

	@Override
	public boolean accepts(Object data) {
		return data instanceof Date;
	}

	@Override
	public boolean isMissingValue(Object object) {
		return object == null;
	}

	@Override
	public AbstractColumn cloneStructure() {
		return new DateColumn(getName(), pattern);
	}

}
