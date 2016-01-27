package me.loki2302.testtools;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

public class Documentation implements TestRule {
    private final String outputDirectory;
    private DocumentationContext context;

    public Documentation(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Path snippetsDirectoryPath = Paths.get(outputDirectory, description.getTestClass().getSimpleName(), description.getMethodName());
                snippetsDirectoryPath = Files.createDirectories(snippetsDirectoryPath);
                context = new DocumentationContext(snippetsDirectoryPath);
                try {
                    base.evaluate();
                } finally {
                    context = null;
                }
            }
        };
    }

    public void snippet(String name, Map<String, Object> model) {
        try {
            context.writeSnippetToFile(name, model);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void text(String name, String text) {
        try {
            context.writeTextToFile(name, text);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private static class DocumentationContext {
        private final Path snippetsDirectoryPath;

        public DocumentationContext(Path snippetsDirectoryPath) {
            this.snippetsDirectoryPath = snippetsDirectoryPath;
        }

        public void writeTextToFile(String name, String text) throws IOException {
            writeContentToFile(name, "txt", text);
        }

        public void writeSnippetToFile(String name, Map<String, Object> model) throws IOException {
            String templateFilename = name + ".adoc";

            Writer writer = new StringWriter();
            MustacheFactory mustacheFactory = new DefaultMustacheFactory();
            String template = Resources.toString(Resources.getResource(templateFilename), Charsets.UTF_8);
            Mustache mustache = mustacheFactory.compile(new StringReader(template), name);
            mustache.execute(writer, model);
            writer.flush();

            writeContentToFile(name, "adoc", writer.toString());
        }

        private void writeContentToFile(String name, String extension, String content) throws IOException {
            if(!Files.exists(snippetsDirectoryPath)) {
                try {
                    Files.createDirectories(snippetsDirectoryPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            String filename = name + "." + extension;
            Path contentFilePath = snippetsDirectoryPath.resolve(filename);
            Files.write(contentFilePath, Collections.singleton(content));
        }
    }
}
