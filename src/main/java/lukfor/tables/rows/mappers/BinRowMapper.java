package lukfor.tables.rows.mappers;

import java.io.IOException;

import lukfor.tables.rows.IRowMapper;
import lukfor.tables.rows.Row;

public class BinRowMapper implements IRowMapper {

	private double binSize = 0;

	private String columnValue = "";

	public BinRowMapper(String columnValue, double binSize) {
		this.columnValue = columnValue;
		this.binSize = binSize;
	}

	@Override
	public Object getKey(Row row) throws IOException {
		double value = row.getDouble(columnValue);
		return Math.floor(value / binSize) * binSize;
	}

}