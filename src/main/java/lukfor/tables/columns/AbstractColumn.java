package lukfor.tables.columns;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Vector;

public abstract class AbstractColumn {

	protected List<Object> storage = new Vector<Object>();

	private String name;

	public void copyDataFrom(AbstractColumn column) {
		for (int i = 0; i < column.getSize(); i++) {
			Object object = column.get(i);
			if (object != null) {
				storage.add(valueToObject(object.toString()));
			} else {
				storage.add(null);
			}
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract ColumnType getType();

	public void set(int index, Object data) {
		storage.set(index, data);
	}

	public abstract boolean accepts(Object data);

	public abstract Object valueToObject(String data);

	public abstract String objectToValue(Object data);

	public void add(Object data) {
		storage.add(data);
	}

	public Object get(int index) {
		if (index < storage.size()) {
			return storage.get(index);
		} else {
			return null;
		}
	}

	public int getSize() {
		return storage.size();
	}

	public void drop(List<Boolean> bitmask) {
		List<Object> newStorage = new Vector<Object>();
		for (int i = 0; i < storage.size(); i++) {
			if (!bitmask.get(i)) {
				newStorage.add(storage.get(i));
			}
		}
		storage.clear();
		storage.addAll(newStorage);
		newStorage.clear();
	}

	public void select(List<Boolean> bitmask) {
		List<Object> newStorage = new Vector<Object>();
		for (int i = 0; i < storage.size(); i++) {
			if (bitmask.get(i)) {
				newStorage.add(storage.get(i));
			}
		}
		storage.clear();
		storage.addAll(newStorage);
		newStorage.clear();
	}

	public void sort(List<Integer> indices) {
		List<Object> newStorage = new Vector<Object>();
		for (int i = 0; i < indices.size(); i++) {
			int index = indices.get(i);
			Object value = storage.get(index);
			newStorage.add(value);
		}
		storage.clear();
		storage.addAll(newStorage);
		newStorage.clear();
	}

	public abstract int compare(Object value1, Object value2);

	@Override
	public String toString() {
		return name + " [" + getType() + "]";
	}

	public void fillMissings(Object value) {
		for (int i = 0; i < storage.size(); i++) {
			if (storage.get(i) == null) {
				storage.set(i, value);
			}
		}
	}

	public int getMissings() {
		int missings = 0;
		for (int i = 0; i < storage.size(); i++) {
			if (storage.get(i) == null) {
				missings++;
			}
		}
		return missings;
	}

	public Double getSum() {
		return null;
	}

	public Double getMean() {
		return null;
	}

	public Double getMin() {
		return null;
	}

	public Double getMax() {
		return null;
	}

	public Double getMedian() {
		return null;
	}

	public Integer getMissingValue() {
		int count = 0;
		for (Object o : storage) {
			if (o == null) {
				count++;
			}
		}
		return count;
	}

	public Integer getUniqueValues() {
		return null;
	}

	public String getSummary() {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps;
		try {
			ps = new PrintStream(baos, true, "UTF-8");
			printSummary(ps);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new String(baos.toByteArray(), StandardCharsets.UTF_8);
	}

	public void printSummary() {
		printSummary(System.out);
	}

	public void printSummary(PrintStream out) {
		out.println(name + " [" + getType() + "] :");
		int missings = getMissingValue();
		out.println("  N: " + (getSize() - missings));
		out.println("  Missings: " + missings);
		Double min = getMin();
		if (min != null) {
			out.println("  Min.: " + min);
		}
		Double mean = getMean();
		if (mean != null) {
			out.println("  Mean.: " + mean);
		}
		Double median = getMedian();
		if (median != null) {
			out.println("  Median.: " + median);
		}
		Double max = getMax();
		if (max != null) {
			out.println("  Max.: " + max);
		}
	}
}
