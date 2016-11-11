package me.loki2302;

import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.extension.BlockProcessor;
import org.asciidoctor.extension.Reader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HtmlBlockProcessor extends BlockProcessor {
    public HtmlBlockProcessor(String name, Map<String, Object> config) {
        super(name, new HashMap<String, Object>() {{
            put("contexts", Arrays.asList(":paragraph"));
        }});
    }

    @Override
    public Object process(AbstractBlock parent, Reader reader, Map<String, Object> attributes) {
        return createBlock(
                parent,
                "pass",
                String.format("<div style='color:red;'>%s</div>", reader.read()),
                attributes,
                new HashMap());
    }
}
