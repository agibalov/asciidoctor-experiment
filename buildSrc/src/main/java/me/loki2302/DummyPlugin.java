package me.loki2302;

import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.extension.BlockProcessor;
import org.asciidoctor.extension.Reader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DummyPlugin extends BlockProcessor {
    public DummyPlugin(String name, Map<String, Object> config) {
        super(name, new HashMap<String, Object>() {{
            put("contexts", Arrays.asList(":paragraph"));
            put("content_model", ":simple");
        }});
    }

    @Override
    public Object process(AbstractBlock parent, Reader reader, Map<String, Object> attributes) {
        return createBlock(
                parent,
                "paragraph",
                String.format("I am Java extension (%s)", reader.read()),
                attributes,
                new HashMap());
    }
}
