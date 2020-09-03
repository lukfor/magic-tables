package lukfor.tables.columns;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import lukfor.tables.exceptions.TableException;

public abstract class AbstractColumn {

	protected List<Object> storage;

	private String name;

	public AbstractColumn(int initSize) {
		storage = new Vector<Object>(initSize);
	}

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

	public void replaceDataFrom(AbstractColumn column) {
		for (int i = 0; i < column.getSize(); i++) {
			Object object = column.get(i);
			if (object != null) {
				storage.set(i, valueToObject(object.toString()));
			} else {
				storage.set(i, null);
			}
		}
	}

	public List<Number> getValues() {
		List<Number> numbers = new Vector<Number>();
		for (int i = 0; i < getSize(); i++) {
			Object object = get(i);
			numbers.add((Number) object);
		}
		return numbers;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract ColumnType getType();

	public void set(int index, Object data) {
		if (isMissingValue(data)) {
			storage.set(index, null);
		} else {
			if (accepts(data)) {
				storage.set(index, data);
			} else {
				throw new RuntimeException("Object in column " + name + " ["+ getClass() +"] has wrong class: " + data.getClass());
			}

		}
	}

	public abstract boolean accepts(Object data);

	public abstract Object valueToObject(String data);

	public abstract String objectToValue(Object data);

	public void add(Object data) {
		if (isMissingValue(data)) {
			storage.add(null);
		} else {
			if (accepts(data)) {
				storage.add(data);
			} else {
				throw new RuntimeException("Object in column " + " ["+ getClass() +"] has wrong class: " + data.getClass());

			}

		}
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

	public void apply(IApplyFunction function) {
		for (int i = 0; i < storage.size(); i++) {
			Object value = storage.get(i);
			Object newValue = function.apply(value);
			storage.set(i, newValue);
		}
	}

	public void replaceValue(Object oldValue, Object newValue) {
		replaceValue(new Object[] { oldValue }, new Object[] { newValue });
	}

	public void replaceValue(Object[] oldValues, Object[] newValues) {

		if (oldValues.length != newValues.length) {
			throw new TableException("Arrays 'oldValues' and 'newValues' have different length.");
		}

		for (int i = 0; i < storage.size(); i++) {
			Object value = storage.get(i);
			for (int j = 0; j < oldValues.length; j++) {
				Object oldValue = oldValues[j];
				Object newValue = newValues[j];
				if (value == null && oldValue == null) {
					storage.set(i, newValue);
				} else if (value != null && value.equals(oldValue)) {
					storage.set(i, newValue);
				}
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

	public int getUniqueValues() {
		Set<Integer> uniques = new HashSet<Integer>();
		for (int i = 0; i < storage.size(); i++) {
			Object value = storage.get(i);
			if (value != null) {
				int hash = value.hashCode();
				uniques.add(hash);
			}
		}
		return uniques.size();

	}

	public Object getSum() {
		return null;
	}

	public Object getMean() {
		return null;
	}

	public Object getMin() {
		return null;
	}

	public Object getMax() {
		return null;
	}

	public Object getSd() {
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
		int missings = getMissings();
		out.println("  N: " + (getSize() - missings));
		out.println("  Missings: " + missings);
		Object min = getMin();
		if (min != null) {
			out.println("  Min.: " + min);
		}
		Object mean = getMean();
		if (mean != null) {
			out.println("  Mean.: " + mean);
		}
		/*
		 * Object median = getMedian(); if (median != null) { out.println("  Median.: "
		 * + median); }
		 */
		Object max = getMax();
		if (max != null) {
			out.println("  Max.: " + max);
		}
	}

	public abstract boolean isMissingValue(Object object);

	public abstract AbstractColumn cloneStructure();
}
