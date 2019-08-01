package lukfor.tables.script;

import java.util.HashMap;

public class ScriptContext {

	public static HashMap<String, String> params = new HashMap<String, String>() {

		private static final long serialVersionUID = 1L;

		public String put(String key, String value) {
			if (get(key) == null) {
				return super.put(key, value);
			} else {
				return null;
			}
		};
	};

}
