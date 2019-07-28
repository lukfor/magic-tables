package lukfor.tables.columns.types;

import lukfor.tables.columns.AbstractColumn;
import lukfor.tables.columns.ColumnType;

public class IntegerColumn extends AbstractColumn {

	public IntegerColumn(String name) {
		setName(name);
	}

	@Override
	public ColumnType getType() {
		return ColumnType.INTEGER;
	}

	@Override
	public Object valueToObject(String data) {
		if (data.equals(".") || data.equals("") || data.equals("NaN") || data.equals("*")) {
			return null;
		} else {
			return Integer.parseInt(data);
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
		return ((Integer) value1).compareTo((Integer) value2);
	}

	@Override
	public boolean accepts(Object data) {
		return data instanceof Integer;
	}

	@Override
	public Double getSum() {
		double sum = 0;
		for (Object o : storage) {
			if (o != null) {
				Integer i = (Integer) o;
				sum += i;
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
				Integer i = (Integer) o;
				sum += i;
				count++;
			}
		}
		return sum / (double) count;
	}

	public Double getMin() {
		double min = Double.MAX_VALUE;
		for (Object o : storage) {
			if (o != null) {
				Integer i = (Integer) o;
				if (i < min) {
					min = i;
				}
			}
		}
		return min;
	}

	public Double getMax() {
		double max = Double.MIN_VALUE;
		for (Object o : storage) {
			if (o != null) {
				Integer i = (Integer) o;
				if (i > max) {
					max = i;
				}
			}
		}
		return max;
	}
	
}
