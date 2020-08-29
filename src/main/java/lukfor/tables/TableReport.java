package lukfor.tables;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import lukfor.reports.HtmlWidgetsReport;
import lukfor.reports.widgets.IWidget;
import lukfor.reports.widgets.WidgetInstance;

public class TableReport extends HtmlWidgetsReport {

	public static final String DEFAULT_TEMPLATE = "/templates/default";

	private String template = DEFAULT_TEMPLATE;

	private StringBuilder content = new StringBuilder();

	private String title = "Table Report";
	
	private String filename = "";

	public TableReport(String filename) {
		super(DEFAULT_TEMPLATE);
		this.filename = filename;
		importWidget("data_table");
	}

	public void title(String title) {
		this.title = title;
		add(tag("h1", title));
	}

	public void section(String section) {
		add(tag("h2", section));
	}

	public void text(String text) {
		add(tag("p", text));
	}

	public void code(String text) {
		add(tag("pre", tag("code", text)));
	}

	public void table(Table table) {
		table(table, table.getColumns().getNames());
	}

	public void table(Table table, String... columns) {

		List<Object> configColumns = new Vector<Object>();

		for (int i = 0; i < columns.length; i++) {
			HashMap<String, Object> configColumn = new HashMap<String, Object>();
			configColumn.put("data", "_" + i);
			configColumn.put("title", columns[i]);
			configColumns.add(configColumn);
		}

		List<Object> data = new Vector<Object>();
		for (int i = 0; i < table.getRows().getSize(); i++) {
			HashMap<String, Object> dataObject = new HashMap<String, Object>();
			for (int j = 0; j < columns.length; j++) {
				dataObject.put("_" + j, table.get(i, columns[j]));
			}
			data.add(dataObject);
		}

		HashMap<String, Object> config = new HashMap<String, Object>();
		config.put("columns", configColumns);
		config.put("data", data);

		widget("data_table", config);

	}

	public void plot(Table table, String x, String y) {
		plot(table, x, y, "markers");
	}

	public void plot(Table table, String x, String y, String type) {		
		List<Table> tables = new Vector<>();
		tables.add(table);		
		plot(tables, x, y, type);
	}

	public void plot(Collection<Table> tables, String x, String y) {
		plot(tables, x, y, "markers");
	}

	public void plot(Collection<Table> tables, String x, String y, String type) {

		HashMap<String, Object> config = new HashMap<>();

		List<Object> traces = new Vector<>();

		for (Table table : tables) {
			HashMap<String, Object> trace = new HashMap<>();
			trace.put("x", table.getColumn(x).getValues());
			trace.put("y", table.getColumn(y).getValues());
			trace.put("mode", type);
			trace.put("type", type);
			trace.put("name", table.getName());
			traces.add(trace);
		}

		config.put("traces", traces);

		HashMap<String, Object> layout = new HashMap<>();
		//layout.put("title", x + " vs. " + y);

		HashMap<String, Object> xaxis = new HashMap<>();
		xaxis.put("title", x);
		layout.put("xaxis", xaxis);

		HashMap<String, Object> yaxis = new HashMap<>();
		yaxis.put("title", y);
		layout.put("yaxis", yaxis);

		config.put("layout", layout);

		widget("plotly", config);
	}

	public void plotBar(Table table, String x, String y) {
		HashMap<String, Object> config = new HashMap<>();

		HashMap<String, Object> trace = new HashMap<>();
		trace.put("x", table.getColumn(x).getValues());
		trace.put("y", table.getColumn(y).getValues());
		trace.put("mode", "bar");
		trace.put("type", "bar");
		config.put("traces", new Object[] { trace });

		HashMap<String, Object> layout = new HashMap<>();
		//layout.put("title", "histogram: " + x);

		HashMap<String, Object> xaxis = new HashMap<>();
		xaxis.put("title", x);
		layout.put("xaxis", xaxis);

		HashMap<String, Object> yaxis = new HashMap<>();
		yaxis.put("title", y);
		layout.put("yaxis", yaxis);

		config.put("layout", layout);

		widget("plotly", config);

	}

	public void hist(Table table, String column, double bin) throws IOException {
		Table hist = table.hist(column, bin);
		plotBar(hist, column, "count");
	}

	public void widget(String name, HashMap<String, Object> config) {
		IWidget widget = importWidget(name);
		WidgetInstance instance = widget.createInstance(config);
		addInstance(instance);
		content.append(instance.getHtml());
	}

	public void add(String tag) {
		this.content.append(tag);
	}

	public String tag(String tag, String content) {
		StringBuilder html = new StringBuilder();
		html.append("<");
		html.append(tag);
		html.append(">");
		html.append(content);
		html.append("</");
		html.append(tag);
		html.append(">");
		return html.toString();
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTemplate() {
		return template;
	}
	
	
	public void save() throws IOException {
		generate(new File(filename));
	}
	
	public void saveAndOpen() throws IOException {
		save();
		File htmlFile = new File(filename);
		Desktop.getDesktop().browse(htmlFile.toURI());
	}
	

	@Override
	public void generate(File outputFile) throws IOException {

		set("title", title);
		set("head", getHead());
		set("content", content.toString());
		set("date", new Date());

		super.generate(outputFile);
	}

}
