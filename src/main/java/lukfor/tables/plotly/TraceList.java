package lukfor.tables.plotly;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class TraceList {

	private List<Trace> traces = new Vector<Trace>();

	private Map<String, Trace> tracesIndex = new HashMap<String, Trace>();

	public Trace getByName(String name) {
		Trace trace = tracesIndex.get(name);
		if (trace == null) {
			trace = new Trace(name);
			traces.add(trace);
			tracesIndex.put(name, trace);
		}
		return trace;
	}

	public List<Trace> getTraces() {
		return traces;
	}
	
}
