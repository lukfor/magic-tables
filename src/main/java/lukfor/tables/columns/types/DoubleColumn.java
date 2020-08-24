package lukfor.tables.columns.types;

import java.text.NumberFormat;
import java.util.Locale;

import lukfor.tables.columns.AbstractColumn;
import lukfor.tables.columns.ColumnType;

public class DoubleColumn extends AbstractColumn {

	public static NumberFormat FORMAT;

	static {
		FORMAT = NumberFormat.getInstance(new Locale("en", "US"));
		FORMAT.setGroupingUsed(false);
		FORMAT.setMinimumFractionDigits(3);
		FORMAT.setMaximumFractionDigits(3);
	}

	public DoubleColumn(String name) {
		this(name, 100);;
	}

	public DoubleColumn(String name, int capacity) {
		super(capacity);
		setName(name);
	}

	@Override
	public ColumnType getType() {
		return ColumnType.DOUBLE;
	}

	@Override
	public Object valueToObject(String data) {
		if (data.equals(".") || data.equals("") || data.equalsIgnoreCase("NA") || data.equalsIgnoreCase("NaN") || data.equals("*")) {
			return null;
		} else {
			try {
				return Double.parseDouble(data);
			} catch (Exception e) {
				//System.out.println("Column " + getName() + ": Error parsing '" + data + "' to double.");
				throw e;
			}
		}
	}

	@Override
	public String objectToValue(Object data) {
		if (data != null) {
			return FORMAT.format(data);
		} else {
			return "";
		}
	}

	@Override
	public int compare(Object value1, Object value2) {
		if (value1 == null) {
			value1 = Double.MIN_VALUE;
		}
		if (value2 == null) {
			value2 = Double.MIN_VALUE;
		}
		return ((Double) value1).compareTo((Double) value2);
	}

	@Override
	public boolean accepts(Object data) {
		return data instanceof Double;
	}

	@Override
	public Object getSum() {
		double sum = 0;
		for (Object o : storage) {
			if (o != null) {
				Double d = (Double) o;
				sum += d;
			}
		}
		return sum;
	}

	@Override
	public Object getMean() {
		double sum = 0;
		int count = 0;
		for (Object o : storage) {
			if (o != null) {
				Double d = (Double) o;
				sum += d;
				count++;
			}
		}
		return sum / (double) count;
	}

	public Object getMin() {
		double min = Double.MAX_VALUE;
		for (Object o : storage) {
			if (o != null) {
				Double d = (Double) o;
				if (d < min) {
					min = d;
				}
			}
		}
		return min;
	}

	public Object getMax() {
		double max = Double.MIN_VALUE;
		for (Object o : storage) {
			if (o != null) {
				Double d = (Double) o;
				if (d > max) {
					max = d;
				}
			}
		}
		return max;
	}

	@Override
	public AbstractColumn cloneStructure() {
		return new DoubleColumn(getName());
	}

	@Override
	public boolean isMissingValue(Object object) {
		return object == null || object.toString().isEmpty();
	}

}
