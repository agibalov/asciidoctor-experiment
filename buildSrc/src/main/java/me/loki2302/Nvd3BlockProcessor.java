package me.loki2302;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.extension.BlockProcessor;
import org.asciidoctor.extension.Reader;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.io.IOException;
import java.util.*;

public class Nvd3BlockProcessor extends BlockProcessor {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Nvd3BlockProcessor(String name, Map<String, Object> config) {
        super(name, new HashMap<String, Object>() {{
            put("contexts", Arrays.asList(":listing"));
        }});
    }

    @Override
    public Object process(AbstractBlock parent, Reader reader, Map<String, Object> attributes) {
        Model model;
        try {
            model = OBJECT_MAPPER.readValue(reader.read(), Model.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String content;
        try {
            content = makeSnippet(model);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return createBlock(
                parent,
                "pass",
                content,
                attributes,
                new HashMap());
    }

    private static String makeSnippet(Model model) throws IOException {
        String templateString = Resources.toString(Resources.getResource("nvd3-pie.html"), Charsets.UTF_8);
        String ejsString = Resources.toString(
                Resources.getResource("META-INF/resources/webjars/ejs/2.4.1/ejs-v2.4.1/ejs.js"),
                Charsets.UTF_8);
        Context context = Context.enter();
        try {
            Scriptable scope = context.initStandardObjects();

            ScriptableObject.putProperty(scope, "template", templateString);
            ScriptableObject.putProperty(scope, "model", OBJECT_MAPPER.writeValueAsString(model));
            context.evaluateString(scope, "window = {}", "browser.js", 1, null);
            context.evaluateString(scope, ejsString, "ejs.js", 1, null);

            String content = (String)context.evaluateString(
                    scope,
                    "window.ejs.render(template, { model: model })",
                    "renderer.js",
                    1,
                    null);

            return content;
        } finally {
            context.exit();
        }
    }

    public static class Model {
        public String id;
        public List<DataPoint> data;
    }

    public static class DataPoint {
        public String key;
        public int value;
    }
}
