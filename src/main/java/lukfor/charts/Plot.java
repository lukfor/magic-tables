package lukfor.charts;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.ChartTheme;

import lukfor.tables.Table;

public class Plot {

	public static void plotXY(Table table, String x, String y) {

		XYChart chart = new XYChartBuilder().width(800).height(600).theme(ChartTheme.GGPlot2).build();
		chart.setXAxisTitle(x);
		chart.setYAxisTitle(y);

		// Customize Chart
		chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);

		// Series
		chart.addSeries("xy", table.getColumn(x).getValues(), table.getColumn(y).getValues());

		new SwingWrapper<XYChart>(chart).displayChart();

	}
	
	public static void plot(Table table, String x, String y) {

		XYChart chart = new XYChartBuilder().width(800).height(600).theme(ChartTheme.GGPlot2).build();
		chart.setXAxisTitle(x);
		chart.setYAxisTitle(y);

		// Series
		chart.addSeries("xy", table.getColumn(x).getValues(), table.getColumn(y).getValues());

		new SwingWrapper<XYChart>(chart).displayChart();

	}

}
