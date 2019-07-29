package lukfor.tables.rows;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Row {

	private Map<String, Object> values = new HashMap<String, Object>();

	public Row() {
	}

	public Object getObject(String column) throws IOException {
		if (values.containsKey(column)) {
			return values.get(column);
		} else {
			throw new IOException("Column '" + column + "' not found.");
		}
	}

	public String getString(String column) throws IOException {
		return (String) getObject(column);
	}

	public int getInteger(String column) throws IOException {
		return (Integer) getObject(column);
	}

	public double getDouble(String column) throws IOException {
		return (Double) getObject(column);
	}

	public void set(String column, Object value) {
		this.values.put(column, value);
	}

	public void remove(String column) {
		this.values.remove(column);
	}

	public boolean hasMissings() {
		for (String key: values.keySet()) {
			if (values.get(key) == null) {
				return true;
			}
		}
		return false;
	}
	
}
