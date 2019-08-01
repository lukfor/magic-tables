package lukfor.tables;

import java.io.File;
import java.io.IOException;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import lukfor.tables.script.ScriptContext;

public class ScriptEngine {

	public void run(String filename, String[] params) throws ResourceException, ScriptException, IOException {
			
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
		
		for (int i =0; i<params.length; i+=2) {
			ScriptContext.params.put(params[i].replaceAll("--", ""), params[i+1]);
		}
		
		GroovyShell shell = new GroovyShell(compilerConfiguration);
		Script script = shell.parse(new File(filename));
		script.run();

	}
	
	public static void main(String[] args) throws ResourceException, ScriptException, IOException {
	
		//new ScriptEngine().run("examples/hello-tables.tables",new String[] {"--input", "lukas.csv"});
		new ScriptEngine().run("examples/hello-tables.tables",new String[] {});
		
	}
	
}
