package lukfor.tables.columns;

import java.io.IOException;

import lukfor.tables.rows.Row;

public interface IBuildValueFunction {

	public Object buildValue(Row row) throws IOException;

}
