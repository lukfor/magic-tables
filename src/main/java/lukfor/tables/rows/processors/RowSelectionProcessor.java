package lukfor.tables.rows.processors;

import java.util.List;
import java.util.Vector;

import lukfor.tables.rows.IRowProcessor;
import lukfor.tables.rows.Row;
import lukfor.tables.rows.filters.IRowFilter;

public class RowSelectionProcessor implements IRowProcessor {

	private IRowFilter filter;

	private List<Boolean> bitmask = new Vector<Boolean>();

	public RowSelectionProcessor(IRowFilter filter) {
		this.filter = filter;
	}

	public void process(Row row) {
		bitmask.add(filter.accepts(row));
	}

	public List<Boolean> getBitmask() {
		return bitmask;
	}
}
