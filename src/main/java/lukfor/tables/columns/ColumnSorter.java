package lukfor.tables.columns;

import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class ColumnSorter {

	public static final int SORT_ASCEND = 1;
	
	public static final int SORT_DESCEND = -1;
	
	private List<Integer> indices = new Vector<Integer>();

	public ColumnSorter(final AbstractColumn column, final int order) {
		for (int i = 0; i < column.getSize(); i++) {
			indices.add(i);
		}

		indices.sort(new Comparator<Integer>() {
			public int compare(Integer index1, Integer index2) {
				Object value1 = column.get(index1);
				Object value2 = column.get(index2);
				return column.compare(value1, value2) * order;
			}
		});

	}

	public List<Integer> getIndices() {
		return indices;
	}
}
