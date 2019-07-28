package lukfor.tables.rows;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class RowGroupProcessor implements IRowProcessor{

	private IRowMapper mapper;

	private Map<Object, List<Integer>> groups = new HashMap<Object, List<Integer>>();
	
	private int index = -1;
	
	public RowGroupProcessor(IRowMapper mapper) {
		this.mapper = mapper;
	}
	
	@Override
	public void process(Row row) throws IOException {
		
		index++;
		
		Object key = mapper.getKey(row);
		List<Integer> rows = groups.get(key);
		if (rows == null) {
			rows = new Vector<Integer>();
			groups.put(key, rows);
		}
		rows.add(index);
	}
	
	
	public Map<Object, List<Integer>> getGroups() {
		return groups;
	}
}
