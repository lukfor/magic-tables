package lukfor.tables.rows.processors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import lukfor.tables.rows.IRowProcessor;
import lukfor.tables.rows.Row;

public class RowDuplicateProcessor implements IRowProcessor {

	private List<Boolean> bitmask = new Vector<Boolean>();

	private Set<Integer> uniques = new HashSet<Integer>();
	
	public void process(Row row) {
		
		int hash = row.getHashCode();
		if (uniques.contains(hash)) {
			bitmask.add(true);
		}else {
			bitmask.add(false);
			uniques.add(hash);
		}
	}

	public List<Boolean> getBitmask() {
		return bitmask;
	}
}
