package lukfor.tables.plotly;

import java.util.List;
import java.util.Vector;

public class Trace {

	public static final String DEFAULT_MODE = "markers";

	public static final String DEFAULT_TYPE = "markers";

	private String mode = DEFAULT_MODE;

	private String type = DEFAULT_TYPE;

	private String name = null;

	private List<Number> x = new Vector<>();

	private List<Number> y = new Vector<>();

	public Trace(String name) {
		this.name = name;
	}
	
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Number> getX() {
		return x;
	}

	public void setX(List<Number> x) {
		this.x = x;
	}

	public List<Number> getY() {
		return y;
	}

	public void setY(List<Number> y) {
		this.y = y;
	}

	public static String getDefaultMode() {
		return DEFAULT_MODE;
	}

	public static String getDefaultType() {
		return DEFAULT_TYPE;
	}

	public void addPoint(Number x, Number y) {
		this.x.add(x);
		this.y.add(y);
	}

}
