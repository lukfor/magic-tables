package lukfor.tables.columns.types;

import lukfor.tables.columns.AbstractColumn;
import lukfor.tables.columns.ColumnType;

public class DoubleColumn extends AbstractColumn {

	public DoubleColumn(String name) {
		setName(name);
	}

	@Override
	public ColumnType getType() {
		return ColumnType.DOUBLE;
	}

	@Override
	public Object valueToObject(String data) {
		if (data.equals(".") || data.equals("") || data.equals("NaN") || data.equals("*")) {
			return null;
		} else {
			return Double.parseDouble(data);
		}
	}

	@Override
	public String objectToValue(Object data) {
		if (data != null) {
			return data.toString();
		} else {
			return "";
		}
	}

	@Override
	public int compare(Object value1, Object value2) {
		return ((Double) value1).compareTo((Double) value2);
	}

	@Override
	public boolean accepts(Object data) {
		return data instanceof Double;
	}

	@Override
	public Double getSum() {
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
	public Double getMean() {
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

	public Double getMin() {
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

	public Double getMax() {
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

}
