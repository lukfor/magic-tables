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
		if (value1 == null) {
			value1 = Integer.MIN_VALUE;
		}
		if (value2 == null) {
			value2 = Integer.MIN_VALUE;
		}
		return ((Integer) value1).compareTo((Integer) value2);
	}

	@Override
	public boolean accepts(Object data) {
		return data instanceof Integer;
	}

	@Override
	public Object getSum() {
		int sum = 0;
		for (Object o : storage) {
			if (o != null) {
				Integer i = (Integer) o;
				sum += i;
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
				Integer i = (Integer) o;
				sum += i;
				count++;
			}
		}
		return sum / (double) count;
	}

	@Override
	public Object getMin() {
		int min = Integer.MAX_VALUE;
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

	@Override
	public Object getMax() {
		int max = Integer.MIN_VALUE;
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
	@Override
	public AbstractColumn cloneStructure() {
		return new IntegerColumn(getName());
	}
	
}
