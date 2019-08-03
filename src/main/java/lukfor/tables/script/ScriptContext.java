package lukfor.tables.script;

import java.util.HashMap;

public class ScriptContext {
	
	public static String baseDir = ".";

	public static HashMap<String, Object> params = new HashMap<String, Object>() {

		private static final long serialVersionUID = 1L;

		public Object put(String key, Object value) {
			if (get(key) == null) {
				return super.put(key, value.toString());
			} else {
				return null;
			}
		};
		
	};

}
