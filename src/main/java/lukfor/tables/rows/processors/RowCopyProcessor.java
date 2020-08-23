package lukfor.tables.rows.processors;

import java.io.IOException;

import lukfor.tables.Table;
import lukfor.tables.rows.IRowProcessor;
import lukfor.tables.rows.Row;

public class RowCopyProcessor implements IRowProcessor {

	private Table destination;

	public RowCopyProcessor(Table destination) {
		this.destination = destination;
	}

	@Override
	public void process(Row row) throws IOException {
		Row newRow = destination.getRows().append();
		newRow.fill(row);
	}

}
