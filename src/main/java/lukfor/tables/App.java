package lukfor.tables;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.groovy.control.CompilationFailedException;

import lukfor.tables.script.MarkdownScriptEngine;
import lukfor.tables.script.ScriptEngine;

public class App {

	public App() {

	}

	public int run(String[] args) throws CompilationFailedException, IOException, ClassNotFoundException {

		if (args.length < 1) {
			System.out.println("Please specify input file");
			return -1;
		}

		Map<String, String> params = new HashMap<String, String>();
		for (int i = 1; i < args.length; i += 2) {
			String name = args[i].replaceAll("--", "");
			String value = args[i + 1];
			params.put(name, value);
		}

		String filename = args[0];
		File script = new File(filename);
		if (!script.exists()) {
			System.out.println("File '" + filename + "' not found.");
			return -1;
		}
		String baseDir = ".";
		if (script.getParentFile() != null) {
			baseDir = script.getParentFile().getAbsolutePath();
		}

		if (filename.endsWith(".mtbl")) {
			ScriptEngine engine = new ScriptEngine(baseDir, params);
			engine.run(script);
		} else if (filename.endsWith(".mtbl.md")) {
			MarkdownScriptEngine engine = new MarkdownScriptEngine(baseDir, params);
			String output = filename.replaceAll(".mtbl.md", ".html");
			engine.run(script, new File(output));
		} else {
			System.out.println("Please specify a mtbl or mtbl.md file.");
			return -1;
		}

		return 0;

	}

	public static void main(String[] args) throws CompilationFailedException, IOException, ClassNotFoundException {
		App app = new App();
		int result = app.run(args);
		System.exit(result);
	}

}
