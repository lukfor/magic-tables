package examples;

import java.io.IOException;

import lukfor.charts.Plot;
import lukfor.tables.Table;
import lukfor.tables.columns.types.DoubleColumn;
import lukfor.tables.columns.types.IntegerColumn;
import lukfor.tables.io.TableBuilder;
import lukfor.tables.rows.IRowAggregator;

public class Plotting {

	public static double BIN_SIZE = 0.01;

	public static void main(String[] args) throws IOException {

		Table table = TableBuilder.fromCsvFile("/home/lukas/chr1.info").withSeparator('\t').load();
		table.printSummary();

		Table histo = table.binBy("MAF", BIN_SIZE, new IRowAggregator() {

			@Override
			public Table aggregate(Object key, Table group) throws IOException {

				Table table = new Table(group.getName() + ":reduced");
				table.getColumns().append(new DoubleColumn("bin"));
				table.getColumns().append(new DoubleColumn("mean"));
				table.getColumns().append(new IntegerColumn("count"));

				Object mean = group.getColumn("Rsq").getMean();
				Object count = group.getColumn("Rsq").getSize();
				table.getRows().append(key, mean, count);

				return table;
			}
		});

		histo.getRows().sortAscBy("bin");
		histo.print();

		Plot.plot(histo, "bin", "mean");

	}

	public static double findBin(Double value) {
		return Math.floor(value / BIN_SIZE) * BIN_SIZE;
	}

}
