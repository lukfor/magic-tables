package lukfor.tables;

import java.io.File;
import java.io.IOException;
import java.util.List;

import genepi.io.FileUtil;
import junit.framework.TestCase;
import lukfor.tables.columns.AbstractColumn;
import lukfor.tables.columns.IApplyFunction;
import lukfor.tables.columns.IBuildValueFunction;
import lukfor.tables.columns.types.DoubleColumn;
import lukfor.tables.columns.types.IntegerColumn;
import lukfor.tables.io.TableBuilder;
import lukfor.tables.io.TableWriter;
import lukfor.tables.rows.Row;
import lukfor.tables.rows.filters.IRowFilter;

public class TableTest extends TestCase {

	public void testLoadCsv() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/dummy.csv").load();
		assertEquals(3, table.getColumns().getSize());
		assertEquals(3, table.getRows().getSize());
		assertEquals(0, table.get(0, "id"));
		assertEquals(1, table.get(1, "id"));
		assertEquals(2, table.get(2, "id"));
	}

	public void testLoadXls() throws IOException {

		Table table = TableBuilder.fromXlsFile("data/dummy.xls").load();
		assertEquals(3, table.getColumns().getSize());
		assertEquals(3, table.getRows().getSize());
		assertEquals(0, table.get(0, "id"));
		assertEquals(1, table.get(1, "id"));
		assertEquals(2, table.get(2, "id"));
		table.print();

	}

	public void testWriteCsv() throws IOException {

		// 2 missings in cloumn a
		Table table = TableBuilder.fromCsvFile("data/duplicates.csv").load();
		File output = File.createTempFile("test", ".csv");
		TableWriter.writeToCsv(table, output);
		assertEquals(FileUtil.readFileAsString("data/duplicates.csv"),
				FileUtil.readFileAsString(output.getAbsolutePath()));
	}

	public void testSortIntegerColumn() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/dummy.csv").load();
		assertEquals(3, table.getColumns().getSize());
		table.getRows().sortAscBy("a");
		assertEquals(3, table.getRows().getSize());
		assertEquals(2, table.get(0, "id"));
		assertEquals(1, table.get(1, "id"));
		assertEquals(0, table.getRows().get(2).getInteger("id"));

	}

	public void testSelectRows() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/dummy.csv").load();
		assertEquals(3, table.getColumns().getSize());
		table.getRows().select(new IRowFilter() {
			public boolean accepts(Row row) {
				return (row.getString("b").equals("z"));
			}
		});
		assertEquals(3, table.getColumns().getSize());
		assertEquals(1, table.getRows().getSize());

	}

	public void testSelectRowsByRegEx() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/dummy.csv").load();
		assertEquals(3, table.getColumns().getSize());
		table.getRows().selectByRegEx("b", "z|r");
		assertEquals(3, table.getColumns().getSize());
		assertEquals(2, table.getRows().getSize());

	}

	public void testGetRows() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/dummy.csv").load();
		assertEquals(3, table.getColumns().getSize());
		List<Row> rows = table.getRows().getAll(new IRowFilter() {
			public boolean accepts(Row row) {
				return (row.getString("b").equals("z"));
			}
		});
		assertEquals(1, rows.size());
		assertEquals(3, table.getColumns().getSize());
		assertEquals(3, table.getRows().getSize());

	}

	public void testGetRowsByRegEx() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/dummy.csv").load();
		assertEquals(3, table.getColumns().getSize());
		List<Row> rows = table.getRows().getAllByRegEx("b", "z");
		assertEquals(1, rows.size());
		assertEquals(3, table.getColumns().getSize());
		assertEquals(3, table.getRows().getSize());

	}

	public void testDropRows() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/dummy.csv").load();
		assertEquals(3, table.getColumns().getSize());
		table.getRows().drop(new IRowFilter() {
			public boolean accepts(Row row) {
				return (row.getString("b").equals("z"));
			}
		});
		assertEquals(3, table.getColumns().getSize());
		assertEquals(2, table.getRows().getSize());

	}

	public void testDropRowsByRegEx() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/dummy.csv").load();
		assertEquals(3, table.getColumns().getSize());
		table.getRows().dropByRegEx("b", "z|r");
		assertEquals(3, table.getColumns().getSize());
		assertEquals(1, table.getRows().getSize());

	}

	public void testDropMissings() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/missings.csv").load();
		assertEquals(5, table.getColumns().getSize());
		assertEquals(7, table.getRows().getSize());
		table.getRows().dropMissings();
		assertEquals(5, table.getColumns().getSize());
		assertEquals(4, table.getRows().getSize());

	}

	public void testDropMissingsByColumn() throws IOException {

		// 2 missings in cloumn a
		Table table = TableBuilder.fromCsvFile("data/missings.csv").load();
		assertEquals(5, table.getColumns().getSize());
		assertEquals(7, table.getRows().getSize());
		assertEquals(2, table.getColumns().get("a").getMissings());
		assertEquals(0, table.getColumns().get("d").getMissings());
		table.getRows().dropMissings("a");
		assertEquals(5, table.getColumns().getSize());
		assertEquals(5, table.getRows().getSize());

		// no missing in column d
		table = TableBuilder.fromCsvFile("data/missings.csv").load();
		assertEquals(5, table.getColumns().getSize());
		assertEquals(7, table.getRows().getSize());
		table.getRows().dropMissings("d");
		assertEquals(5, table.getColumns().getSize());
		assertEquals(7, table.getRows().getSize());

	}

	public void testFillMissings() throws IOException {

		// 2 missings in cloumn a
		Table table = TableBuilder.fromCsvFile("data/missings.csv").load();
		assertEquals(5, table.getColumns().getSize());
		assertEquals(7, table.getRows().getSize());
		assertEquals(2, table.getColumns().get("a").getMissings());
		table.getColumns().get("a").fillMissings(-1);
		assertEquals(0, table.getColumns().get("a").getMissings());

	}

	public void testReplaceValue() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/missings.csv").load();
		assertEquals(5, table.getColumns().getSize());
		assertEquals(7, table.getRows().getSize());
		assertEquals(2, table.getColumns().get("a").getMissings());
		table.getColumns().get("a").fillMissings(-1);
		assertEquals(0, table.getColumns().get("a").getMissings());
		table.getColumns().get("a").replaceValue(-1, null);
		assertEquals(2, table.getColumns().get("a").getMissings());
		table.getColumns().get("a").replaceValue(null, 9);
		assertEquals(0, table.getColumns().get("a").getMissings());
		table.getColumns().get("a").replaceValue(new Integer[] { 0, 1 }, new Integer[] { 11, 12 });
		assertEquals(0, table.getColumns().get("a").getMissings());

	}

	public void testGetUniqueValues() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/uniques.csv").load();
		assertEquals(2, table.getColumns().getSize());
		assertEquals(9, table.getRows().getSize());
		assertEquals(9, table.getColumns().get("a").getUniqueValues());
		assertEquals(4, table.getColumns().get("b").getUniqueValues());
		assertEquals(4 + 9, table.getUniqueValues());

	}

	public void testDropDuplicates() throws IOException {

		// 2 missings in cloumn a
		Table table = TableBuilder.fromCsvFile("data/duplicates.csv").load();
		assertEquals(5, table.getColumns().getSize());
		assertEquals(10, table.getRows().getSize());
		table.getRows().dropDuplicates();
		assertEquals(7, table.getRows().getSize());

	}

	public void testAppendColumn() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/dummy.csv").load();
		assertEquals(3, table.getColumns().getSize());
		assertEquals(3, table.getRows().getSize());
		table.getColumns().append(new IntegerColumn("id_2"), new IBuildValueFunction() {
			public Object buildValue(Row row) {
				return row.getInteger("id") * 2;
			}
		});
		assertEquals(4, table.getColumns().getSize());
		assertEquals(3, table.getRows().getSize());
		assertEquals(0, table.getRows().get(0).getInteger("id_2"));
		assertEquals(2, table.get(1, "id_2"));
		assertEquals(4, table.get(2, 3));

	}

	public void testApply() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/dummy.csv").load();
		assertEquals(3, table.getColumns().getSize());
		assertEquals(3, table.getRows().getSize());
		assertEquals(0, table.get(0, "id"));
		assertEquals(1, table.get(1, "id"));
		assertEquals(2, table.get(2, "id"));

		table.getColumn("id").apply(new IApplyFunction() {
			@Override
			public Object apply(Object value) {
				Integer integerValue = (Integer) value;
				return integerValue * 2;
			}
		});
		assertEquals(3, table.getColumns().getSize());
		assertEquals(3, table.getRows().getSize());
		assertEquals(0, table.get(0, "id"));
		assertEquals(2, table.get(1, "id"));
		assertEquals(4, table.get(2, "id"));

	}

	public void testDropColumns() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/dummy.csv").load();
		assertEquals(3, table.getColumns().getSize());
		assertEquals(3, table.getRows().getSize());
		table.getColumns().drop("id");
		assertEquals(2, table.getColumns().getSize());
		assertEquals(3, table.getRows().getSize());

	}

	public void testSelectColumns() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/dummy.csv").load();
		assertEquals(3, table.getColumns().getSize());
		assertEquals(3, table.getRows().getSize());
		table.getColumns().select("id");
		assertEquals(1, table.getColumns().getSize());
		assertEquals(3, table.getRows().getSize());

	}

	public void testSelectColumnsByRegEx() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/dummy.csv").load();
		assertEquals(3, table.getColumns().getSize());
		assertEquals(3, table.getRows().getSize());
		table.getColumns().selectByRegEx("id|a");
		assertEquals(2, table.getColumns().getSize());
		assertEquals(3, table.getRows().getSize());

	}

	public void testMerge() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/dummy.csv").load();
		assertEquals(3, table.getColumns().getSize());
		Table table2 = TableBuilder.fromCsvFile("data/dummy2.csv").load();
		assertEquals(4, table2.getColumns().getSize());
		table.merge(table2, "id");
		assertEquals(6, table.getColumns().getSize());
		assertEquals(3, table.getRows().getSize());
		File output = File.createTempFile("test", ".csv");
		TableWriter.writeToCsv(table, output);
		assertEquals(FileUtil.readFileAsString("data/dummy_joined.csv"),
				FileUtil.readFileAsString(output.getAbsolutePath()));
	}

	public void testAppendRow() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/dummy.csv").load();
		assertEquals(3, table.getColumns().getSize());
		assertEquals(3, table.getRows().getSize());
		Row row = table.getRows().append();
		row.set("id", 5);
		row.set("a", 5);
		row.set("b", null);
		assertEquals(3, table.getColumns().getSize());
		assertEquals(4, table.getRows().getSize());
		assertEquals(5, table.getRows().get(3).getObject("id"));
		assertEquals(5, table.getRows().get(3).getObject("a"));
		assertEquals(null, table.getRows().get(3).getObject("b"));

	}

	public void testAppendRowWrongTypes() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/dummy.csv").load();
		assertEquals(3, table.getColumns().getSize());
		assertEquals(3, table.getRows().getSize());
		Row row = table.getRows().append();
		row.set("id", 5);
		try {
			row.set("a", "test");
			assertTrue(false);
		} catch (Exception e) {
			// Wrong data type throw exception
		}
		row.set("b", "lukas");
		assertEquals(3, table.getColumns().getSize());
		assertEquals(4, table.getRows().getSize());
		assertEquals(5, table.getRows().get(3).getObject("id"));
		assertEquals(null, table.getRows().get(3).getObject("a"));
		assertEquals("lukas", table.getRows().get(3).getObject("b"));

	}

	public void testSummaryAndColumnTypeDetector() throws IOException {

		Table iris = TableBuilder.fromCsvFile("data/iris.csv").withSeparator(';').load();
		AbstractColumn column = iris.getColumns().get("sepal.length");
		assertTrue(column instanceof DoubleColumn);
		assertEquals(4.3, column.getMin());
		assertEquals(7.9, column.getMax());
		assertEquals(0, column.getMissings());

	}

	public void testGroupByKeyAndCount() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/groups.csv").load();

		Table groups = table.groupBy("group").count();
		groups.getRows().sortAscBy("group");

		assertEquals(2, groups.getColumns().getSize());
		assertEquals(3, groups.getRows().getSize());
		assertEquals(1, groups.get(0, "group"));
		assertEquals(2, groups.get(1, "group"));
		assertEquals(3, groups.get(2, "group"));
		assertEquals(3, groups.get(0, "count"));
		assertEquals(2, groups.get(1, "count"));
		assertEquals(4, groups.get(2, "count"));

	}

	public void testGroupByKeyAndSum() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/groups.csv").load();

		Table groups = table.groupBy("group").sum("value");
		groups.getRows().sortAscBy("group");

		assertEquals(2, groups.getColumns().getSize());
		assertEquals(3, groups.getRows().getSize());
		assertEquals(1, groups.get(0, "group"));
		assertEquals(2, groups.get(1, "group"));
		assertEquals(3, groups.get(2, "group"));
		assertEquals(6, groups.get(0, "sum"));
		assertEquals(3, groups.get(1, "sum"));
		assertEquals(10, groups.get(2, "sum"));

	}

	public void testGroupByKeyAndMin() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/groups.csv").load();

		Table groups = table.groupBy("group").min("value");
		groups.getRows().sortAscBy("group");

		assertEquals(2, groups.getColumns().getSize());
		assertEquals(3, groups.getRows().getSize());
		assertEquals(1, groups.get(0, "group"));
		assertEquals(2, groups.get(1, "group"));
		assertEquals(3, groups.get(2, "group"));
		assertEquals(1, groups.get(0, "min"));
		assertEquals(1, groups.get(1, "min"));
		assertEquals(1, groups.get(2, "min"));

	}

	public void testGroupByKeyAndMax() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/groups.csv").load();

		Table groups = table.groupBy("group").max("value");
		groups.getRows().sortAscBy("group");

		assertEquals(2, groups.getColumns().getSize());
		assertEquals(3, groups.getRows().getSize());
		assertEquals(1, groups.get(0, "group"));
		assertEquals(2, groups.get(1, "group"));
		assertEquals(3, groups.get(2, "group"));
		assertEquals(3, groups.get(0, "max"));
		assertEquals(2, groups.get(1, "max"));
		assertEquals(4, groups.get(2, "max"));

	}

	public void testGroupByKeyAndMean() throws IOException {

		Table table = TableBuilder.fromCsvFile("data/groups.csv").load();

		Table groups = table.groupBy("group").mean("value");
		groups.getRows().sortAscBy("group");

		assertEquals(2, groups.getColumns().getSize());
		assertEquals(3, groups.getRows().getSize());
		assertEquals(1, groups.get(0, "group"));
		assertEquals(2, groups.get(1, "group"));
		assertEquals(3, groups.get(2, "group"));
		assertEquals(2.0, groups.get(0, "mean"));
		assertEquals(1.5, groups.get(1, "mean"));
		assertEquals(2.5, groups.get(2, "mean"));

	}

}
