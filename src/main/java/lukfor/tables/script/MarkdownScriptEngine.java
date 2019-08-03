package lukfor.tables.script;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.groovy.control.CompilationFailedException;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.FencedCodeBlock;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import genepi.io.FileUtil;
import groovy.lang.Writable;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;

public class MarkdownScriptEngine {

	public static final String DEFAULT_TEMPLATE = "templates/default.html";

	private ScriptEngine engine;

	private ByteArrayOutputStream out;

	public MarkdownScriptEngine(String baseDir, Map<String, String> params) {
		engine = new ScriptEngine(baseDir, params);
	}

	class CodeBlockVisitor extends AbstractVisitor {

		@Override
		public void visit(FencedCodeBlock fencedCodeBlock) {

			String scriptText = fencedCodeBlock.getLiteral();

			try {
				out.reset();
				engine.run(scriptText);
			} catch (Exception e) {
				e.printStackTrace(new PrintStream(out));
			}
			fencedCodeBlock.setLiteral("> " + scriptText.replaceAll("\n", "\n> ") + "\n\n" + out.toString());

			visitChildren(fencedCodeBlock);
		}

	}

	public void run(File input, File output) throws CompilationFailedException, ClassNotFoundException, IOException {

		System.out.println("Running script file " + input.getAbsolutePath() + "...");

		Parser parser = Parser.builder().build();
		Node document = parser.parse(FileUtil.readFileAsString(input.getAbsolutePath()));

		PrintStream oldOut = System.out;
		out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));

		document.accept(new CodeBlockVisitor());

		System.setOut(oldOut);

		HtmlRenderer renderer = HtmlRenderer.builder().build();
		String content = renderer.render(document);

		SimpleTemplateEngine templateEngine = new SimpleTemplateEngine();
		ClassLoader classLoader = MarkdownScriptEngine.class.getClassLoader();
		URL resource = classLoader.getResource(DEFAULT_TEMPLATE);
		Template template = templateEngine.createTemplate(resource);
		Map<String, String> bindingTemplate = new HashMap<String, String>();
		bindingTemplate.put("title", "lukfor-tables");
		bindingTemplate.put("author", System.getProperty("user.name"));
		bindingTemplate.put("content", content);
		bindingTemplate.put("date", new Date().toString());
		bindingTemplate.put("username", System.getProperty("user.name"));
		Writable writable = template.make(bindingTemplate);

		FileUtil.writeStringBufferToFile(output.getAbsolutePath(), new StringBuffer(writable.toString()));

		System.out.println("Output written to file " + output.getAbsolutePath());
	}

}
