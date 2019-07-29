package lukfor.tables.rows;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class RowDuplicateProcessor implements IRowProcessor {

	private List<Boolean> bitmask = new Vector<Boolean>();

	private Set<Integer> uniques = new HashSet<Integer>();
	
	public void process(Row row) throws IOException {
		
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
