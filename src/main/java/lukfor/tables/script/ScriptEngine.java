package lukfor.tables.script;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

public class ScriptEngine {

	private GroovyShell shell;

	public ScriptEngine(String baseDir, Map<String, String> params) {

		CompilerConfiguration compilerConfiguration = createCompilerConfiguration();

		initScriptContext(baseDir, params);

		
		Binding binding = new Binding();
		shell = new GroovyShell(compilerConfiguration);
	}

	public void run(File file) throws CompilationFailedException, IOException {
		Script script = shell.parse(file);
		script.run();
	}

	public void run(String scriptText) throws CompilationFailedException, IOException {
		Script script = shell.parse(scriptText);
		script.run();
	}

	private CompilerConfiguration createCompilerConfiguration() {

		ImportCustomizer customizer = new ImportCustomizer();
		customizer.addStarImports("lukfor.tables");
		customizer.addStarImports("lukfor.tables.io");
		customizer.addStarImports("lukfor.tables.rows");
		customizer.addStarImports("lukfor.tables.columns");
		customizer.addStarImports("lukfor.tables.columns.types");
		customizer.addStaticStars("lukfor.tables.io.TableBuilder");
		customizer.addStaticStars("lukfor.tables.script.ScriptContext");

		CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
		compilerConfiguration.addCompilationCustomizers(customizer);

		return compilerConfiguration;

	}

	private void initScriptContext(String baseDir, Map<String, String> params) {
		for (String param : params.keySet()) {
			ScriptContext.params.put(param, params.get(param));
		}
		ScriptContext.baseDir = baseDir;
	}

}
