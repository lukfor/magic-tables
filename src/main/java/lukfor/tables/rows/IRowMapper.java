package lukfor.tables.rows;

import java.io.IOException;

public interface IRowMapper {

	public Object getKey(Row row) throws IOException;
	
}
